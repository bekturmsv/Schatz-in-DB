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
                                    Die SELECT-Anweisung wird verwendet, um Daten aus einer Datenbank auszuwählen.

                                    Beispiel:
                                    ```sql
                                    SELECT CustomerName, City FROM Customers;
                                    ```

                                    Syntax:
                                    ```sql
                                    SELECT column1, column2, ...
                                    FROM table_name;
                                    ```
                                    Hier sind column1, column2, ... die Feldnamen der Tabelle, aus der du Daten auswählen möchtest.
                                    table_name steht für den Namen der Tabelle, aus der du Daten abrufen möchtest.
                                  """,
                        SqlKategorie.SELECT,
                        null
                ),
                createMaterial(
                        "The SQL SELECT DISTINCT Statement",
                        """
                                    Die SELECT DISTINCT-Anweisung wird verwendet, um nur verschiedene (eindeutige) Werte zurückzugeben.

                                    Wähle alle verschiedenen Länder aus der Tabelle "Customers":
                                    ```sql
                                    SELECT DISTINCT Country FROM Customers;
                                    ```

                                    In einer Tabelle enthält eine Spalte häufig viele doppelte Werte;
                                    manchmal möchte man jedoch nur die verschiedenen (eindeutigen) Werte auflisten.

                                    Syntax:
                                    ```sql
                                    SELECT DISTINCT column1, column2, ... FROM table_name;
                                    ```

                                    Beispiel ohne DISTINCT:
                                    Wenn du das DISTINCT-Schlüsselwort weglässt, liefert die SQL-Anweisung den Wert der Spalte "Country" aus allen Datensätzen der Tabelle "Customers":

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
                                    Die WHERE-Klausel wird verwendet, um Datensätze zu filtern.

                                    Sie wird verwendet, um nur diejenigen Datensätze auszuwählen, die eine bestimmte Bedingung erfüllen.

                                    Beispiel:
                                    Wähle alle Kunden aus Mexiko:
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

                                    Textfelder vs. numerische Felder:
                                    SQL erfordert einfache Anführungszeichen um Textwerte (die meisten Datenbanksysteme erlauben auch doppelte Anführungszeichen).
                                    Numerische Felder dürfen jedoch nicht in Anführungszeichen gesetzt werden.

                                    Beispiel:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE CustomerID=1;
                                    ```

                                    Operatoren in der WHERE-Klausel:
                                    Du kannst andere Operatoren als = verwenden, um die Suche zu filtern.

                                    Beispiel:
                                    Wähle alle Kunden mit einer CustomerID größer als 80:
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
                                    Das Schlüsselwort ORDER BY wird verwendet, um das Ergebnis in aufsteigender oder absteigender Reihenfolge zu sortieren.

                                    Beispiel:
                                    Sortiere die Produkte nach Preis:
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
                                    Das ORDER BY-Schlüsselwort sortiert die Datensätze standardmäßig in aufsteigender Reihenfolge.
                                    Um in absteigender Reihenfolge zu sortieren, verwende das Schlüsselwort DESC.

                                    Beispiel:
                                    Sortiere die Produkte vom höchsten zum niedrigsten Preis:
                                    ```sql
                                    SELECT * FROM Products
                                    ORDER BY Price DESC;
                                    ```

                                    Alphabetische Sortierung:
                                    Bei Textwerten sortiert das ORDER BY-Schlüsselwort alphabetisch.

                                    Beispiel:
                                    Sortiere die Produkte alphabetisch nach ProductName:
                                    ```sql
                                    SELECT * FROM Products
                                    ORDER BY ProductName;
                                    ```

                                    Alphabetisch DESC:
                                    Um die Tabelle in umgekehrter alphabetischer Reihenfolge zu sortieren, verwende das Schlüsselwort DESC.

                                    Beispiel:
                                    Sortiere die Produkte nach ProductName in umgekehrter Reihenfolge:
                                    ```sql
                                    SELECT * FROM Products
                                    ORDER BY ProductName DESC;
                                    ```

                                    ORDER BY Mehrerer Spalten:
                                    Die folgende SQL-Anweisung wählt alle Kunden aus der Tabelle "Customers" aus, sortiert nach der Spalte "Country" und dann nach "CustomerName". Wenn mehrere Zeilen dasselbe Land haben, wird zusätzlich nach Kundennamen sortiert:

                                    ```sql
                                    SELECT * FROM Customers
                                    ORDER BY Country, CustomerName;
                                    ```

                                    Kombination von ASC und DESC:
                                    Die folgende SQL-Anweisung wählt alle Kunden aus der Tabelle "Customers" aus,
                                    sortiert aufsteigend nach "Country" und absteigend nach "CustomerName":

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
                                    Die WHERE-Klausel kann einen oder mehrere AND-Operatoren enthalten.

                                    Der AND-Operator wird verwendet, um Datensätze basierend auf mehr als einer Bedingung zu filtern,
                                    zum Beispiel alle Kunden aus Spanien, deren Name mit dem Buchstaben 'G' beginnt:

                                    Beispiel:
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

                                    AND vs. OR:
                                    - Der AND-Operator zeigt einen Datensatz an, wenn **alle** Bedingungen TRUE sind.
                                    - Der OR-Operator zeigt einen Datensatz an, wenn **eine** der Bedingungen TRUE ist.

                                    Alle Bedingungen müssen erfüllt sein:
                                    Die folgende SQL-Anweisung wählt alle Felder aus Customers aus, bei denen Country 'Brazil',
                                    City 'Rio de Janeiro' und CustomerID größer als 50 ist:

                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE Country = 'Brazil'
                                    AND City = 'Rio de Janeiro'
                                    AND CustomerID > 50;
                                    ```

                                    Kombination von AND und OR:
                                    Du kannst AND und OR kombinieren. Verwende Klammern, um die richtige Reihenfolge zu erzwingen.

                                    Beispiel:
                                    Wähle alle spanischen Kunden, deren Name entweder mit 'G' oder 'R' beginnt:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
                                    ```

                                    Ohne Klammern würde die folgende Abfrage:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE Country = 'Spain' AND CustomerName LIKE 'G%' OR CustomerName LIKE 'R%';
                                    ```
                                    alle Kunden aus Spanien, deren Name mit 'G' beginnt, **plus** alle Kunden, deren Name mit 'R' beginnt,
                                    unabhängig von ihrem Land, zurückgeben.
                                  """,
                        SqlKategorie.AND,
                        null
                ),
                createMaterial(
                        "The SQL OR Operator",
                        """
                                    Die WHERE-Klausel kann einen oder mehrere OR-Operatoren enthalten.

                                    Der OR-Operator wird verwendet, um Datensätze basierend auf mehr als einer Bedingung zu filtern.
                                    Zum Beispiel, um alle Kunden aus Deutschland oder Spanien zurückzugeben:

                                    Beispiel:
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

                                    OR vs. AND:
                                    - Der OR-Operator zeigt einen Datensatz an, wenn **eine** der Bedingungen TRUE ist.
                                    - Der AND-Operator zeigt einen Datensatz an, wenn **alle** Bedingungen TRUE sind.

                                    Mindestens eine Bedingung muss erfüllt sein:
                                    Die folgende SQL-Anweisung wählt alle Felder aus Customers aus, bei denen
                                    City 'Berlin', CustomerName beginnt mit 'G' oder Country 'Norway' ist:

                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE City = 'Berlin' OR CustomerName LIKE 'G%' OR Country = 'Norway';
                                    ```

                                    Kombination von AND und OR:
                                    Du kannst AND und OR kombinieren. Verwende Klammern, um die Reihenfolge zu steuern.

                                    Beispiel:
                                    Wähle alle spanischen Kunden, deren Name entweder mit 'G' oder 'R' beginnt:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE Country = 'Spain' AND (CustomerName LIKE 'G%' OR CustomerName LIKE 'R%');
                                    ```

                                    Ohne Klammern:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE Country = 'Spain' AND CustomerName LIKE 'G%' OR CustomerName LIKE 'R%';
                                    ```
                                    Dies würde zurückgeben:
                                    - alle Kunden aus Spanien, deren Name mit 'G' beginnt, **plus**
                                    - alle Kunden, deren Name mit 'R' beginnt, unabhängig von ihrem Land.
                                  """,
                        SqlKategorie.OR,
                        null
                ),
                createMaterial(
                        "The SQL NOT Operator",
                        """
                                    Der NOT-Operator wird in Kombination mit anderen Operatoren verwendet, um das gegenteilige (negative) Ergebnis zu liefern.

                                    Zum Beispiel, um alle Kunden zurückzugeben, die nicht aus Spanien sind:

                                    Beispiel:
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

                                    Der NOT-Operator kann mit verschiedenen Vergleichs- und logischen Operatoren verwendet werden:

                                    NOT LIKE:
                                    Wähle Kunden, deren Name nicht mit dem Buchstaben 'A' beginnt:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE CustomerName NOT LIKE 'A%';
                                    ```

                                    NOT BETWEEN:
                                    Wähle Kunden mit einer CustomerID, die nicht zwischen 10 und 60 liegt:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE CustomerID NOT BETWEEN 10 AND 60;
                                    ```

                                    NOT IN:
                                    Wähle Kunden, die nicht aus Paris oder London kommen:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE City NOT IN ('Paris', 'London');
                                    ```

                                    NOT Greater Than:
                                    Wähle Kunden mit einer CustomerID, die nicht größer als 50 ist:
                                    ```sql
                                    SELECT * FROM Customers
                                    WHERE NOT CustomerID > 50;
                                    ```

                                    NOT Less Than:
                                    Wähle Kunden mit einer CustomerID, die nicht kleiner als 50 ist:
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
                                    Die SUM()-Funktion gibt die Gesamtsumme einer numerischen Spalte zurück.

                                    Beispiel:
                                    Rückgabe der Summe aller Quantity-Felder in der OrderDetails-Tabelle:
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

                                    Hinzufügen einer WHERE-Klausel:
                                    Du kannst eine WHERE-Klausel hinzufügen, um Bedingungen anzugeben.

                                    Beispiel:
                                    Rückgabe der Summe der Quantity-Felder für das Produkt mit ProductID 11:
                                    ```sql
                                    SELECT SUM(Quantity)
                                    FROM OrderDetails
                                    WHERE ProductId = 11;
                                    ```

                                    Alias verwenden:
                                    Benenne die zusammengefasste Spalte mit dem AS-Schlüsselwort.

                                    Beispiel:
                                    Nenne die Spalte "total":
                                    ```sql
                                    SELECT SUM(Quantity) AS total
                                    FROM OrderDetails;
                                    ```

                                    SUM() mit GROUP BY:
                                    Verwende die SUM()-Funktion und die GROUP BY-Klausel, um die Quantity für jede OrderID zurückzugeben:
                                    ```sql
                                    SELECT OrderID, SUM(Quantity) AS [Total Quantity]
                                    FROM OrderDetails
                                    GROUP BY OrderID;
                                    ```

                                    SUM() mit einem Ausdruck:
                                    Der Parameter in SUM() kann auch ein Ausdruck sein.

                                    Beispiel:
                                    Wenn jedes Produkt 10 Dollar kostet:
                                    ```sql
                                    SELECT SUM(Quantity * 10)
                                    FROM OrderDetails;
                                    ```

                                    Oder verwende echte Preise mit JOIN:
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
                                    Die AVG()-Funktion gibt den Durchschnittswert einer numerischen Spalte zurück.

                                    Beispiel:
                                    Finde den Durchschnittspreis aller Produkte:
                                    ```sql
                                    SELECT AVG(Price)
                                    FROM Products;
                                    ```

                                    Hinweis: NULL-Werte werden ignoriert.

                                    Syntax:
                                    ```sql
                                    SELECT AVG(column_name)
                                    FROM table_name
                                    WHERE condition;
                                    ```

                                    Hinzufügen einer WHERE-Klausel:
                                    Du kannst eine WHERE-Klausel hinzufügen, um Bedingungen anzugeben.

                                    Beispiel:
                                    Durchschnittspreis der Produkte in Kategorie 1:
                                    ```sql
                                    SELECT AVG(Price)
                                    FROM Products
                                    WHERE CategoryID = 1;
                                    ```

                                    Alias verwenden:
                                    Benenne die AVG-Spalte mit AS.

                                    Beispiel:
                                    Nenne die Spalte "average price":
                                    ```sql
                                    SELECT AVG(Price) AS [average price]
                                    FROM Products;
                                    ```

                                    Höher als Durchschnitt:
                                    Um alle Datensätze mit einem Preis über dem Durchschnitt aufzulisten, verwende AVG() in einer Subquery:
                                    ```sql
                                    SELECT * FROM Products
                                    WHERE Price > (SELECT AVG(Price) FROM Products);
                                    ```

                                    AVG() mit GROUP BY:
                                    Durchschnittspreis für jede Kategorie:
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
                                    Die JOIN-Klausel wird verwendet, um Zeilen aus zwei oder mehr Tabellen basierend auf einer gemeinsamen Spalte zu kombinieren.

                                    Beispiel:
                                    Verbinde Orders und Customers über CustomerID:
                                    ```sql
                                    SELECT Orders.OrderID, Customers.CustomerName
                                    FROM Orders
                                    JOIN Customers ON Orders.CustomerID = Customers.CustomerID;
                                    ```

                                    Syntax:
                                    ```sql
                                    SELECT column_names
                                    FROM table1
                                    JOIN table2 ON table1.column_name = table2.column_name;
                                    ```

                                    WITH WHERE:
                                    Du kannst eine WHERE-Klausel hinzufügen, um die verbundenen Ergebnisse zu filtern.

                                    Beispiel:
                                    Alle Bestellungen von Kunden aus Deutschland:
                                    ```sql
                                    SELECT Orders.OrderID, Customers.CustomerName, Customers.Country
                                    FROM Orders
                                    JOIN Customers ON Orders.CustomerID = Customers.CustomerID
                                    WHERE Customers.Country = 'Germany';
                                    ```

                                    Alias verwenden:
                                    Vereinfache Tabellennamen mit Aliases.

                                    Beispiel:
                                    ```sql
                                    SELECT o.OrderID, c.CustomerName
                                    FROM Orders AS o
                                    JOIN Customers AS c ON o.CustomerID = c.CustomerID;
                                    ```

                                    JOIN-Typen:
                                    - INNER JOIN (nur übereinstimmende Datensätze)
                                    - LEFT JOIN (alle Datensätze der linken Tabelle und übereinstimmende der rechten)
                                    - RIGHT JOIN
                                    - FULL OUTER JOIN

                                    LEFT JOIN Beispiel:
                                    Kunden und ihre Bestellungen (falls vorhanden):
                                    ```sql
                                    SELECT Customers.CustomerName, Orders.OrderID
                                    FROM Customers
                                    LEFT JOIN Orders ON Customers.CustomerID = Orders.CustomerID;
                                    ```

                                    FULL JOIN Beispiel:
                                    Alle Kunden und alle Bestellungen, auch ohne Übereinstimmung:
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
                                    Die GROUP BY-Anweisung gruppiert Zeilen mit denselben Werten zu Zusammenfassungszeilen,
                                    z.B. "Anzahl der Kunden in jedem Land ermitteln".

                                    Häufig verwendet mit Aggregatfunktionen
                                    (COUNT(), MAX(), MIN(), SUM(), AVG()) zur Gruppierung des Ergebnisses.

                                    Syntax:
                                    ```sql
                                    SELECT column_name(s)
                                    FROM table_name
                                    WHERE condition
                                    GROUP BY column_name(s)
                                    ORDER BY column_name(s);
                                    ```

                                    Beispiele:

                                    Anzahl der Kunden pro Land:
                                    ```sql
                                    SELECT COUNT(CustomerID), Country
                                    FROM Customers
                                    GROUP BY Country;
                                    ```

                                    Anzahl der Kunden pro Land, sortiert von hoch nach niedrig:
                                    ```sql
                                    SELECT COUNT(CustomerID), Country
                                    FROM Customers
                                    GROUP BY Country
                                    ORDER BY COUNT(CustomerID) DESC;
                                    ```

                                    GROUP BY mit JOIN:
                                    Anzahl der versendeten Bestellungen pro Spediteur:
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
                                    Die HAVING-Klausel wurde hinzugefügt, weil WHERE nicht mit Aggregatfunktionen verwendet werden kann.

                                    Syntax:
                                    ```sql
                                    SELECT column_name(s)
                                    FROM table_name
                                    WHERE condition
                                    GROUP BY column_name(s)
                                    HAVING condition
                                    ORDER BY column_name(s);
                                    ```

                                    Beispiele:

                                    Anzahl der Kunden pro Land, nur Länder mit mehr als 5 Kunden:
                                    ```sql
                                    SELECT COUNT(CustomerID), Country
                                    FROM Customers
                                    GROUP BY Country
                                    HAVING COUNT(CustomerID) > 5;
                                    ```

                                    Sortiert von hoch nach niedrig, nur wenn mehr als 5 Kunden:
                                    ```sql
                                    SELECT COUNT(CustomerID), Country
                                    FROM Customers
                                    GROUP BY Country
                                    HAVING COUNT(CustomerID) > 5
                                    ORDER BY COUNT(CustomerID) DESC;
                                    ```

                                    Weitere HAVING-Beispiele:

                                    Angestellte mit mehr als 10 Bestellungen:
                                    ```sql
                                    SELECT Employees.LastName, COUNT(Orders.OrderID) AS NumberOfOrders
                                    FROM Orders
                                    INNER JOIN Employees ON Orders.EmployeeID = Employees.EmployeeID
                                    GROUP BY LastName
                                    HAVING COUNT(Orders.OrderID) > 10;
                                    ```

                                    Überprüfung für "Davolio" oder "Fuller" mit mehr als 25 Bestellungen:
                                    ```sql
                                    SELECT Employees.LastName, COUNT(Orders.OrderID) AS NumberOfOrders
                                    FROM Orders
                                    INNER JOIN Employees ON Orders.EmployeeID = Employees.EmployeeID
                                    WHERE LastName = 'Davolio' OR LastName = 'Fuller'
                                    GROUP BY LastName
                                    HAVING COUNT(Orders.OrderID) > 25;
                                    ```
                                  """,
                        SqlKategorie.HAVING,
                        null
                ),
                createMaterial(
                        "SQL Aggregate Functions",
                        """
                                    Eine Aggregatfunktion führt eine Berechnung für eine Gruppe von Werten durch und gibt einen einzelnen Wert zurück.

                                    Wird oft mit GROUP BY verwendet, um das Ergebnis in Gruppen aufzuteilen und für jede Gruppe einen Wert zu erhalten.

                                    Die gängigsten Aggregatfunktionen sind:

                                    MIN() – kleinster Wert
                                    MAX() – größter Wert
                                    COUNT() – Anzahl der Zeilen
                                    SUM() – Summe einer numerischen Spalte
                                    AVG() – Durchschnitt einer numerischen Spalte

                                    Aggregatfunktionen ignorieren NULL-Werte
                                    (ausgenommen COUNT()).

                                    Detaillierte Beschreibungen folgen in den nächsten Abschnitten.
                                  """,
                        SqlKategorie.AGGREGATE,
                        null
                ),
                createMaterial(
                        "The SQL Subquery",
                        """
                                    Eine Subquery ist eine SQL-Abfrage, die innerhalb einer anderen Abfrage verschachtelt ist.

                                    Subqueries können in SELECT-, FROM- oder WHERE-Klauseln verwendet werden,
                                    um Zwischenergebnisse zu berechnen oder zu filtern.

                                    Syntax:
                                    ```sql
                                    SELECT column1
                                    FROM table1
                                    WHERE column2 = (SELECT column2 FROM table2 WHERE condition);
                                    ```

                                    Beispiel:
                                    Gib alle Produkte mit einem Preis über dem Durchschnitt zurück:
                                    ```sql
                                    SELECT * FROM Products
                                    WHERE Price > (SELECT AVG(Price) FROM Products);
                                    ```

                                    Subquery in SELECT:
                                    Du kannst einen Wert aus einer Subquery direkt in der Ergebnismenge zurückgeben:
                                    ```sql
                                    SELECT ProductName,
                                           (SELECT AVG(Price) FROM Products) AS AvgPrice
                                    FROM Products;
                                    ```

                                    Subquery in FROM:
                                    Subqueries in FROM werden als temporäre Tabellen betrachtet:
                                    ```sql
                                    SELECT Category, AVG_Price
                                    FROM (
                                        SELECT CategoryID AS Category, AVG(Price) AS AVG_Price
                                        FROM Products
                                        GROUP BY CategoryID
                                    ) AS CategoryAverages;
                                    ```

                                    Hinweise:
                                    - Subqueries müssen einen einzelnen Wert zurückgeben, wenn sie mit (=,<,>) etc. verwendet werden.
                                    - Für mehrere Werte verwende IN, ANY oder ALL.
                                  """,
                        SqlKategorie.SUBQUERY,
                        null
                ),
                createMaterial(
                        "The SQL COUNT() Function",
                        """
                                    Die COUNT()-Funktion gibt die Anzahl der Zeilen zurück, die eine bestimmte Bedingung erfüllen.

                                    Syntax:
                                    ```sql
                                    SELECT COUNT(column_name)
                                    FROM table_name
                                    WHERE condition;
                                    ```

                                    COUNT(*) zählt alle Zeilen, einschließlich NULL-Werten.

                                    Beispiel:
                                    Anzahl der Kunden zählen:
                                    ```sql
                                    SELECT COUNT(*) FROM Customers;
                                    ```

                                    Nur nicht-NULL-Werte zählen:
                                    ```sql
                                    SELECT COUNT(CustomerName) FROM Customers;
                                    ```

                                    COUNT() mit WHERE:
                                    Kunden aus Deutschland zählen:
                                    ```sql
                                    SELECT COUNT(*) FROM Customers
                                    WHERE Country = 'Germany';
                                    ```

                                    COUNT() mit GROUP BY:
                                    Anzahl der Kunden pro Land zählen:
                                    ```sql
                                    SELECT Country, COUNT(CustomerID)
                                    FROM Customers
                                    GROUP BY Country;
                                    ```

                                    COUNT() ignoriert NULL-Werte bei der Zählung von Spalten.
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