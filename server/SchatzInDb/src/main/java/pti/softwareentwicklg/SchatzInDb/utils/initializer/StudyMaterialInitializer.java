package pti.softwareentwicklg.SchatzInDb.utils.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.task.StudyMaterial;
import pti.softwareentwicklg.SchatzInDb.repository.task.StudyMaterialRepository;

import java.util.List;

@Component
public class StudyMaterialInitializer {

    private final StudyMaterialRepository studyMaterialRepository;

    public StudyMaterialInitializer(StudyMaterialRepository studyMaterialRepository) {
        this.studyMaterialRepository = studyMaterialRepository;
    }

    @PostConstruct
    public void initStudyMaterials() {
        List<StudyMaterial> materials = List.of(
                createMaterial(
                        "The SQL SELECT Statement",
                        """
                                    The SELECT statement is used to select data from a database.
                            
                                    Example:
                                    ```sql
                                    SELECT CustomerName, City FROM Customers;
                                    ```
                            
                                    Syntax:
                                    ```sql
                                    SELECT column1, column2, ...
                                    FROM table_name;
                                    ```
                                    Here, column1, column2, ... are the field names of the table you want to select data from.
                                    The table_name represents the name of the table you want to select data from.
                                  """,
                        SqlKategorie.SELECT,
                        null
                ),
                createMaterial(
                        "The SQL SELECT DISTINCT Statement",
                        """
                            The SELECT DISTINCT statement is used to return only distinct (different) values.
            
                            Example:
                            Select all the different countries from the "Customers" table:
                            ```sql
                            SELECT DISTINCT Country FROM Customers;
                            ```
            
                            Inside a table, a column often contains many duplicate values;
                            and sometimes you only want to list the different (distinct) values.
            
                            Syntax:
                            ```sql
                            SELECT DISTINCT column1, column2, ...
                            FROM table_name;
                            ```
            
                            SELECT Example Without DISTINCT:
                            If you omit the DISTINCT keyword, the SQL statement returns the "Country" value from all the records of the "Customers" table:
            
                            Example:
                            ```sql
                            SELECT Country FROM Customers;
                            ```
                        """,
                        SqlKategorie.DISTINCT,
                        null
                ),
                createMaterial(
                        "The SQL WHERE Clause",
                        """
                            The WHERE clause is used to filter records.
            
                            It is used to extract only those records that fulfill a specified condition.
            
                            Example:
                            Select all customers from Mexico:
                            ```sql
                            SELECT * FROM Customers
                            WHERE Country='Mexico';
                            ```
            
                            Syntax:
                            ```sql
                            SELECT column1, column2, ...
                            FROM table_name
                            WHERE condition;
                            ```
            
                            Text Fields vs. Numeric Fields:
                            SQL requires single quotes around text values (most database systems will also allow double quotes).
            
                            However, numeric fields should not be enclosed in quotes.
            
                            Example:
                            ```sql
                            SELECT * FROM Customers
                            WHERE CustomerID=1;
                            ```
            
                            Operators in The WHERE Clause:
                            You can use other operators than the = operator to filter the search.
            
                            Example:
                            Select all customers with a CustomerID greater than 80:
                            ```sql
                            SELECT * FROM Customers
                            WHERE CustomerID > 80;
                            ```
                        """,
                        SqlKategorie.WHERE,
                        null
                ),
                createMaterial(
                        "The SQL ORDER BY",
                        """
                            The ORDER BY keyword is used to sort the result-set in ascending or descending order.
                    
                            Example:
                            Sort the products by price:
                            ```sql
                            SELECT * FROM Products
                            ORDER BY Price;
                            ```
                    
                            Syntax:
                            ```sql
                            SELECT column1, column2, ...
                            FROM table_name
                            ORDER BY column1, column2, ... ASC|DESC;
                            ```
                    
                            DESC:
                            The ORDER BY keyword sorts the records in ascending order by default.
                            To sort the records in descending order, use the DESC keyword.
                    
                            Example:
                            Sort the products from highest to lowest price:
                            ```sql
                            SELECT * FROM Products
                            ORDER BY Price DESC;
                            ```
                    
                            Order Alphabetically:
                            For string values the ORDER BY keyword will order alphabetically.
                    
                            Example:
                            Sort the products alphabetically by ProductName:
                            ```sql
                            SELECT * FROM Products
                            ORDER BY ProductName;
                            ```
                    
                            Alphabetically DESC:
                            To sort the table reverse alphabetically, use the DESC keyword.
                    
                            Example:
                            Sort the products by ProductName in reverse order:
                            ```sql
                            SELECT * FROM Products
                            ORDER BY ProductName DESC;
                            ```
                    
                            ORDER BY Several Columns:
                            The following SQL statement selects all customers from the "Customers" table, sorted by the "Country" and the "CustomerName" column. This means that it orders by Country,
                            but if some rows have the same Country, it orders them by CustomerName:
                    
                            Example:
                            ```sql
                            SELECT * FROM Customers
                            ORDER BY Country, CustomerName;
                            ```
                    
                            Using Both ASC and DESC:
                            The following SQL statement selects all customers from the "Customers" table,
                            sorted ascending by the "Country" and descending by the "CustomerName" column:
                    
                            Example:
                            ```sql
                            SELECT * FROM Customers
                            ORDER BY Country ASC, CustomerName DESC;
                            ```
                        """,
                        SqlKategorie.ORDER_BY,
                        null
                ),
                createMaterial(
                        "The SQL AND Operator",
                        """
                            The WHERE clause can contain one or many AND operators.
                    
                            The AND operator is used to filter records based on more than one condition,
                            like if you want to return all customers from Spain that start with the letter 'G':
                    
                            Example:
                            ```sql
                            SELECT *
                            FROM Customers
                            WHERE Country = 'Spain' AND CustomerName LIKE 'G%';
                            ```
                    
                            Syntax:
                            ```sql
                            SELECT column1, column2, ...
                            FROM table_name
                            WHERE condition1 AND condition2 AND condition3 ...;
                            ```
                    
                            AND vs OR:
                            - The AND operator displays a record if **all** the conditions are TRUE.
                            - The OR operator displays a record if **any** of the conditions are TRUE.
                    
                            All Conditions Must Be True:
                            The following SQL statement selects all fields from Customers where Country is "Brazil",
                            City is "Rio de Janeiro", AND CustomerID is higher than 50:
                    
                            Example:
                            ```sql
                            SELECT * FROM Customers
                            WHERE Country = 'Brazil'
                            AND City = 'Rio de Janeiro'
                            AND CustomerID > 50;
                            ```
                    
                            Combining AND and OR:
                            You can combine the AND and OR operators.
                            Make sure you use parentheses to get the correct result.
                    
                            Example:
                            Select all Spanish customers that start with either "G" or "R":
                            ```sql
                            SELECT * FROM Customers
                            WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
                            ```
                    
                            Without parentheses, the following query:
                            ```sql
                            SELECT * FROM Customers
                            WHERE Country = 'Spain' AND CustomerName LIKE 'G%' OR CustomerName LIKE 'R%';
                            ```
                            will return all customers from Spain that start with "G", **plus** all customers that start with "R",
                            regardless of their country.
                        """,
                        SqlKategorie.AND,
                        null
                ),
                createMaterial(
                        "The SQL OR Operator",
                        """
                            The WHERE clause can contain one or more OR operators.
                    
                            The OR operator is used to filter records based on more than one condition.
                            For example, to return all customers from Germany or from Spain:
                    
                            Example:
                            ```sql
                            SELECT *
                            FROM Customers
                            WHERE Country = 'Germany' OR Country = 'Spain';
                            ```
                    
                            Syntax:
                            ```sql
                            SELECT column1, column2, ...
                            FROM table_name
                            WHERE condition1 OR condition2 OR condition3 ...;
                            ```
                    
                            OR vs AND:
                            - The OR operator displays a record if **any** of the conditions are TRUE.
                            - The AND operator displays a record if **all** of the conditions are TRUE.
                    
                            At Least One Condition Must Be True:
                            The following SQL statement selects all fields from Customers where
                            either City is "Berlin", CustomerName starts with "G", or Country is "Norway":
                    
                            Example:
                            ```sql
                            SELECT * FROM Customers
                            WHERE City = 'Berlin' OR CustomerName LIKE 'G%' OR Country = 'Norway';
                            ```
                    
                            Combining AND and OR:
                            You can combine the AND and OR operators. Use parentheses to control precedence.
                    
                            Example:
                            Select all Spanish customers that start with either "G" or "R":
                            ```sql
                            SELECT * FROM Customers
                            WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
                            ```
                    
                            Without parentheses:
                            ```sql
                            SELECT * FROM Customers
                            WHERE Country = 'Spain' AND CustomerName LIKE 'G%' OR CustomerName LIKE 'R%';
                            ```
                            This will return:
                            - all customers from Spain whose names start with "G", **plus**
                            - all customers whose names start with "R", regardless of their country.
                        """,
                        SqlKategorie.OR,
                        null
                ),
                createMaterial(
                        "The SQL NOT Operator",
                        """
                            The NOT operator is used in combination with other operators to give the opposite (negative) result.
                    
                            For example, to return all customers that are not from Spain:
                    
                            Example:
                            ```sql
                            SELECT * FROM Customers
                            WHERE NOT Country = 'Spain';
                            ```
                    
                            Syntax:
                            ```sql
                            SELECT column1, column2, ...
                            FROM table_name
                            WHERE NOT condition;
                            ```
                    
                            The NOT operator can be used with various comparison and logical operators:
                    
                            NOT LIKE:
                            Select customers whose names do not start with the letter 'A':
                            ```sql
                            SELECT * FROM Customers
                            WHERE CustomerName NOT LIKE 'A%';
                            ```
                    
                            NOT BETWEEN :
                            Select customers with a CustomerID not between 10 and 60:
                            ```sql
                            SELECT * FROM Customers
                            WHERE CustomerID NOT BETWEEN 10 AND 60;
                            ```
                    
                            NOT IN:
                            Select customers who are not from Paris or London:
                            ```sql
                            SELECT * FROM Customers
                            WHERE City NOT IN ('Paris', 'London');
                            ```
                    
                            NOT Greater Than:
                            Select customers with a CustomerID not greater than 50:
                            ```sql
                            SELECT * FROM Customers
                            WHERE NOT CustomerID > 50;
                            ```
                    
                            NOT Less Than:
                            Select customers with a CustomerID not less than 50:
                            ```sql
                            SELECT * FROM Customers
                            WHERE NOT CustomerID < 50;
                            ```
                        """,
                        SqlKategorie.NOT,
                        null
                ),
                createMaterial(
                        "The SQL SUM() Function",
                        """
                            The SUM() function returns the total sum of a numeric column.
                    
                            Example:
                            Return the sum of all Quantity fields in the OrderDetails table:
                            ```sql
                            SELECT SUM(Quantity)
                            FROM OrderDetails;
                            ```
                    
                            Syntax:
                            ```sql
                            SELECT SUM(column_name)
                            FROM table_name
                            WHERE condition;
                            ```
                    
                            Add a WHERE Clause:
                            You can add a WHERE clause to specify conditions.
                    
                            Example:
                            Return the sum of the Quantity field for the product with ProductID 11:
                            ```sql
                            SELECT SUM(Quantity)
                            FROM OrderDetails
                            WHERE ProductId = 11;
                            ```
                    
                            Use an Alias:
                            Give the summarized column a name by using the AS keyword.
                    
                            Example:
                            Name the column "total":
                            ```sql
                            SELECT SUM(Quantity) AS total
                            FROM OrderDetails;
                            ```
                    
                            Use SUM() with GROUP BY:
                            Use the SUM() function and the GROUP BY clause to return the Quantity for each OrderID in the OrderDetails table.
                    
                            Example:
                            ```sql
                            SELECT OrderID, SUM(Quantity) AS [Total Quantity]
                            FROM OrderDetails
                            GROUP BY OrderID;
                            ```
                    
                            SUM() With an Expression:
                            The parameter inside the SUM() function can also be an expression.
                    
                            Example:
                            If each product costs 10 dollars:
                            ```sql
                            SELECT SUM(Quantity * 10)
                            FROM OrderDetails;
                            ```
                    
                            Or use real prices with JOIN:
                            ```sql
                            SELECT SUM(Price * Quantity)
                            FROM OrderDetails
                            LEFT JOIN Products ON OrderDetails.ProductID = Products.ProductID;
                            ```
                        """,
                        SqlKategorie.SUM,
                        null
                ),
                createMaterial(
                        "The SQL AVG() Function",
                        """
                            The AVG() function returns the average value of a numeric column.
                    
                            Example:
                            Find the average price of all products:
                            ```sql
                            SELECT AVG(Price)
                            FROM Products;
                            ```
                    
                            Note: NULL values are ignored.
                    
                            Syntax:
                            ```sql
                            SELECT AVG(column_name)
                            FROM table_name
                            WHERE condition;
                            ```
                    
                            Add a WHERE Clause:
                            You can add a WHERE clause to specify conditions.
                    
                            Example:
                            Return the average price of products in category 1:
                            ```sql
                            SELECT AVG(Price)
                            FROM Products
                            WHERE CategoryID = 1;
                            ```
                    
                            Use an Alias:
                            Give the AVG column a name by using the AS keyword.
                    
                            Example:
                            Name the column "average price":
                            ```sql
                            SELECT AVG(Price) AS [average price]
                            FROM Products;
                            ```
                    
                            Higher Than Average:
                            To list all records with a higher price than average, use AVG() in a subquery.
                    
                            Example:
                            ```sql
                            SELECT * FROM Products
                            WHERE price > (SELECT AVG(price) FROM Products);
                            ```
                    
                            Use AVG() with GROUP BY:
                            Return the average price for each category in the Products table.
                    
                            Example:
                            ```sql
                            SELECT AVG(Price) AS AveragePrice, CategoryID
                            FROM Products
                            GROUP BY CategoryID;
                            ```
                        """,
                        SqlKategorie.AVG,
                        null
                ),
                createMaterial(
                        "The SQL JOIN Clause",
                        """
                            The SQL JOIN clause is used to combine rows from two or more tables, based on a related column between them.
                    
                            Example:
                            Join the Orders and Customers tables by CustomerID:
                            ```sql
                            SELECT Orders.OrderID, Customers.CustomerName
                            FROM Orders
                            JOIN Customers ON Orders.CustomerID = Customers.CustomerID;
                            ```
                    
                            Syntax:
                            ```sql
                            SELECT column_names
                            FROM table1
                            JOIN table2
                            ON table1.column_name = table2.column_name;
                            ```
                    
                            Use a WHERE Clause:
                            You can add a WHERE clause to filter joined results.
                    
                            Example:
                            Return all orders made by customers from Germany:
                            ```sql
                            SELECT Orders.OrderID, Customers.CustomerName, Customers.Country
                            FROM Orders
                            JOIN Customers ON Orders.CustomerID = Customers.CustomerID
                            WHERE Customers.Country = 'Germany';
                            ```
                    
                            Use an Alias:
                            You can simplify table names using aliases.
                    
                            Example:
                            ```sql
                            SELECT o.OrderID, c.CustomerName
                            FROM Orders AS o
                            JOIN Customers AS c ON o.CustomerID = c.CustomerID;
                            ```
                    
                            JOIN Types:
                            SQL supports different types of joins:
                            - INNER JOIN (returns only matching records)
                            - LEFT JOIN (returns all records from the left table and matched records from the right table)
                            - RIGHT JOIN
                            - FULL OUTER JOIN
                    
                            Example of LEFT JOIN:
                            List all customers and their orders (if any):
                            ```sql
                            SELECT Customers.CustomerName, Orders.OrderID
                            FROM Customers
                            LEFT JOIN Orders ON Customers.CustomerID = Orders.CustomerID;
                            ```
                    
                            Example of FULL JOIN:
                            Return all customers and all orders, even if there is no match:
                            ```sql
                            SELECT Customers.CustomerName, Orders.OrderID
                            FROM Customers
                            FULL JOIN Orders ON Customers.CustomerID = Orders.CustomerID;
                            ```
                        """,
                        SqlKategorie.JOIN,
                        null
                ),
                createMaterial(
                        "The SQL GROUP BY Statement",
                        """
                            The GROUP BY statement groups rows that have the same values into summary rows, 
                            like "find the number of customers in each country".
                    
                            The GROUP BY statement is often used with aggregate functions 
                            (COUNT(), MAX(), MIN(), SUM(), AVG()) to group the result-set by one or more columns.
                    
                            Syntax:
                            ```sql
                            SELECT column_name(s)
                            FROM table_name
                            WHERE condition
                            GROUP BY column_name(s)
                            ORDER BY column_name(s);
                            ```
                    
                            Examples:
                    
                            Count the number of customers in each country:
                            ```sql
                            SELECT COUNT(CustomerID), Country
                            FROM Customers
                            GROUP BY Country;
                            ```
                    
                            Count the number of customers in each country and sort the result from high to low:
                            ```sql
                            SELECT COUNT(CustomerID), Country
                            FROM Customers
                            GROUP BY Country
                            ORDER BY COUNT(CustomerID) DESC;
                            ```
                    
                            GROUP BY with JOIN:
                    
                            List the number of orders sent by each shipper:
                            ```sql
                            SELECT Shippers.ShipperName, COUNT(Orders.OrderID) AS NumberOfOrders
                            FROM Orders
                            LEFT JOIN Shippers ON Orders.ShipperID = Shippers.ShipperID
                            GROUP BY ShipperName;
                            ```
                        """,
                        SqlKategorie.GROUP_BY,
                        null
                ),
                createMaterial(
                        "The SQL HAVING Clause",
                        """
                            The HAVING clause was added to SQL because the WHERE keyword cannot be used with aggregate functions.
                    
                            Syntax:
                            ```sql
                            SELECT column_name(s)
                            FROM table_name
                            WHERE condition
                            GROUP BY column_name(s)
                            HAVING condition
                            ORDER BY column_name(s);
                            ```
                    
                            Examples:
                    
                            List the number of customers in each country. Only include countries with more than 5 customers:
                            ```sql
                            SELECT COUNT(CustomerID), Country
                            FROM Customers
                            GROUP BY Country
                            HAVING COUNT(CustomerID) > 5;
                            ```
                    
                            List the number of customers in each country, sorted from high to low, only if they have more than 5 customers:
                            ```sql
                            SELECT COUNT(CustomerID), Country
                            FROM Customers
                            GROUP BY Country
                            HAVING COUNT(CustomerID) > 5
                            ORDER BY COUNT(CustomerID) DESC;
                            ```
                    
                            More HAVING examples:
                    
                            List the employees that have registered more than 10 orders:
                            ```sql
                            SELECT Employees.LastName, COUNT(Orders.OrderID) AS NumberOfOrders
                            FROM Orders
                            INNER JOIN Employees ON Orders.EmployeeID = Employees.EmployeeID
                            GROUP BY LastName
                            HAVING COUNT(Orders.OrderID) > 10;
                            ```
                    
                            List if the employees "Davolio" or "Fuller" have registered more than 25 orders:
                            ```sql
                            SELECT Employees.LastName, COUNT(Orders.OrderID) AS NumberOfOrders
                            FROM Orders
                            INNER JOIN Employees ON Orders.EmployeeID = Employees.EmployeeID
                            WHERE LastName = 'Davolio' OR LastName = 'Fuller'
                            GROUP BY LastName
                            HAVING COUNT(Orders.OrderID) > 25;
                            ```
                        """,
                        SqlKategorie.GROUP_BY,
                        null
                ),
                createMaterial(
                        "SQL Aggregate Functions",
                        """
                            An aggregate function is a function that performs a calculation on a set of values and returns a single value.
                    
                            Aggregate functions are often used with the GROUP BY clause of the SELECT statement. 
                            The GROUP BY clause splits the result-set into groups of values, and the aggregate function returns a single value for each group.
                    
                            The most commonly used SQL aggregate functions are:
                    
                            MIN() - returns the smallest value within the selected column  
                            MAX() - returns the largest value within the selected column  
                            COUNT() - returns the number of rows in a set  
                            SUM() - returns the total sum of a numerical column  
                            AVG() - returns the average value of a numerical column
                    
                            Aggregate functions ignore NULL values (except for COUNT()).
                    
                            We will go through each of these aggregate functions in the next sections.
                        """,
                        SqlKategorie.AGGREGATE,
                        null
                ),
                createMaterial(
                        "The SQL Subquery",
                        """
                            A subquery is a SQL query nested inside another query.
                    
                            Subqueries can be used in SELECT, FROM, or WHERE clauses and are often used to perform intermediate calculations or filtering.
                    
                            Syntax:
                            ```sql
                            SELECT column1
                            FROM table1
                            WHERE column2 = (SELECT column2 FROM table2 WHERE condition);
                            ```
                    
                            Example:
                            Return all products with a price higher than the average:
                            ```sql
                            SELECT * FROM Products
                            WHERE Price > (SELECT AVG(Price) FROM Products);
                            ```
                    
                            Subquery in SELECT:
                            You can also return a value from a subquery directly in the result set.
                    
                            Example:
                            ```sql
                            SELECT ProductName, 
                                   (SELECT AVG(Price) FROM Products) AS AvgPrice
                            FROM Products;
                            ```
                    
                            Subquery in FROM:
                            Subqueries in the FROM clause are treated as temporary tables.
                    
                            Example:
                            ```sql
                            SELECT Category, AVG_Price
                            FROM (
                                SELECT CategoryID AS Category, AVG(Price) AS AVG_Price
                                FROM Products
                                GROUP BY CategoryID
                            ) AS CategoryAverages;
                            ```
                    
                            Notes:
                            - Subqueries must return only one value when used with operators like `=`, `<`, `>`, etc.
                            - For multiple values, use `IN`, `ANY`, or `ALL`.
                        """,
                        SqlKategorie.SUBQUERY,
                        null
                ),
                createMaterial(
                        "The SQL COUNT() Function",
                        """
                            The COUNT() function returns the number of rows that match a specified condition.
                    
                            Syntax:
                            ```sql
                            SELECT COUNT(column_name)
                            FROM table_name
                            WHERE condition;
                            ```
                    
                            COUNT(*) counts all rows, including those with NULLs.
                    
                            Example:
                            Count the number of customers:
                            ```sql
                            SELECT COUNT(*) FROM Customers;
                            ```
                    
                            Count only non-null values in a column:
                            ```sql
                            SELECT COUNT(CustomerName) FROM Customers;
                            ```
                    
                            COUNT() with WHERE:
                            Count customers from Germany:
                            ```sql
                            SELECT COUNT(*) FROM Customers
                            WHERE Country = 'Germany';
                            ```
                    
                            COUNT() with GROUP BY:
                            Count the number of customers per country:
                            ```sql
                            SELECT Country, COUNT(CustomerID)
                            FROM Customers
                            GROUP BY Country;
                            ```
                    
                            COUNT() ignores NULL values when counting specific columns.
                        """,
                        SqlKategorie.COUNT,
                        null
                )

        );

        studyMaterialRepository.saveAll(materials);
    }

    private StudyMaterial createMaterial(String title, String description, SqlKategorie sqlKategorie, Long teacherId) {
        StudyMaterial material = new StudyMaterial();
        material.setTitle(title);
        material.setDescription(description);
        material.setSqlKategorie(sqlKategorie);
        material.setTeacher(teacherId);
        return material;
    }
}