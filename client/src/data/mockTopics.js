export const topics = [
    {
        id: "sql-basics",
        title: "SQL Basics",
        content: `
      <h1>SQL Basics</h1>
      <p>SQL (Structured Query Language) is used to interact with relational databases. Within this topic, you will learn the fundamental concepts and principles of working with relational databases, including:</p>
      <ul>
        <li>Basic SELECT Syntax</li>
        <li>Data Filtering</li>
        <li>Sorting and Grouping</li>
        <li>Aggregate Functions</li>
        <li>Joins (JOINs)</li>
        <li>Subqueries and Nested Queries</li>
        <li>Set Operators (Combining Results)</li>
        <li>Aggregate and Scalar Functions</li>
        <li>String, Numeric, and Date/Time Functions</li>
        <li>Window (Analytical) Functions</li>
        <li>Common Table Expressions (CTE)</li>
        <li>Working with Temporary Tables and Views</li>
        <li>Indexes and Optimization</li>
      </ul>
    `,
        details: [
            {
                id: "sql-basics-select",
                title: "Basic SELECT Syntax",
                content: `
          <h1>Basic SELECT Syntax</h1>
          <p>The <code>SELECT</code> statement is used to retrieve data from a database. The basic syntax is as follows:</p>
          <pre><code>SELECT column1, column2, ...
          FROM table_name
          WHERE condition;</code></pre>
          <p><strong>Explanation:</strong></p>
          <ul>
            <li><code>SELECT</code>: Specifies the columns or expressions to be returned in the result set.</li>
            <li><code>FROM</code>: Identifies the table or tables from which to retrieve the data.</li>
            <li><code>WHERE</code>: Filters the rows based on a specified condition.</li>
          </ul>
          <p><strong>Example:</strong></p>
          <pre><code>SELECT first_name, last_name
          FROM employees
          WHERE department = 'Sales';</code></pre>
          <p>This query retrieves the first and last names of employees from the 'Sales' department.</p>
          <h2>Syntax for a Simple Deletion Query</h2>
          <p>The syntax for a simple deletion query is as follows:</p>
          <pre><code>DELETE FROM table_name
          WHERE condition;</code></pre>
          <p><strong>Example:</strong></p>
          <pre><code>DELETE FROM employees
          WHERE department = 'Sales';</code></pre>
          <p>This query deletes all employees from the 'Sales' department.</p>
          <h2>Additional Aspects to Review</h2>
          <ul>
            <li>History and Development: SQL was developed in the 1970s by IBM researchers.</li>
            <li>Relation to the Relational Data Model: SQL is based on the relational model proposed by E.F. Codd.</li>
            <li>Relation to Other Query Languages: SQL differs from hierarchical and network models.</li>
          </ul>
        `,
            },
            {
                id: "sql-basics-filtering",
                title: "Data Filtering",
                content: `
          <h1>Data Filtering</h1>
          <p>Data filtering in SQL is performed using the <code>WHERE</code> clause to specify conditions for selecting rows. Example:</p>
          <pre><code>SELECT * FROM employees
          WHERE salary > 50000;</code></pre>
          <p>This retrieves all employees with a salary greater than 50,000.</p>
        `,
            },
        ],
    },
    {
        id: "query-optimization",
        title: "Query Optimization and Performance",
        content: `
      <h1>Query Optimization and Performance</h1>
      <p>Learn how to optimize SQL queries for better performance, including:</p>
      <ul>
        <li>Query Execution and Execution Plans</li>
        <li>Parallel Execution and Conflict Management</li>
        <li>Stored Procedures, Functions, and Triggers</li>
        <li>Cursors and Error Handling</li>
        <li>Dynamic SQL</li>
      </ul>
    `,
        details: [
            {
                id: "query-optimization-plans",
                title: "Query Execution and Execution Plans",
                content: `
          <h1>Query Execution and Execution Plans</h1>
          <p>Execution plans show how the database engine executes a query. Example:</p>
          <pre><code>EXPLAIN SELECT * FROM employees WHERE salary > 50000;</code></pre>
          <p>This command provides insight into the query's execution strategy.</p>
        `,
            },
        ],
    },
    {
        id: "transaction-fundamentals",
        title: "Transaction Fundamentals and ACID Properties",
        content: `
      <h1>Transaction Fundamentals and ACID Properties</h1>
      <p>Understand database transactions and the ACID properties (Atomicity, Consistency, Isolation, Durability):</p>
      <ul>
        <li>Transaction Control Language (TCL)</li>
        <li>Basic SELECT Syntax</li>
      </ul>
    `,
        details: [
            {
                id: "transaction-tcl",
                title: "Transaction Control Language (TCL)",
                content: `
          <h1>Transaction Control Language (TCL)</h1>
          <p>TCL commands manage transactions in SQL, including:</p>
          <pre><code>COMMIT;</code></pre>
          <pre><code>ROLLBACK;</code></pre>
          <p>Example: <code>BEGIN TRANSACTION; UPDATE accounts SET balance = balance - 100 WHERE id = 1; COMMIT;</code></p>
        `,
            },
        ],
    },
];