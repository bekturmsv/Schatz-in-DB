// const mockTasks = {
//     easy: {
//         regularTasks: [
//             {
//                 id: 1,
//                 name: "SELECT Basics",
//                 topic: "SELECT",
//                 points: 5,
//                 description: "Write a query to select all columns from the 'users' table.",
//                 hint: "Try using: SELECT * FROM users;",
//                 correctAnswer: "SELECT * FROM users;",
//             },
//             {
//                 id: 2,
//                 name: "Simple JOIN",
//                 topic: "JOIN",
//                 points: 5,
//                 description: "Join 'users' and 'orders' tables to get user orders.",
//                 hint: "Try using: SELECT * FROM users JOIN orders ON users.id = orders.user_id;",
//                 correctAnswer: "SELECT * FROM users JOIN orders ON users.id = orders.user_id;",
//             },
//         ],
//         finalTest: {
//             tasks: [
//                 {
//                     id: 1,
//                     name: "Test Task 1",
//                     topic: "SELECT",
//                     points: 5,
//                     description: "Select names from 'users' where age > 30.",
//                     hint: "Try using: SELECT name FROM users WHERE age > 30;",
//                     correctAnswer: "SELECT name FROM users WHERE age > 30;",
//                 },
//                 {
//                     id: 2,
//                     name: "Test Task 2",
//                     topic: "SELECT",
//                     points: 5,
//                     description: "Select distinct cities from 'users'.",
//                     hint: "Try using: SELECT DISTINCT city FROM users;",
//                     correctAnswer: "SELECT DISTINCT city FROM users;",
//                 },
//                 {
//                     id: 3,
//                     name: "Test Task 3",
//                     topic: "JOIN",
//                     points: 5,
//                     description: "Join 'users' and 'orders' to count orders per user.",
//                     hint: "Try using: SELECT users.name, COUNT(orders.id) FROM users JOIN orders ON users.id = orders.user_id GROUP BY users.name;",
//                     correctAnswer: "SELECT users.name, COUNT(orders.id) FROM users JOIN orders ON users.id = orders.user_id GROUP BY users.name;",
//                 },
//                 {
//                     id: 4,
//                     name: "Test Task 4",
//                     topic: "WHERE",
//                     points: 5,
//                     description: "Select orders from 'orders' where total > 100.",
//                     hint: "Try using: SELECT * FROM orders WHERE total > 100;",
//                     correctAnswer: "SELECT * FROM orders WHERE total > 100;",
//                 },
//                 {
//                     id: 5,
//                     name: "Test Task 5",
//                     topic: "GROUP BY",
//                     points: 5,
//                     description: "Group users by city and count them.",
//                     hint: "Try using: SELECT city, COUNT(*) FROM users GROUP BY city;",
//                     correctAnswer: "SELECT city, COUNT(*) FROM users GROUP BY city;",
//                 },
//                 {
//                     id: 6,
//                     name: "Test Task 6",
//                     topic: "SELECT",
//                     points: 5,
//                     description: "Select names from 'users' ordered by name.",
//                     hint: "Try using: SELECT name FROM users ORDER BY name;",
//                     correctAnswer: "SELECT name FROM users ORDER BY name;",
//                 },
//                 {
//                     id: 7,
//                     name: "Test Task 7",
//                     topic: "JOIN",
//                     points: 5,
//                     description: "Join 'users' and 'orders' to get orders from last month.",
//                     hint: "Try using: SELECT * FROM users JOIN orders ON users.id = orders.user_id WHERE orders.date >= '2023-10-01';",
//                     correctAnswer: "SELECT * FROM users JOIN orders ON users.id = orders.user_id WHERE orders.date >= '2023-10-01';",
//                 },
//                 {
//                     id: 8,
//                     name: "Test Task 8",
//                     topic: "WHERE",
//                     points: 5,
//                     description: "Select users with names starting with 'A'.",
//                     hint: "Try using: SELECT * FROM users WHERE name LIKE 'A%';",
//                     correctAnswer: "SELECT * FROM users WHERE name LIKE 'A%';",
//                 },
//                 {
//                     id: 9,
//                     name: "Test Task 9",
//                     topic: "GROUP BY",
//                     points: 5,
//                     description: "Group orders by user and sum their totals.",
//                     hint: "Try using: SELECT user_id, SUM(total) FROM orders GROUP BY user_id;",
//                     correctAnswer: "SELECT user_id, SUM(total) FROM orders GROUP BY user_id;",
//                 },
//                 {
//                     id: 10,
//                     name: "Test Task 10",
//                     topic: "SELECT",
//                     points: 5,
//                     description: "Select the top 5 users by age.",
//                     hint: "Try using: SELECT * FROM users ORDER BY age DESC LIMIT 5;",
//                     correctAnswer: "SELECT * FROM users ORDER BY age DESC LIMIT 5;",
//                 },
//             ],
//         },
//     },
//     medium: {
//         regularTasks: [
//             {
//                 id: 1,
//                 name: "Complex JOINs",
//                 topic: "JOIN",
//                 points: 10,
//                 description: "Join three tables: 'users', 'orders', and 'products'.",
//                 hint: "Try using: SELECT * FROM users JOIN orders ON users.id = orders.user_id JOIN products ON orders.product_id = products.id;",
//                 correctAnswer: "SELECT * FROM users JOIN orders ON users.id = orders.user_id JOIN products ON orders.product_id = products.id;",
//             },
//         ],
//         finalTest: {
//             tasks: [],
//         },
//     },
//     hard: {
//         regularTasks: [
//             {
//                 id: 1,
//                 name: "Subqueries",
//                 topic: "Subqueries",
//                 points: 15,
//                 description: "Write a subquery to find users with more than 5 orders.",
//                 hint: "Try using: SELECT * FROM users WHERE id IN (SELECT user_id FROM orders GROUP BY user_id HAVING COUNT(*) > 5);",
//                 correctAnswer: "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders GROUP BY user_id HAVING COUNT(*) > 5);",
//             },
//         ],
//         finalTest: {
//             tasks: [],
//         },
//     },
// };
//
// export default mockTasks;


const mockTasks = {
    easy: {
        regularTasks: [
            {
                id: 1,
                name: "SELECT Basics",
                topic: "SELECT",
                points: 5,
                description: "Write a query to select all columns from the 'users' table.",
                hint: "Try using: SELECT * FROM users;",
                testTable: JSON.stringify({
                    users: {
                        columns: ["id", "name", "age"],
                        rows: [
                            [1, "Alice", 25],
                            [2, "Bob", 30],
                            [3, "Charlie", 35],
                        ],
                    },
                }),
                correctAnswer: JSON.stringify([
                    { id: 1, name: "Alice", age: 25 },
                    { id: 2, name: "Bob", age: 30 },
                    { id: 3, name: "Charlie", age: 35 },
                ]),
            },
            {
                id: 2,
                name: "Simple JOIN",
                topic: "JOIN",
                points: 5,
                description: "Join 'users' and 'orders' tables to get user orders.",
                hint: "Try using: SELECT * FROM users JOIN orders ON users.id = orders.user_id;",
                testTable: JSON.stringify({
                    users: {
                        columns: ["id", "name"],
                        rows: [
                            [1, "Alice"],
                            [2, "Bob"],
                        ],
                    },
                    orders: {
                        columns: ["order_id", "user_id", "total"],
                        rows: [
                            [101, 1, 200],
                            [102, 2, 150],
                        ],
                    },
                }),
                correctAnswer: JSON.stringify([
                    { id: 1, name: "Alice", order_id: 101, user_id: 1, total: 200 },
                    { id: 2, name: "Bob", order_id: 102, user_id: 2, total: 150 },
                ]),
            },
        ],
        finalTest: {
            tasks: [
                {
                    id: 1,
                    name: "Test Task 1",
                    topic: "SELECT",
                    points: 5,
                    description: "Select names from 'users' where age > 30.",
                    hint: "Try using: SELECT name FROM users WHERE age > 30;",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name", "age"],
                            rows: [
                                [1, "Alice", 25],
                                [2, "Bob", 30],
                                [3, "Charlie", 35],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([{ name: "Charlie" }]),
                },
                {
                    id: 2,
                    name: "Test Task 2",
                    topic: "SELECT",
                    points: 5,
                    description: "Select distinct cities from 'users'.",
                    hint: "Try using: SELECT DISTINCT city FROM users;",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name", "city"],
                            rows: [
                                [1, "Alice", "New York"],
                                [2, "Bob", "New York"],
                                [3, "Charlie", "London"],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { city: "New York" },
                        { city: "London" },
                    ]),
                },
                {
                    id: 3,
                    name: "Test Task 3",
                    topic: "JOIN",
                    points: 5,
                    description: "Join 'users' and 'orders' to count orders per user.",
                    hint: "Try using: SELECT users.name, COUNT(orders.id) FROM users JOIN orders ON users.id = orders.user_id GROUP BY users.name;",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name"],
                            rows: [
                                [1, "Alice"],
                                [2, "Bob"],
                            ],
                        },
                        orders: {
                            columns: ["id", "user_id"],
                            rows: [
                                [101, 1],
                                [102, 1],
                                [103, 2],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { name: "Alice", "COUNT(orders.id)": 2 },
                        { name: "Bob", "COUNT(orders.id)": 1 },
                    ]),
                },
                {
                    id: 4,
                    name: "Test Task 4",
                    topic: "WHERE",
                    points: 5,
                    description: "Select orders from 'orders' where total > 100.",
                    hint: "Try using: SELECT * FROM orders WHERE total > 100;",
                    testTable: JSON.stringify({
                        orders: {
                            columns: ["order_id", "total"],
                            rows: [
                                [101, 50],
                                [102, 150],
                                [103, 200],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { order_id: 102, total: 150 },
                        { order_id: 103, total: 200 },
                    ]),
                },
                {
                    id: 5,
                    name: "Test Task 5",
                    topic: "GROUP BY",
                    points: 5,
                    description: "Group users by city and count them.",
                    hint: "Try using: SELECT city, COUNT(*) FROM users GROUP BY city;",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name", "city"],
                            rows: [
                                [1, "Alice", "New York"],
                                [2, "Bob", "New York"],
                                [3, "Charlie", "London"],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { city: "New York", "COUNT(*)": 2 },
                        { city: "London", "COUNT(*)": 1 },
                    ]),
                },
                {
                    id: 6,
                    name: "Test Task 6",
                    topic: "SELECT",
                    points: 5,
                    description: "Select names from 'users' ordered by name.",
                    hint: "Try using: SELECT name FROM users ORDER BY name;",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name", "age"],
                            rows: [
                                [1, "Charlie", 35],
                                [2, "Alice", 25],
                                [3, "Bob", 30],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { name: "Alice" },
                        { name: "Bob" },
                        { name: "Charlie" },
                    ]),
                },
                {
                    id: 7,
                    name: "Test Task 7",
                    topic: "JOIN",
                    points: 5,
                    description: "Join 'users' and 'orders' to get orders from last month.",
                    hint: "Try using: SELECT * FROM users JOIN orders ON users.id = orders.user_id WHERE orders.date >= '2023-10-01';",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name"],
                            rows: [
                                [1, "Alice"],
                                [2, "Bob"],
                            ],
                        },
                        orders: {
                            columns: ["order_id", "user_id", "date"],
                            rows: [
                                [101, 1, "2023-09-01"],
                                [102, 1, "2023-10-15"],
                                [103, 2, "2023-11-01"],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { id: 1, name: "Alice", order_id: 102, user_id: 1, date: "2023-10-15" },
                        { id: 2, name: "Bob", order_id: 103, user_id: 2, date: "2023-11-01" },
                    ]),
                },
                {
                    id: 8,
                    name: "Test Task 8",
                    topic: "WHERE",
                    points: 5,
                    description: "Select users with names starting with 'A'.",
                    hint: "Try using: SELECT * FROM users WHERE name LIKE 'A%';",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name", "age"],
                            rows: [
                                [1, "Alice", 25],
                                [2, "Bob", 30],
                                [3, "Charlie", 35],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([{ id: 1, name: "Alice", age: 25 }]),
                },
                {
                    id: 9,
                    name: "Test Task 9",
                    topic: "GROUP BY",
                    points: 5,
                    description: "Group orders by user and sum their totals.",
                    hint: "Try using: SELECT user_id, SUM(total) FROM orders GROUP BY user_id;",
                    testTable: JSON.stringify({
                        orders: {
                            columns: ["order_id", "user_id", "total"],
                            rows: [
                                [101, 1, 200],
                                [102, 1, 300],
                                [103, 2, 150],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { user_id: 1, "SUM(total)": 500 },
                        { user_id: 2, "SUM(total)": 150 },
                    ]),
                },
                {
                    id: 10,
                    name: "Test Task 10",
                    topic: "SELECT",
                    points: 5,
                    description: "Select the top 5 users by age.",
                    hint: "Try using: SELECT * FROM users ORDER BY age DESC LIMIT 5;",
                    testTable: JSON.stringify({
                        users: {
                            columns: ["id", "name", "age"],
                            rows: [
                                [1, "Alice", 25],
                                [2, "Bob", 30],
                                [3, "Charlie", 35],
                                [4, "David", 40],
                                [5, "Eve", 45],
                                [6, "Frank", 20],
                            ],
                        },
                    }),
                    correctAnswer: JSON.stringify([
                        { id: 5, name: "Eve", age: 45 },
                        { id: 4, name: "David", age: 40 },
                        { id: 3, name: "Charlie", age: 35 },
                        { id: 2, name: "Bob", age: 30 },
                        { id: 1, name: "Alice", age: 25 },
                    ]),
                },
            ],
        },
    },
    medium: {
        regularTasks: [
            {
                id: 1,
                name: "Complex JOINs",
                topic: "JOIN",
                points: 10,
                description: "Join three tables: 'users', 'orders', and 'products'.",
                hint: "Try using: SELECT * FROM users JOIN orders ON users.id = orders.user_id JOIN products ON orders.product_id = products.id;",
                testTable: JSON.stringify({
                    users: {
                        columns: ["id", "name"],
                        rows: [
                            [1, "Alice"],
                            [2, "Bob"],
                        ],
                    },
                    orders: {
                        columns: ["order_id", "user_id", "product_id"],
                        rows: [
                            [101, 1, 1],
                            [102, 2, 2],
                        ],
                    },
                    products: {
                        columns: ["id", "product_name"],
                        rows: [
                            [1, "Laptop"],
                            [2, "Phone"],
                        ],
                    },
                }),
                correctAnswer: JSON.stringify([
                    {
                        id: 1,
                        name: "Alice",
                        order_id: 101,
                        user_id: 1,
                        product_id: 1,
                        product_name: "Laptop",
                    },
                    {
                        id: 2,
                        name: "Bob",
                        order_id: 102,
                        user_id: 2,
                        product_id: 2,
                        product_name: "Phone",
                    },
                ]),
            },
        ],
        finalTest: {
            tasks: [],
        },
    },
    hard: {
        regularTasks: [
            {
                id: 1,
                name: "Subqueries",
                topic: "Subqueries",
                points: 15,
                description: "Write a subquery to find users with more than 5 orders.",
                hint: "Try using: SELECT * FROM users WHERE id IN (SELECT user_id FROM orders GROUP BY user_id HAVING COUNT(*) > 5);",
                testTable: JSON.stringify({
                    users: {
                        columns: ["id", "name"],
                        rows: [
                            [1, "Alice"],
                            [2, "Bob"],
                            [3, "Charlie"],
                        ],
                    },
                    orders: {
                        columns: ["order_id", "user_id"],
                        rows: [
                            [101, 1],
                            [102, 1],
                            [103, 1],
                            [104, 1],
                            [105, 1],
                            [106, 1], // Alice: 6 orders
                            [107, 2],
                            [108, 2], // Bob: 2 orders
                        ],
                    },
                }),
                correctAnswer: JSON.stringify([{ id: 1, name: "Alice" }]),
            },
        ],
        finalTest: {
            tasks: [],
        },
    },
};

export default mockTasks;