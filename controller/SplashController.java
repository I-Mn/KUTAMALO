package controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private VBox rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Start transparent
        rootPane.setOpacity(0);

        // Fade in
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        fadeIn.setOnFinished(e -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> checkSessionAndProceed());
            pause.play();
        });
        
        fadeIn.play();
    }

    private void checkSessionAndProceed() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.8), rootPane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            try {
                java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot().node("kutamalo");
                String savedUser = prefs.get("kutamalo_username", null);
                String savedPass = prefs.get("kutamalo_password", null);

                String nextView = "/view/LoginView.fxml";
                if (savedUser != null && savedPass != null) {
                    model.User user = database.DatabaseConnection.login(savedUser, savedPass);
                    if (user != null) {
                        model.Session.getInstance().setCurrentUser(user);
                        nextView = "/view/MainView.fxml";
                    }
                }

                Parent root = FXMLLoader.load(getClass().getResource(nextView));
                Stage stage = (Stage) rootPane.getScene().getWindow();
                
                // Keep the same window size but transition smoothly
                Scene currentScene = stage.getScene();
                currentScene.setRoot(root);
                
                // Add a small fade-in to the new root for ultra-smooth feeling
                root.setOpacity(0);
                FadeTransition rootFadeIn = new FadeTransition(Duration.seconds(0.5), root);
                rootFadeIn.setFromValue(0);
                rootFadeIn.setToValue(1);
                rootFadeIn.play();
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        fadeOut.play();
    }
}
