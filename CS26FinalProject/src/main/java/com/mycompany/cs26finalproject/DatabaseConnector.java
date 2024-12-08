/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cs26finalproject;

/**
 *
 * @author dionardvale
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test"; 
    private static final String USER = "root"; 
    private static final String PASS = "Gwaposineilugdionard@2002";  

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL driver (if using older JDBC versions)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection established successfully!");

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
        }

        return connection;
    }
}
