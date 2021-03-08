package com.highradius;

import java.sql.*;

public class Connect {
	String url = "";
	String dbName = "";
	String user = "";
	String pass = "";
	Connection dbconn = null;

	public Connect(String url, String dbName, String user, String pass) {
		this.url = url;
		this.dbName = dbName;
		this.user = user;
		this.pass = pass;
	}

	Connection getConnection() {
		try {
			// Register JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Open the connection
			this.dbconn = DriverManager.getConnection(this.url + this.dbName, this.user, this.pass);

			// display the message weather the connection is successful or not
			if (this.dbconn != null) {
				System.out.println("SQL connection successfull" + "\n");
			} else {
				System.out.println("Failed to establish SQL connection" + "\n");
			}
		}
		// catch the exception,if occurred
		catch (Exception e) {
			System.out.println("Exception encountered : " + e.getMessage() + "\n");
		}
		return dbconn;

	}
}