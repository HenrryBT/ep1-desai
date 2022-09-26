package com.henrrybeltran.evaluacionpermanente1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class Evaluacionpermanente1Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Evaluacionpermanente1Application.class, args);

		String jdbcUrl = "jdbc:mysql://localhost:3306/desai";

		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection connection = DriverManager.getConnection(jdbcUrl, "root", "root");

		// To execute the methods
		Evaluacionpermanente1Application ep1 = new Evaluacionpermanente1Application();

		// METHOD 1 - First we instantiate a new account and then pass to the method
		User account = new User(
				"Lily",
				"William",
				"Lulu12",
				"lulu.will@gmail.com",
				"supersecret",
				22,
				"Free"
		);

		ep1.createNewAccount(connection, account);

		// METHOD 2 - This method updates the name and lastname, using the email as identifier
//		ep1.updateCompleteNameAccount(connection, "Lulu", "William", "lulu.will@gmail.com");

		// METHOD 3 - This method updates your subscription account, with your login using email and password
//		ep1.updateSubscriptionAccount(connection, "lulu.will@gmail.com", "supersecret", "Standard");

		// METHOD 4 - This returns a list of all accounts with a specific subscription type
//		ep1.getAccountsGroupBySubscription(connection, "Standard");

		// METHOD 5 - This deletes your account by email and password
//		ep1.deleteYourUserAccount(connection, "'Lulu12'", "'supersecret'");

		connection.close();
	}

	// CALLABLE STATEMENT - create a new account
	private void createNewAccount(Connection connection, User user) throws Exception {
		CallableStatement call = connection.prepareCall( "{ CALL sp_createNewAccount(?, ?, ?, ?, ?, ?, ?, ?) }");
		call.setString(1, user.name);
		call.setString(2, user.lastname);
		call.setString(3, user.username);
		call.setString(4, user.email);
		call.setString(5, user.password);
		call.setInt(6, user.age);
		call.setString(7, user.subscription);
		call.registerOutParameter(8, Types.INTEGER);

		ResultSet callResult = call.executeQuery();

		int validation = call.getInt(8);

		if (validation == 1) {
			printUnknowResult(callResult);
		}
		else if (validation == 2) {
			System.out.println("Tu cuenta ha sido creado con éxito");

			getAllAccounts(connection);
		}
	}

	// PREPARED STATEMENT - update the name and lastname with the email
	private void updateCompleteNameAccount(Connection connection, String name, String lastname, String email) throws Exception {
		PreparedStatement statement = connection.prepareStatement(
				"UPDATE Accounts " +
						"SET Name = ?, " +
						"Lastname = ? " +
						"WHERE Email = ?");
		statement.setString(1, name);
		statement.setString(2, lastname);
		statement.setString(3, email);

		int affectedRows = statement.executeUpdate();

		System.out.println("Afected rows: " + affectedRows);

		// print the account information to check the update
		if (affectedRows > 0)
			getAccountByEmail(connection, email);
	}

	// CALLABLE STATEMENT - update the subscription with the email and password
	private void updateSubscriptionAccount(Connection connection, String email, String password, String newSubscription) throws Exception {
		CallableStatement call = connection.prepareCall("{ CALL sp_updateSubscription(?, ?, ?) }");
		call.setString(1, email);
		call.setString(2, password);
		call.setString(3, newSubscription);

		ResultSet result = call.executeQuery();
		printUnknowResult(result);
		getAccountByEmail(connection, email);
	}

	// CALLABLE STATEMENT - get all accounts base on the type of subscription
	private void getAccountsGroupBySubscription(Connection connection, String subscription) throws Exception {
		CallableStatement call = connection.prepareCall("{ CALL sp_getAllAccountsWithSubscription(?) }");
		call.setString(1, subscription);

		ResultSet result = call.executeQuery();
		printAllTheResults(result);
	}

	// STATEMENT - delete an account with the username and password
	private void deleteYourUserAccount(Connection connection, String username, String password) throws Exception {
		String deleteQuery = "DELETE FROM Accounts WHERE Username = " + username + " AND Pass = " + password + ";";
		Statement deleteStatement = connection.createStatement();
		int affectedRows = deleteStatement.executeUpdate(deleteQuery);

		System.out.println("Afected rows: " + affectedRows);

		// This shows all the accounts content, if the deleted account exists
		if (affectedRows > 0) {
			getAllAccounts(connection);

			System.out.println("The account was successfully deleted");
		}
		else {
			System.out.println("This account doesn't exists");
		}
	}


	// STATEMENT - get all the accounts
	// I'm using this method to show the results in the other methods
	private void getAllAccounts(Connection connection) throws Exception {
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery("SELECT * FROM Accounts");

		printAllTheResults(result);
	}

	// PREPARED STATEMENT - get an accounts by email
	// I'm using this method to show the results in the other methods
	private void getAccountByEmail(Connection connection, String email) throws Exception {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM Accounts WHERE Email = ?");
		statement.setString(1, email);

		ResultSet result = statement.executeQuery();

		printAllTheResults(result);
	}


	// Extra methods
	private void printAllTheResults(ResultSet resultSet) throws Exception {
		while (resultSet.next()){
			System.out.println(
					resultSet.getString("ID") + " " +
					resultSet.getString("Name") + " " +
					resultSet.getString("Lastname") + " " +
					resultSet.getString("Username") + " " +
					resultSet.getString("Email") + " " +
					resultSet.getString("Pass") + " " +
					resultSet.getString("Age") + " " +
					resultSet.getString("Subscription"));
		}
	}

	private void printUnknowResult(ResultSet result) throws Exception {
		while (result.next())  {
			System.out.println(result.getString(1));
		}
	}
}
