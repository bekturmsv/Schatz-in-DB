import alasql from 'alasql';

const transliterateGerman = (text) => {
    return text
        .replace(/ä/g, 'ae')
        .replace(/ö/g, 'oe')
        .replace(/ü/g, 'ue')
        .replace(/ß/g, 'ss');
};

function transliterateSampleData(sampleData) {
    const newData = {};
    Object.entries(sampleData).forEach(([tableName, rows]) => {
        const newTableName = transliterateGerman(tableName);
        newData[newTableName] = rows.map(row => {
            const newRow = {};
            Object.entries(row).forEach(([key, value]) => {
                newRow[transliterateGerman(key)] = value;
            });
            return newRow;
        });
    });
    return newData;
}

// Наивно транслитерировать запрос (осторожно — только если ВСЕ идентификаторы латинские/немецкие)
function transliterateQuery(query) {
    // Поддерживает только латинские и немецкие буквы, иначе могут быть баги с ключевыми словами!
    return query
        .replace(/[\wäöüß]+/gi, (word) => transliterateGerman(word));
}

export const executeSqlQuery = (sqlQuery, sampleData) => {
    if (!sqlQuery || !sampleData) {
        throw new Error('SQL query or sample data is missing');
    }

    // Транслитерируем sampleData и sqlQuery
    const transliteratedSampleData = transliterateSampleData(sampleData);
    const transliteratedSqlQuery = transliterateQuery(sqlQuery);

    try {
        alasql('DROP DATABASE IF EXISTS tempdb');
        alasql('CREATE DATABASE tempdb');
        alasql('USE tempdb');

        // Создаём таблицы
        Object.entries(transliteratedSampleData).forEach(([tableName, data]) => {
            if (!Array.isArray(data) || data.length === 0) {
                alasql(`CREATE TABLE [${tableName}]`);
            } else {
                const columns = Object.keys(data[0])
                    .map((col) => `[${col}] TEXT`)
                    .join(', ');
                alasql(`CREATE TABLE [${tableName}] (${columns})`);
                data.forEach((row) => {
                    const cols = Object.keys(row).map((col) => `[${col}]`).join(', ');
                    const vals = Object.values(row).map((v) =>
                        typeof v === 'number' ? v : `'${v}'`
                    ).join(', ');
                    alasql(
                        `INSERT INTO [${tableName}] (${cols}) VALUES (${vals})`
                    );
                });
            }
        });

        // Выполнить транслитерированный запрос!
        const result = alasql(transliteratedSqlQuery);

        alasql('DROP DATABASE IF EXISTS tempdb');

        return Array.isArray(result) && result.length > 0 ? result : [{ result: 'No results' }];
    } catch (error) {
        console.error('Error executing SQL query:', error);
        throw new Error('Invalid SQL query');
    }
};
