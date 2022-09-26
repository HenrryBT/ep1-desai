package com.henrrybeltran.evaluacionpermanente1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class Evaluacionpermanente1Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Evaluacionpermanente1Application.class, args);

		String jdbcUrl = "jdbc:mysql://localhost:3306/desai";

		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection connection = DriverManager.getConnection(jdbcUrl, "root", "root");

		// Execute the methods
		// Here

		// Use this method for first method
		String subscription = "'Standard'";
		String queryTest = "SELECT * FROM Accounts WHERE Subscription = " + subscription;
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(queryTest);

		// Refactor this, because it's going to repeat
		while (resultSet.next()){
			System.out.println(resultSet.getString("ID") + " " +
					resultSet.getString("Name") + " " +
					resultSet.getString("Lastname") + " " +
					resultSet.getString("Username") + " " +
					resultSet.getString("Email") + " " +
					resultSet.getString("Pass") + " " +
					resultSet.getString("Age") + " " +
					resultSet.getString("Subscription"));
		}

		connection.close();
	}

	// Statement delete an account with the username and password

	// Prepared statement update the name and lastname with the email

	// Callable statement get all accounts base on the type of subscription

	// Callable statement create a new account

	// Callable statement update the subscription with the email and password

	// Here I'm going to put all the refactored methods
}
