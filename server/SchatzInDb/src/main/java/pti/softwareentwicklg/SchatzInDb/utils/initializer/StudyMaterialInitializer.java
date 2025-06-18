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
    
                        Inside a table, a column often contains many duplicate values; and sometimes you only want to list the different (distinct) values.
    
                        Syntax:
                        ```sql
                        SELECT DISTINCT column1, column2, ...
                        FROM table_name;
                        ```
    
                        SELECT Example Without DISTINCT
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
                    
                        However, numeric fields should not be enclosed in quotes:
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
                    
                        The following operators can be used in the WHERE clause:
                        Operator    Description                 Example
                        =           Equal
                        >           Greater than
                        <           Less than
                        >=          Greater than or equal
                        <=          Less than or equal
                        <>          Not equal. Note: In some versions of SQL this operator may be written as !=
                        BETWEEN     Between a certain range
                        LIKE        Search for a pattern
                        IN          To specify multiple possible values for a column
                        """,
                        SqlKategorie.WHERE,
                        null
                ),
                createMaterial(
                        "SQL ORDER BY Keyword",
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
                        The ORDER BY keyword sorts the records in ascending order by default. To sort the records in descending order, use the DESC keyword.
                    
                        Example:
                        Sort the products from highest to lowest price:
                        ```sql
                        SELECT * FROM Products
                        ORDER BY Price DESC;
                        ```
                    
                        Order Alphabetically:
                        For string values, the ORDER BY keyword will order alphabetically.
                    
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
                        The following SQL statement selects all customers from the "Customers" table, sorted by the "Country" and the "CustomerName" column. This means that it orders by Country, but if some rows have the same Country, it orders them by CustomerName.
                    
                        Example:
                        ```sql
                        SELECT * FROM Customers
                        ORDER BY Country, CustomerName;
                        ```
                    
                        Using Both ASC and DESC:
                        The following SQL statement selects all customers from the "Customers" table, sorted ascending by the "Country" and descending by the "CustomerName" column:
                    
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
                        "SQL AND Operator",
                        """
                        The WHERE clause can contain one or many AND operators.
                    
                        The AND operator is used to filter records based on more than one condition, like if you want to return all customers from Spain that starts with the letter 'G':
                    
                        Example:
                        Select all customers from Spain that starts with the letter 'G':
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
                        The AND operator displays a record if all the conditions are TRUE.
                    
                        The OR operator displays a record if any of the conditions are TRUE.
                    
                        All Conditions Must Be True:
                        The following SQL statement selects all fields from Customers where Country is "Brazil" AND City is "Rio de Janeiro" AND CustomerID is higher than 50:
                    
                        Example:
                        ```sql
                        SELECT * FROM Customers
                        WHERE Country = 'Brazil'
                        AND City = 'Rio de Janeiro'
                        AND CustomerID > 50;
                        ```
                    
                        Combining AND and OR:
                        You can combine the AND and OR operators.
                    
                        The following SQL statement selects all customers from Spain that starts with a "G" or an "R".
                    
                        Make sure you use parenthesis to get the correct result.
                    
                        Example:
                        Select all Spanish customers that starts with either "G" or "R":
                    
                        ```sql
                        SELECT * FROM Customers
                        WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
                        ```
                    
                        Without parenthesis, the select statement will return all customers from Spain that starts with a "G", plus all customers that starts with an "R", regardless of the country value:
                    
                        Example:
                        Select all customers that either:
                        - are from Spain and starts with either "G", or
                        - starts with the letter "R":
                    
                        ```sql
                        SELECT * FROM Customers
                        WHERE Country = 'Spain' AND CustomerName LIKE 'G%' OR CustomerName LIKE 'R%';
                        ```
                        """,
                        SqlKategorie.AND,
                        null
                ),
                createMaterial(
                        "SQL OR Operator",
                        """
                        The WHERE clause can contain one or more OR operators.
                    
                        The OR operator is used to filter records based on more than one condition, like if you want to return all customers from Germany but also those from Spain:
                    
                        Example:
                        Select all customers from Germany or Spain:
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
                        The OR operator displays a record if any of the conditions are TRUE.
                    
                        The AND operator displays a record if all the conditions are TRUE.
                    
                        At Least One Condition Must Be True:
                        The following SQL statement selects all fields from Customers where either City is "Berlin", CustomerName starts with the letter "G" or Country is "Norway":
                    
                        Example:
                        ```sql
                        SELECT * FROM Customers
                        WHERE City = 'Berlin' OR CustomerName LIKE 'G%' OR Country = 'Norway';
                        ```
                    
                        Combining AND and OR:
                        You can combine the AND and OR operators.
                    
                        The following SQL statement selects all customers from Spain that starts with a "G" or an "R".
                    
                        Make sure you use parenthesis to get the correct result.
                    
                        Example:
                        Select all Spanish customers that starts with either "G" or "R":
                    
                        ```sql
                        SELECT * FROM Customers
                        WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
                        ```
                    
                        Without parenthesis, the select statement will return all customers from Spain that starts with a "G", plus all customers that starts with an "R", regardless of the country value:
                    
                        Example:
                        Select all customers that either:
                        - are from Spain and starts with either "G", or
                        - starts with the letter "R":
                    
                        ```sql
                        SELECT * FROM Customers
                        WHERE Country = 'Spain' AND CustomerName LIKE 'G%' OR CustomerName LIKE 'R%';
                        ```
                        """,
                        SqlKategorie.OR,
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