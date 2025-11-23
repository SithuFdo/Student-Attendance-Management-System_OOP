package com.ijse.studentattendancems.db;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection;
    private Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_attendance_db", "root", "Rashmi_123");
    private final String url = "jdbc:mysql://localhost:3306/student_attendance_db";
    private final String username = "root";
    private final String password = "Rashmi_123";

    private DbConnection() throws SQLException {
        this.connection.setAutoCommit(true);
    }

    public static DbConnection getInstance() throws SQLException {
        if (dbConnection == null) {
            dbConnection = new DbConnection();
        }

        return dbConnection;
    }

    public Connection getConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_attendance_db", "root", "Rashmi_123");
        }

        return this.connection;
    }
}
