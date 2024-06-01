# Banking_Management_System
This Banking Management System UI project provides a comprehensive solution for managing banking operations with an intuitive interface, making it easier for bank staff to handle customer and account data efficiently.

#Development Tools and Environment
->Java: Programming language used for development.
->JavaFX: Library used for building the graphical user interface.
->Oracle Database: Database system used for storing banking data.
->JDBC: API used for database connectivity and operations.

#Functionalities

1. Customer Management
	Show Customers:
  - Displays a list of all customers stored in the database.
  - Provides customer details including Customer Number, Name, Phone Number, and City.

	Add Customer:
  - Allows the user to add a new customer to the database.
  - Requires input for Customer Number, Name, Phone Number, and City.
  - Inserts the new customer record into the database and confirms the addition with a success message.

	Delete Customer:
  - Enables the user to delete a customer from the database.
  - Requires the Customer Number of the customer to be deleted.
  - Deletes the specified customer record and confirms the deletion with a success message.

	Update Customer:
  - Allows the user to update existing customer information.
  - Requires the Customer Number, the attribute to be updated (Name, Phone Number, or City), and the new value.
  - Updates the customer record in the database and confirms the update with a success message.

2. Account Management
	Show Account Details:
  - Retrieves and displays details of a specific account based on the Account Number.
  - Displays account information including Account Number, Type, Balance, and Branch Code.

	Deposit Money:
  - Allows the user to deposit money into a specific account.
  - Requires input for Account Number and the Amount to be deposited.
  - Updates the account balance in the database and confirms the deposit with a success message.

	Withdraw Money:
  - Enables the user to withdraw money from a specific account.
  - Requires input for Account Number and the Amount to be withdrawn.
  - Checks if the account has sufficient balance before updating the account balance in the database.
  - Confirms the withdrawal with a success message or alerts the user if there is insufficient balance.

3. Loan Management
	Show Loan Details:
  - Retrieves and displays loan details for a specific customer based on the Customer Number.
  - Displays loan information including Loan Number, Loan Amount, and Branch Code.
  - Notifies the user if no loans are found for the specified customer.

Project Components

	Database Connection
- Establishes a connection to the Oracle database using JDBC.
- Manages database operations and ensures the connection is closed properly when the application stops.

	User Interface (UI)
- Developed using JavaFX, providing a graphical interface for user interaction.
- Contains menus and various forms for handling customer, account, and loan operations.

	Event Handling
- Handles user actions such as button clicks and menu item selections.
- Executes appropriate database operations based on user inputs and displays results or notifications.

	Alert Notifications
- Provides feedback to users through alert dialogs.
- Displays information, success messages, warnings, and error messages based on the outcomes of operations.

Classes and Structure
	BankingManagementSystemUI: Main application class, responsible for setting up the UI, connecting to the database, and handling events.

	Customer: Model class representing customer data with attributes such as Customer Number, Name, Phone Number, and City.
