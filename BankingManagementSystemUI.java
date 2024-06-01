import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class BankingManagementSystemUI extends Application {

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String USER = "system";
    private static final String PASSWORD = "nirupom";

    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Banking Management System");

        // Connect to database
        connectToDatabase();

        // Create UI elements
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Create and add menu
        MenuBar menuBar = new MenuBar();
        Menu customerMenu = new Menu("Customer");
        MenuItem showCustomers = new MenuItem("Show Customers");
        MenuItem addCustomer = new MenuItem("Add Customer");
        MenuItem deleteCustomer = new MenuItem("Delete Customer");
        MenuItem updateCustomer = new MenuItem("Update Customer");
        customerMenu.getItems().addAll(showCustomers, addCustomer, deleteCustomer, updateCustomer);

        Menu accountMenu = new Menu("Account");
        MenuItem showAccountDetails = new MenuItem("Show Account Details");
        MenuItem depositMoney = new MenuItem("Deposit Money");
        MenuItem withdrawMoney = new MenuItem("Withdraw Money");
        accountMenu.getItems().addAll(showAccountDetails, depositMoney, withdrawMoney);

        Menu loanMenu = new Menu("Loan");
        MenuItem showLoanDetails = new MenuItem("Show Loan Details");
        loanMenu.getItems().add(showLoanDetails);

        menuBar.getMenus().addAll(customerMenu, accountMenu, loanMenu);
        root.getChildren().add(menuBar);

        // Event handling
        showCustomers.setOnAction(e -> showCustomerRecords());
        addCustomer.setOnAction(e -> addCustomerRecord());
        deleteCustomer.setOnAction(e -> deleteCustomerRecord());
        updateCustomer.setOnAction(e -> updateCustomerInformation());
        showAccountDetails.setOnAction(e -> showAccountDetails());
        depositMoney.setOnAction(e -> depositMoney());
        withdrawMoney.setOnAction(e -> withdrawMoney());
        showLoanDetails.setOnAction(e -> showLoanDetails());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomerRecords() {
        Stage stage = new Stage();
        stage.setTitle("Customer Records");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        TableView<Customer> tableView = new TableView<>();
        TableColumn<Customer, String> custNoCol = new TableColumn<>("Customer Number");
        custNoCol.setCellValueFactory(new PropertyValueFactory<>("custNo"));
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Customer, String> phoneNoCol = new TableColumn<>("Phone Number");
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        TableColumn<Customer, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));

        tableView.getColumns().addAll(custNoCol, nameCol, phoneNoCol, cityCol);

        String query = "SELECT * FROM CUSTOMER";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            tableView.getItems().clear();
            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getString("cust_no"),
                        resultSet.getString("name"),
                        resultSet.getString("phone_no"),
                        resultSet.getString("city")
                );
                tableView.getItems().add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        root.getChildren().add(tableView);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void addCustomerRecord() {
        Stage stage = new Stage();
        stage.setTitle("Add Customer");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label custNoLabel = new Label("Customer Number:");
        TextField custNoField = new TextField();
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label phoneNoLabel = new Label("Phone Number:");
        TextField phoneNoField = new TextField();
        Label cityLabel = new Label("City:");
        TextField cityField = new TextField();

        Button addButton = new Button("Add Customer");
        addButton.setOnAction(e -> {
            String custNo = custNoField.getText();
            String name = nameField.getText();
            String phoneNo = phoneNoField.getText();
            String city = cityField.getText();

            String query = "INSERT INTO CUSTOMER (CUST_NO, NAME, PHONE_NO, CITY) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, custNo);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, phoneNo);
                preparedStatement.setString(4, city);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted.");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(custNoLabel, custNoField, nameLabel, nameField,
                phoneNoLabel, phoneNoField, cityLabel, cityField, addButton);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void deleteCustomerRecord() {
        Stage stage = new Stage();
        stage.setTitle("Delete Customer Record");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label custNoLabel = new Label("Customer Number:");
        TextField custNoField = new TextField();

        Button deleteButton = new Button("Delete Customer");
        deleteButton.setOnAction(e -> {
            String custNo = custNoField.getText();
            String query = "DELETE FROM CUSTOMER WHERE CUST_NO = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, custNo);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) deleted.");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(custNoLabel, custNoField, deleteButton);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void updateCustomerInformation() {
        Stage stage = new Stage();
        stage.setTitle("Update Customer Information");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label custNoLabel = new Label("Customer Number:");
        TextField custNoField = new TextField();
        Label attributeLabel = new Label("Select attribute to update:");
        ComboBox<String> attributeComboBox = new ComboBox<>();
        attributeComboBox.getItems().addAll("Name", "Phone_NO", "City");
        Label newValueLabel = new Label("Enter new value:");
        TextField newValueField = new TextField();

        Button updateButton = new Button("Update Customer");
        updateButton.setOnAction(e -> {
            String custNo = custNoField.getText();
            String attributeName = attributeComboBox.getValue();
            String newValue = newValueField.getText();

            String query = "UPDATE CUSTOMER SET " + attributeName + " = ? WHERE CUST_NO = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setString(2, custNo);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated.");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(custNoLabel, custNoField, attributeLabel, attributeComboBox,
                newValueLabel, newValueField, updateButton);

        Scene scene = new Scene(root, 400, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void showAccountDetails() {
        Stage stage = new Stage();
        stage.setTitle("Account Details");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label accountNoLabel = new Label("Account Number:");
        TextField accountNoField = new TextField();

        Button showButton = new Button("Show Details");
        showButton.setOnAction(e -> {
            String accountNo = accountNoField.getText();
            // Retrieve account details from the database based on the account number
            String query = "SELECT * FROM ACCOUNT WHERE ACCOUNT_NO = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, accountNo);
                ResultSet resultSet = preparedStatement.executeQuery();
                root.getChildren().clear();
                root.getChildren().addAll(accountNoLabel, accountNoField, showButton);
                if (resultSet.next()) {
                    Label accountInfoLabel = new Label("Account Number: " + resultSet.getString("ACCOUNT_NO")
                            + "\nType: " + resultSet.getString("TYPE")
                            + "\nBalance: " + resultSet.getDouble("BALANCE")
                            + "\nBranch Code: " + resultSet.getString("BRANCH_CODE"));
                    root.getChildren().add(accountInfoLabel);
                } else {
                    Label notFoundLabel = new Label("Account not found.");
                    root.getChildren().add(notFoundLabel);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(accountNoLabel, accountNoField, showButton);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void depositMoney() {
        Stage stage = new Stage();
        stage.setTitle("Deposit Money");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label accountNoLabel = new Label("Account Number:");
        TextField accountNoField = new TextField();
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();

        Button depositButton = new Button("Deposit");
        depositButton.setOnAction(e -> {
            String accountNo = accountNoField.getText();
            double amount = Double.parseDouble(amountField.getText());
            // Update account balance in the database
            String query = "UPDATE ACCOUNT SET BALANCE = BALANCE + ? WHERE ACCOUNT_NO = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, amount);
                preparedStatement.setString(2, accountNo);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Deposit successful.");
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Deposit successful.");
                } else {
                    System.out.println("Failed to deposit money.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(accountNoLabel, accountNoField, amountLabel, amountField, depositButton);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void withdrawMoney() {
        Stage stage = new Stage();
        stage.setTitle("Withdraw Money");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label accountNoLabel = new Label("Account Number:");
        TextField accountNoField = new TextField();
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();

        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setOnAction(e -> {
            String accountNo = accountNoField.getText();
            double amount = Double.parseDouble(amountField.getText());
            // Check if sufficient balance and update account balance in the database
            String selectQuery = "SELECT BALANCE FROM ACCOUNT WHERE ACCOUNT_NO = ?";
            String updateQuery = "UPDATE ACCOUNT SET BALANCE = BALANCE - ? WHERE ACCOUNT_NO = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                selectStatement.setString(1, accountNo);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    double balance = resultSet.getDouble("BALANCE");
                    if (balance >= amount) {
                        updateStatement.setDouble(1, amount);
                        updateStatement.setString(2, accountNo);
                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Withdrawal successful.");
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Withdrawal successful.");
                        } else {
                            System.out.println("Failed to withdraw money.");
                        }
                    } else {
                        System.out.println("Insufficient balance.");
                        showAlert(Alert.AlertType.WARNING, "Warning", "Insufficient balance.");
                    }
                } else {
                    System.out.println("Account not found.");
                    showAlert(Alert.AlertType.WARNING, "Warning", "Account not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(accountNoLabel, accountNoField, amountLabel, amountField, withdrawButton);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showLoanDetails() {
        Stage stage = new Stage();
        stage.setTitle("Loan Details");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label custNoLabel = new Label("Customer Number:");
        TextField custNoField = new TextField();

        Button showButton = new Button("Show Details");
        showButton.setOnAction(e -> {
            String custNo = custNoField.getText();
            // Retrieve loan details from the database based on the customer number
            String query = "SELECT * FROM LOAN WHERE CUST_NO = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, custNo);
                ResultSet resultSet = preparedStatement.executeQuery();
                root.getChildren().clear();
                root.getChildren().addAll(custNoLabel, custNoField, showButton);
                if (resultSet.next()) {
                    Label loanInfoLabel = new Label("Loan Number: " + resultSet.getString("LOAN_NO")
                            + "\nLoan Amount: " + resultSet.getDouble("AMOUNT")
                            + "\nBranch Code: " + resultSet.getString("BRANCH_CODE"));
                    root.getChildren().add(loanInfoLabel);
                } else {
                    Label notFoundLabel = new Label("No loans found for the customer.");
                    root.getChildren().add(notFoundLabel);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(custNoLabel, custNoField, showButton);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public class Customer {
        private String custNo;
        private String name;
        private String phoneNo;
        private String city;

        public Customer(String custNo, String name, String phoneNo, String city) {
            this.custNo = custNo;
            this.name = name;
            this.phoneNo = phoneNo;
            this.city = city;
        }

        public String getCustNo() {
            return custNo;
        }

        public void setCustNo(String custNo) {
            this.custNo = custNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

}
