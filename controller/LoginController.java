package controller;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Session;
import model.User;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private VBox loginForm;
    @FXML private VBox registerForm;

    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Label loginErrorLabel;

    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private Label registerErrorLabel;
    @FXML private TextField registerEmailField;
    @FXML private TextField registerPhoneField;


    @FXML
    public void toggleToRegister(MouseEvent event) {
        loginForm.setVisible(false);
        registerForm.setVisible(true);
        loginErrorLabel.setVisible(false);
        loginErrorLabel.setManaged(false);
    }

    @FXML
    public void toggleToLogin(MouseEvent event) {
        registerForm.setVisible(false);
        loginForm.setVisible(true);
        registerErrorLabel.setVisible(false);
        registerErrorLabel.setManaged(false);
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError(loginErrorLabel, "Please enter username and password.", false);
            return;
        }

        User user = DatabaseConnection.login(username, password);
        if (user != null) {
            Session.getInstance().setCurrentUser(user);
            loadMainView(event);
        } else {
            showError(loginErrorLabel, "Invalid username or password.", false);
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String username = registerUsernameField.getText().trim();
        String password = registerPasswordField.getText();
        String email = registerEmailField.getText().trim();
        String phone = registerPhoneField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showError(registerErrorLabel, "Please fill in all fields.", false);
            return;
        }

        boolean success = DatabaseConnection.register(username, password, email, phone);
        if (success) {
            showError(registerErrorLabel, "Registration successful! Please login.", true);
            // Optionally auto-switch to login:
            // toggleToLogin(null);
            // loginUsernameField.setText(username);
            // loginPasswordField.setText("");
        } else {
            showError(registerErrorLabel, "Registration failed. Username may exist.", false);
        }
    }

    private void showError(Label label, String msg, boolean isSuccess) {
        label.setText(msg);
        if (isSuccess) {
            label.setStyle("-fx-text-fill: #4caf50;");
        } else {
            label.setStyle("-fx-text-fill: #ff4c4c;");
        }
        label.setVisible(true);
        label.setManaged(true);
    }

    private void loadMainView(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/view/MainView.fxml");
            if (fxmlLocation == null) {
                System.err.println("MainView.fxml not found!");
                return;
            }
            Parent root = FXMLLoader.load(fxmlLocation);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
