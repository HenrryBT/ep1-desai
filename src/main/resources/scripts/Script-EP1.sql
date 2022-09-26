/*
 *	Evaluación Permanente 1 - Henrry Beltrán De La Torre
 */

USE DESAI;

SHOW TABLES;


-- We create a table for accounts that use a subscription system

CREATE TABLE Accounts (
	ID INT AUTO_INCREMENT NOT NULL,
	Name VARCHAR(80) NOT NULL,
	Lastname VARCHAR(100),
	Username VARCHAR(20) NOT NULL,
	Email VARCHAR(100) NOT NULL,
	Pass VARCHAR(30) NOT NULL,
	Age INT,
	Subscription VARCHAR(100),
	PRIMARY KEY (ID)
)


-- To delete the whole table

DROP TABLE Accounts;


-- Show all the rows from the table

SELECT * FROM Accounts;


-- Creates new accounts

INSERT INTO Accounts (Name, Lastname, Username, Email, Pass, Age, Subscription)
VALUES ('Elizabeth', 'Stones', 'Eli24', 'elizabeth24@gmail.com', '123456', 25, 'Standard');

INSERT INTO Accounts (Name, Lastname, Username, Email, Pass, Age, Subscription)
VALUES ('Dante', 'Ramos', 'DanteRamos16', 'd_ramos@gmail.com', 'dante', 28, 'Free');

INSERT INTO Accounts (Name, Lastname, Username, Email, Pass, Age, Subscription)
VALUES ('Jhon', 'Collins', 'J_Master', 'jhon.master.16@gmail.com', '123456', 20, 'Standard');

INSERT INTO Accounts (Name, Lastname, Username, Email, Pass, Age, Subscription)
VALUES ('Isha', 'Malone', 'IshaMM', 'isha_mm@gmail.com', 'malone123', 31, 'Premium');


-- We update the username by validating the email and password

UPDATE Accounts SET Username = 'Dante_16' 
WHERE Email = 'd_ramos@gmail.com' AND Pass = 'dante';


-- To delete an account

DELETE FROM Accounts WHERE ID = 4;



/*  ==========  STORED PROCEDURES  ==========  */


-- This creates a new account

DROP PROCEDURE IF EXISTS sp_createNewAccount;

CREATE PROCEDURE sp_createNewAccount(
	IN p_name VARCHAR(80),
	IN p_lastname VARCHAR(100),
	IN p_username VARCHAR(20),
	IN p_email VARCHAR(100),
	IN p_pass VARCHAR(30),
	IN p_age INT,
	IN p_subscription VARCHAR(100),
	OUT out_validation INT
)
BEGIN
	DECLARE countUsernames INT DEFAULT 0;
	DECLARE countEmails INT DEFAULT 0;

	SELECT COUNT(*) INTO countUsernames FROM Accounts WHERE Username = p_username ;
	SELECT COUNT(*) INTO countEmails FROM Accounts WHERE Email = p_email ;

	IF (countUsernames > 0) THEN
		SELECT 'El nombre de usuario ya existe';

		SET out_validation = 1;				-- This means that new account needs to change the form
	ELSEIF (countEmails > 0) THEN
		SELECT 'Este correo ya esta registrado';
	
		SET out_validation = 1; 			-- This means that new account needs to change the form
	ELSE
		INSERT INTO Accounts (Name, Lastname, Username, Email, Pass, Age, Subscription)
		VALUES (p_name, p_lastname, p_username, p_email, p_pass, p_age, p_subscription);
	
		SET out_validation = 2; 			-- This means that the account was successfully created and you can tested
	END IF;
END

-- This execute the stored procedure

CALL sp_createNewAccount('Lily', 'William', 'Lulu12', 'lulu.will@gmail.com', 'supersecret', 22, 'Free', @validation);
SELECT @validation;


-- This change your subscription base

DROP PROCEDURE IF EXISTS sp_updateSubscription;

CREATE PROCEDURE sp_updateSubscription(
	IN p_email VARCHAR(100),
	IN p_pass VARCHAR(30),
	IN p_newSubscription VARCHAR(100)
)
BEGIN
	DECLARE validation INT DEFAULT 0;
	DECLARE currentSubscription VARCHAR(100) DEFAULT null;

	SELECT COUNT(*) INTO validation 
	FROM Accounts 
	WHERE Email = p_email AND Pass = p_pass;

	IF (validation <= 0) THEN
		SELECT 'Correo o contraseña incorrectos';
	
	ELSE
		SELECT Subscription INTO currentSubscription 
		FROM Accounts 
		WHERE Email = p_email;

		IF (currentSubscription = p_newSubscription) THEN
			SELECT CONCAT('Sin cambios, su suscripción actual ya es ', currentSubscription);
		ELSE
			UPDATE Accounts 
			SET Subscription = p_newSubscription
			WHERE Email = p_email;
		
			SELECT CONCAT('Su suscripción se actualizó con éxito a ', Subscription)
			FROM Accounts
			WHERE Email = p_email;
		END IF;
	END IF;
END

-- This execute the stored procedure

CALL sp_updateSubscription('lulu.will@gmail.com', 'supersecret', 'Standard');


-- This query returns all accounts that have a specific type of subscription

DROP PROCEDURE IF EXISTS sp_getAllAccountsWithSubscription;

CREATE PROCEDURE sp_getAllAccountsWithSubscription(IN p_subscription VARCHAR(100))
BEGIN
	SELECT * FROM Accounts
	WHERE Subscription = p_subscription;
END

-- This execute the stored procedure

CALL sp_getAllAccountsWithSubscription('Standard');





