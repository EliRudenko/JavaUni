package org.example;

import java.sql.*;
import java.util.Properties;

public class Main
{
    // конфіденційні дані краще зберігати у файлі конфігурації
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:FREE";
    private static final String USER = "SYS";
    private static final String PASSWORD = "eli301186";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // встановлення з'єднання з базою даних
            conn = connectToDatabase();
            stmt = conn.createStatement();

            // перевірка існування таблиці та її видалення, якщо вона існує
            if (tableExists(stmt, "DEMO")) {
                dropTable(stmt, "DEMO");
            }

            createTable(stmt);
            truncateTable(stmt, "DEMO");
            insertData(stmt);
            readData(stmt);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // закриття ресурсів для уникнення витоку пам'яті
            closeResources(rs, stmt, conn);
        }
    }

    // метод для встановлення з'єднання з базою даних
    private static Connection connectToDatabase() throws ClassNotFoundException, SQLException {
        // завантаження драйвера oracle jdbc
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Properties props = new Properties();
        props.put("user", USER);
        props.put("password", PASSWORD);
        // встановлення ролі sysdba для доступу до адміністративних функцій
        props.put("internal_logon", "SYSDBA");
        return DriverManager.getConnection(URL, props);
    }

    // перевірка, чи існує таблиця в базі даних
    private static boolean tableExists(Statement stmt, String tableName) throws SQLException {
        // запит для перевірки наявності таблиці в базі даних
        String checkTableSQL = "SELECT COUNT(*) FROM all_tables WHERE table_name = '" + tableName + "'";
        try (ResultSet rs = stmt.executeQuery(checkTableSQL)) {
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    // видалення таблиці з бази даних
    private static void dropTable(Statement stmt, String tableName) throws SQLException {
        String dropTableSQL = "DROP TABLE " + tableName;
        stmt.executeUpdate(dropTableSQL);
        System.out.println("Таблицю " + tableName + " видалено");
    }

    // створення нової таблиці demo
    private static void createTable(Statement stmt) throws SQLException {
        // створення таблиці з полями id, productname, customername тощо
        String createTableSQL = "CREATE TABLE DEMO ("
                + "Id NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY, "
                + "ProductName VARCHAR2(20) NOT NULL, "
                + "CustomerName VARCHAR2(20) NOT NULL, "
                + "DispatchDate DATE, "
                + "DeliveryTime TIMESTAMP, "
                + "Price INT, "
                + "Location VARCHAR2(20))";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Таблицю DEMO створено");
    }

    // очищення даних у таблиці
    private static void truncateTable(Statement stmt, String tableName) throws SQLException {
        String truncateTableSQL = "TRUNCATE TABLE " + tableName;
        stmt.executeUpdate(truncateTableSQL);
        System.out.println("Дані з таблиці " + tableName + " видалено");
    }

    // вставка тестових даних у таблицю
    private static void insertData(Statement stmt) throws SQLException {
        // вставка трьох записів із різними значеннями
        String insertDataSQL1 = "INSERT INTO DEMO (ProductName, CustomerName, DispatchDate, DeliveryTime, Price, Location) VALUES "
                + "('Laptop', 'Ганна', TO_DATE('2024-08-15', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-08-16 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1200, 'Одеса')";
        String insertDataSQL2 = "INSERT INTO DEMO (ProductName, CustomerName, DispatchDate, DeliveryTime, Price, Location) VALUES "
                + "('Smartphone', 'Борис', TO_DATE('2024-08-16', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-08-17 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 800, 'Ужгород')";
        String insertDataSQL3 = "INSERT INTO DEMO (ProductName, CustomerName, DispatchDate, DeliveryTime, Price, Location) VALUES "
                + "('Tablet', 'Петро', TO_DATE('2024-08-17', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-08-18 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 450, 'Чернівці')";

        stmt.executeUpdate(insertDataSQL1);
        stmt.executeUpdate(insertDataSQL2);
        stmt.executeUpdate(insertDataSQL3);
        System.out.println("Дані вставлено");
    }

    // зчитування та виведення даних із таблиці
    private static void readData(Statement stmt) throws SQLException {
        String selectSQL = "SELECT Id, ProductName, CustomerName, DispatchDate, DeliveryTime, Price, Location FROM DEMO";
        try (ResultSet rs = stmt.executeQuery(selectSQL)) {
            // ітерація по всіх записах у таблиці
            while (rs.next()) {
                int id = rs.getInt("Id");
                String productName = rs.getString("ProductName");
                String customerName = rs.getString("CustomerName");
                Date dispatchDate = rs.getDate("DispatchDate");
                Timestamp deliveryTime = rs.getTimestamp("DeliveryTime");
                int price = rs.getInt("Price");
                String location = rs.getString("Location");
                System.out.println("ID: " + id + ", ProductName: " + productName + ", CustomerName: " + customerName
                        + ", DispatchDate: " + dispatchDate + ", DeliveryTime: " + deliveryTime + ", Price: " + price
                        + ", Location: " + location);
            }
        }
    }

    // закриття ресурсів бази даних
    private static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            // перевірка та закриття resultset, statement і connection
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
