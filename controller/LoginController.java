package controller;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
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


    private void crossFade(VBox fadeOut, VBox fadeIn) {
        FadeTransition ftOut = new FadeTransition(Duration.millis(250), fadeOut);
        ftOut.setFromValue(1.0);
        ftOut.setToValue(0.0);
        ftOut.setOnFinished(e -> {
            fadeOut.setVisible(false);
            
            fadeIn.setOpacity(0.0);
            fadeIn.setVisible(true);
            FadeTransition ftIn = new FadeTransition(Duration.millis(250), fadeIn);
            ftIn.setFromValue(0.0);
            ftIn.setToValue(1.0);
            ftIn.play();
        });
        ftOut.play();
    }

    @FXML
    public void toggleToRegister(MouseEvent event) {
        crossFade(loginForm, registerForm);
        loginErrorLabel.setVisible(false);
        loginErrorLabel.setManaged(false);
    }

    @FXML
    public void toggleToLogin(MouseEvent event) {
        crossFade(registerForm, loginForm);
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
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError(registerErrorLabel, "Please enter a valid email format.", false);
            return;
        }

        if (!phone.matches("^\\+?\\d{10,15}$")) {
            showError(registerErrorLabel, "Please enter a valid phone number.", false);
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
            Parent newRoot = FXMLLoader.load(fxmlLocation);
            newRoot.setOpacity(0.0);
            
            Node sourceNode = (Node) event.getSource();
            Scene scene = sourceNode.getScene();
            Parent currentRoot = scene.getRoot();
            
            // Set scene background to dark so it doesn't flash white during transition
            scene.setFill(javafx.scene.paint.Color.web("#121212"));
            
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentRoot);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                scene.setRoot(newRoot);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
