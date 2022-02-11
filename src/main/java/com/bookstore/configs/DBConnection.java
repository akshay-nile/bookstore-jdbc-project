package com.bookstore.configs;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

	private static Connection conn;

	public static Connection getConnection() {
		try {
			Properties pr = new Properties();
			pr.load(new FileReader("resources/connection.properties"));

			String driver = pr.getProperty("driver-class");
			String url = pr.getProperty("url");
			String username = pr.getProperty("username");
			String password = pr.getProperty("password");

			if (conn == null) {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);
			}
		} catch (IOException e) {
			System.err.println("connection.properies file not found..!");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not load the Driver class..!");
		} catch (Exception e) {
			System.err.println("Failed to establish the connection with database..!");
		}

		return conn;
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
