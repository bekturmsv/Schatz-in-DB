import { useState } from "react";
import { useTranslation } from "react-i18next";
// import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

const AdminTaskCreate = () => {
    const { t } = useTranslation();
    // const navigate = useNavigate();

    const [task, setTask] = useState({
        name: "",
        topic: "",
        points: "",
        description: "",
        hint: "",
        testTable: {},
        correctAnswer: "",
    });

    const [tables, setTables] = useState([
        { name: "Table1", columns: ["Column1"], rows: [["Value1"]] },
    ]);

    const [correctQuery, setCorrectQuery] = useState("");
    const [queryResult, setQueryResult] = useState(null);

    // Добавление новой таблицы
    const addTable = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setTables((prev) => [
            ...prev,
            { name: `Table${prev.length + 1}`, columns: ["Column1"], rows: [["Value1"]] },
        ]);
    };

    // Удаление таблицы
    const removeTable = (e, tableIndex) => {
        e.preventDefault();
        e.stopPropagation();
        if (tables.length > 1) {
            setTables((prev) => prev.filter((_, idx) => idx !== tableIndex));
        }
    };

    // Обновление имени таблицы
    const updateTableName = (tableIndex, newName) => {
        setTables((prev) =>
            prev.map((tbl, idx) =>
                idx === tableIndex ? { ...tbl, name: newName } : tbl
            )
        );
    };

    // Добавление столбца в таблицу
    const addColumn = (e, tableIndex) => {
        e.preventDefault();
        e.stopPropagation();
        setTables((prev) =>
            prev.map((tbl, idx) => {
                if (idx !== tableIndex) return tbl;
                const nextCol = `Column${tbl.columns.length + 1}`;
                return {
                    ...tbl,
                    columns: [...tbl.columns, nextCol],
                    rows: tbl.rows.map((row) => [...row, ""]),
                };
            })
        );
    };

    // Удаление столбца из таблицы
    const removeColumn = (e, tableIndex) => {
        e.preventDefault();
        e.stopPropagation();
        setTables((prev) =>
            prev.map((tbl, idx) => {
                if (idx !== tableIndex) return tbl;
                if (tbl.columns.length <= 1) return tbl;
                return {
                    ...tbl,
                    columns: tbl.columns.slice(0, -1),
                    rows: tbl.rows.map((row) => row.slice(0, -1)),
                };
            })
        );
    };

    // Добавление строки в таблицу (копируем первую строку как шаблон)
    const addRow = (e, tableIndex) => {
        e.preventDefault();
        e.stopPropagation();
        setTables((prev) =>
            prev.map((tbl, idx) => {
                if (idx !== tableIndex) return tbl;
                const template =
                    tbl.rows[0]?.slice() || tbl.columns.map((_, i) => `Value${i + 1}`);
                return {
                    ...tbl,
                    rows: [...tbl.rows, template],
                };
            })
        );
    };

    // Удаление строки из таблицы
    const removeRow = (e, tableIndex) => {
        e.preventDefault();
        e.stopPropagation();
        setTables((prev) =>
            prev.map((tbl, idx) => {
                if (idx !== tableIndex) return tbl;
                if (tbl.rows.length <= 1) return tbl;
                return {
                    ...tbl,
                    rows: tbl.rows.slice(0, -1),
                };
            })
        );
    };

    // Обновление значения ячейки
    const updateCell = (tableIndex, rowIndex, colIndex, value) => {
        setTables((prev) =>
            prev.map((tbl, idx) => {
                if (idx !== tableIndex) return tbl;
                const newRows = tbl.rows.map((row, r) =>
                    r === rowIndex
                        ? row.map((cell, c) => (c === colIndex ? value : cell))
                        : row
                );
                return { ...tbl, rows: newRows };
            })
        );
    };

    // Преобразование таблиц в JSON
    const tablesToJson = () => {
        const tablesJson = {};
        tables.forEach((table) => {
            tablesJson[table.name.toLowerCase()] = {
                columns: table.columns,
                rows: table.rows,
            };
        });
        return JSON.stringify(tablesJson);
    };

    // Обработка ввода полей
    const handleChange = (e) => {
        const { name, value } = e.target;
        setTask((prev) => ({ ...prev, [name]: value }));
    };

    // Выполнение correctQuery для получения правильного ответа
    const executeCorrectQuery = (e) => {
        e.preventDefault();
        e.stopPropagation();
        const normalizedQuery = correctQuery.trim().toLowerCase(); // Нормализация запроса
        console.log("Executing normalized query:", normalizedQuery);
        if (!normalizedQuery || !tables.length) {
            toast.error(t("invalidQueryOrTable"));
            return;
        }

        const alasql = window.alasql;
        const tableData = {};
        tables.forEach((table) => {
            const tableName = table.name.toLowerCase();
            console.log(`Preparing table data for ${tableName}:`, {
                columns: table.columns,
                rows: table.rows,
            });
            tableData[tableName] = table.rows.map((row) =>
                table.columns.reduce((obj, col, i) => {
                    obj[col] = row[i];
                    return obj;
                }, {})
            );
        });
        console.log("Table data sent to alasql:", tableData);

        // Явная регистрация таблицы
        Object.keys(tableData).forEach((tableName) => {
            alasql.tables[tableName] = {
                data: tableData[tableName],
            };
        });

        try {
            const result = alasql(normalizedQuery);
            console.log("Query result:", result);
            setQueryResult(result);
            setTask((prev) => ({
                ...prev,
                correctAnswer: JSON.stringify(result),
            }));
            toast.success(t("queryExecuted"));
        } catch (error) {
            console.error("Query error:", error.message);
            toast.error(t("invalidQuery"));
            setQueryResult(null);
            setTask((prev) => ({ ...prev, correctAnswer: "" }));
        }
    };

    // Сохранение задачи
    const handleSave = (e) => {
        e.preventDefault();
        e.stopPropagation();
        if (!task.correctAnswer) {
            toast.error(t("executeQueryFirst"));
            return;
        }
        const taskData = {
            id: Date.now(),
            name: task.name,
            topic: task.topic,
            points: parseInt(task.points),
            description: task.description,
            hint: task.hint,
            testTable: tablesToJson(),
            correctAnswer: task.correctAnswer,
        };
        console.log("Saving task:", taskData);
        // navigate("/admin");
    };

    return (
        <div className="min-h-screen p-4 font-custom">
            <h1 className="text-3xl font-bold mb-4 text-primary">{t("createTask")}</h1>
            <div className="space-y-4">
                <input
                    name="name"
                    value={task.name}
                    onChange={handleChange}
                    placeholder={t("taskName")}
                    className="w-full p-2 border rounded"
                />
                <input
                    name="topic"
                    value={task.topic}
                    onChange={handleChange}
                    placeholder={t("taskTopic")}
                    className="w-full p-2 border rounded"
                />
                <input
                    name="points"
                    type="number"
                    value={task.points}
                    onChange={handleChange}
                    placeholder={t("taskPoints")}
                    className="w-full p-2 border rounded"
                />
                <textarea
                    name="description"
                    value={task.description}
                    onChange={handleChange}
                    placeholder={t("taskDescription")}
                    className="w-full p-2 border rounded h-24"
                />
                <textarea
                    name="hint"
                    value={task.hint}
                    onChange={handleChange}
                    placeholder={t("taskHint")}
                    className="w-full p-2 border rounded h-24"
                />
                <textarea
                    name="correctQuery"
                    value={correctQuery}
                    onChange={(e) => setCorrectQuery(e.target.value)}
                    placeholder={t("correctQuery")}
                    className="w-full p-2 border rounded h-24"
                />
                <button
                    type="button"
                    onClick={executeCorrectQuery}
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition mt-2"
                >
                    {t("executeQuery")}
                </button>
                {queryResult && (
                    <div className="mt-2">
                        <h3 className="text-lg font-bold text-primary">{t("queryResult")}:</h3>
                        <pre className="bg-gray-100 p-2 rounded">
              {JSON.stringify(queryResult, null, 2)}
            </pre>
                    </div>
                )}

                <div>
                    <h2 className="text-xl font-bold mb-2 text-primary">{t("testTables")}</h2>
                    {tables.map((table, tableIndex) => (
                        <div key={tableIndex} className="mb-6 border p-4 rounded-lg">
                            <div className="flex justify-between items-center mb-2">
                                <input
                                    value={table.name}
                                    onChange={(e) => updateTableName(tableIndex, e.target.value)}
                                    placeholder={t("tableName")}
                                    className="p-2 border rounded"
                                />
                                <div>
                                    <button
                                        type="button"
                                        onClick={(e) => removeTable(e, tableIndex)}
                                        className="bg-red-500 text-white px-2 py-1 rounded mr-2"
                                        disabled={tables.length === 1}
                                    >
                                        {t("removeTable")}
                                    </button>
                                    <button
                                        type="button"
                                        onClick={addTable}
                                        className="bg-green-500 text-white px-2 py-1 rounded"
                                    >
                                        {t("addTable")}
                                    </button>
                                </div>
                            </div>
                            <table className="border-collapse border border-gray-300 w-full">
                                <thead>
                                <tr>
                                    {table.columns.map((col, colIndex) => (
                                        <th key={colIndex} className="border p-2">
                                            <input
                                                value={col}
                                                onChange={(e) => {
                                                    const newCols = [...table.columns];
                                                    newCols[colIndex] = e.target.value;
                                                    setTables((prev) =>
                                                        prev.map((tbl, idx) =>
                                                            idx === tableIndex ? { ...tbl, columns: newCols } : tbl
                                                        )
                                                    );
                                                }}
                                                className="w-full p-1"
                                            />
                                        </th>
                                    ))}
                                    <th className="border p-2">
                                        <button
                                            type="button"
                                            onClick={(e) => addColumn(e, tableIndex)}
                                            className="bg-green-500 text-white px-2 py-1 rounded"
                                        >
                                            +
                                        </button>
                                        <button
                                            type="button"
                                            onClick={(e) => removeColumn(e, tableIndex)}
                                            className="bg-red-500 text-white px-2 py-1 rounded ml-1"
                                            disabled={table.columns.length === 1}
                                        >
                                            –
                                        </button>
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                {table.rows.map((row, rowIndex) => (
                                    <tr key={rowIndex}>
                                        {row.map((cell, colIndex) => (
                                            <td key={colIndex} className="border p-2">
                                                <input
                                                    value={cell}
                                                    onChange={(e) =>
                                                        updateCell(tableIndex, rowIndex, colIndex, e.target.value)
                                                    }
                                                    className="w-full p-1"
                                                />
                                            </td>
                                        ))}
                                        <td className="border p-2">
                                            <button
                                                type="button"
                                                onClick={(e) => addRow(e, tableIndex)}
                                                className="bg-green-500 text-white px-2 py-1 rounded"
                                            >
                                                +
                                            </button>
                                            <button
                                                type="button"
                                                onClick={(e) => removeRow(e, tableIndex)}
                                                className="bg-red-500 text-white px-2 py-1 rounded ml-1"
                                                disabled={table.rows.length === 1}
                                            >
                                                –
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    ))}
                </div>

                <button
                    type="button"
                    onClick={handleSave}
                    className="bg-primary text-white px-6 py-2 rounded-lg hover:bg-opacity-90 transition"
                >
                    {t("saveTask")}
                </button>
            </div>
        </div>
    );
};

export default AdminTaskCreate;