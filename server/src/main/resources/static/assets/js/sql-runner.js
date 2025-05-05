// sql-runner.js
let SQL;
let db;

async function initSqlJsDatabase() {
    SQL = await initSqlJs({ locateFile: file => `https://sql.js.org/dist/${file}` });
    db = new SQL.Database();
    console.log("Database initialized");
}

function resetDatabase() {
    if (SQL) {
        db = new SQL.Database();
        console.log("Database reset");
    }
}

function runQuery(query) {
    if (!db || !query) return "Error: No database or query.";
    try {
        const result = db.exec(query);
        if (!result.length) return "Query executed successfully (no result)";
        return JSON.stringify(result[0]); // return raw JSON output of first result set
    } catch (err) {
        return "SQL Error: " + err.message;
    }

}


function bindSqlHandlers() {
    document.querySelectorAll("[data-sql-action]").forEach(button => {
        button.addEventListener("click", function () {
            const action = this.dataset.sqlAction;
            if (action === "run-setup") {
                const query = document.querySelector("[name='setup-query']").value;
                const result = runQuery(query);
                document.querySelector("[name='setup-query-result']").value = result;
            } else if (action === "reset-db") {
                resetDatabase();
                document.querySelector("[name='setup-query-result']").value = "Database reset.";
                document.querySelector("[name='solution-query-result']").value = "";
            } else if (action === "run-solution") {
                const query = document.querySelector("[name='solution-query']").value;
                const result = runQuery(query);
                document.querySelector("[name='solution-query-result']").value = result;
            }
            displayAllTablesData();
        });
    });
}

window.addEventListener("DOMContentLoaded", async () => {
    await initSqlJsDatabase();
    bindSqlHandlers();
});
