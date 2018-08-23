package com.example.stockmarketapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "Lollapalooza0905";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/stocks?useSSL=false&serverTimezone=UTC";

    public static void main(String[] args) throws SQLException {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            System.out.println("Connected...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }
    }
}
