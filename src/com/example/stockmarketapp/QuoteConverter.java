package com.example.stockmarketapp;

import com.google.gson.*;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class QuoteConverter {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "Lollapalooza0905";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/stocks?useSSL=false&serverTimezone=UTC";
    private static final String jsonData = "https://bootcamp-training-files.cfapps.io/week1/week1-stocks.json";

    public static JsonArray getJson() throws IOException {

        InputStream stream = null;
        BufferedInputStream buf = null;
        StringBuilder sb = null;

        try {
            // open stream to read url
            URL url = new URL(jsonData);
            stream = url.openStream();
            buf = new BufferedInputStream(stream);

            // StringBuilder to store input
            sb = new StringBuilder();

            while (true) {
                int data = buf.read();

                if (data == -1) {
                    break;
                } else {
                    sb.append((char) data);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
            buf.close();
        }

        // converts string to JsonArray provided by Gson library
        JsonArray jsonArray = new JsonParser().parse(sb.toString()).getAsJsonArray();
        return jsonArray;
    }

    public static void insertRows(JsonArray quoteArr) throws SQLException {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

            for (int i = 0; i < quoteArr.size(); i++) {

                JsonElement e = quoteArr.get(i);
                JsonObject j = e.getAsJsonObject();

                String symbol = j.get("symbol").getAsString();

                int volume = j.get("volume").getAsInt();

                float price = j.get("price").getAsFloat();

                String date = j.get("date").getAsString();


//                String[] dateTime = date.split("T");
//                String dateOnly = dateTime[0];
//                String timeOnly = dateTime[1].substring(0, 8);
//
//                System.out.println(timeOnly + ' ' + dateOnly);
                System.out.println("inserting data...");

                String query = "INSERT into stock_quotes (symbol, volume, price, date)" + "values (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, symbol);
                stmt.setInt(2, volume);
                stmt.setFloat(3, price);
                stmt.setString(4,  date);

                stmt.execute();
                System.out.println("data inserted!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void getHighs(String date) throws SQLException {

        Connection conn = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

            //sql statement retrieves each symbol and its highest price based on the day
            String query =
                    "SELECT symbol, MAX(price) as dateHigh FROM stock_quotes WHERE date LIKE ? GROUP BY symbol";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, date + '%');
            rs = stmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            System.out.println("Highs for " + date);
            System.out.println("-----------------");
            while (rs.next()) {
                String column1 = rs.getString("symbol") + ":";
                String column2 = rs.getString("dateHigh");
                System.out.print(column1 + " " + column2 + "\n");
            }
            System.out.println(" ");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void getLows(String date) throws SQLException {

        Connection conn = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

            //sql statement retrieves each symbol and its lowest price based on the day
            String query =
                    "SELECT symbol, MIN(price) as dateLow FROM stock_quotes WHERE date LIKE ? GROUP BY symbol";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, date + '%');
            rs = stmt.executeQuery();

            System.out.println("Lows for " + date);
            System.out.println("-----------------");
            while (rs.next()) {
                String column1 = rs.getString("symbol") + ":";
                String column2 = rs.getString("dateLow");
                System.out.print(column1 + " " + column2 + "\n");
            }
            System.out.println(" ");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void volumeTraded(String date) throws SQLException {

        Connection conn = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

            //sql statement retrieves each symbol and its highest price based on the day
            String query =
                    "SELECT symbol, SUM(volume) as total_volume FROM stock_quotes WHERE date LIKE ? GROUP BY symbol";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, date + '%');
            rs = stmt.executeQuery();

            System.out.println("Total Volume Traded On " + date);
            System.out.println("-----------------");
            while (rs.next()) {
                String column1 = rs.getString("symbol") + ":";
                String column2 = rs.getString("total_volume");
                System.out.print(column1 + " " + column2 + "\n");
            }
            System.out.println(" ");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void getClosingPrice(String date) throws SQLException {

        Connection conn = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

            //sql statement retrieves each symbol and its highest price based on the day
            String query =
                    "select symbol, price, date FROM stock_quotes WHERE date like ? ORDER BY date DESC LIMIT 5";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, date + '%');
            rs = stmt.executeQuery();

            System.out.println("Closing Prices for " + date);
            System.out.println("-----------------");
            while (rs.next()) {
                String column1 = rs.getString("symbol") + ":";
                String column2 = rs.getString("price");
                System.out.print(column1 + " " + column2 + "\n");
            }
            System.out.println(" ");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void main(String args[]) throws SQLException, IOException {

        //JsonArray jArr = getJson();

        //insertRows(jArr);
        System.out.println("Enter a Date (YYYY-MM-DD): ");
        Scanner sc = new Scanner(System.in);
        String date = sc.nextLine();
        getHighs(date);
        getLows(date);
        volumeTraded(date);
        getClosingPrice(date);

    }
}
