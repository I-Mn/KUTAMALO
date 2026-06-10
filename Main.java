import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Check remember me
        java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot().node("kutamalo");
        String savedUser = prefs.get("kutamalo_username", null);
        String savedPass = prefs.get("kutamalo_password", null);
        
        String startView = "/view/LoginView.fxml";
        if (savedUser != null && savedPass != null) {
            model.User user = database.DatabaseConnection.login(savedUser, savedPass);
            if (user != null) {
                model.Session.getInstance().setCurrentUser(user);
                startView = "/view/MainView.fxml";
            }
        }

        URL fxmlLocation = getClass().getResource(startView);
        if (fxmlLocation == null) {
            System.err.println("MainView.fxml tidak ditemukan!");
            System.exit(1);
        }

        // Load custom fonts programmatically using FileInputStream to bypass classpath
        // bin directory issues
        try {
            Font.loadFont(new java.io.FileInputStream("view/fonts/Outfit-Regular.ttf"), 14);
            Font.loadFont(new java.io.FileInputStream("view/fonts/Outfit-Bold.ttf"), 14);
        } catch (Exception e) {
            System.err.println("Gagal memuat font lokal: " + e.getMessage());
        }
        Parent root = FXMLLoader.load(fxmlLocation);

        primaryStage.setTitle("Kutamalo - Catat Uang Saat Kurs Tak Masuk Logika");
        try {
            primaryStage.getIcons()
                    .add(new javafx.scene.image.Image(new java.io.FileInputStream("view/images/app_icon.png")));
        } catch (Exception e) {
            System.err.println("Gagal memuat icon aplikasi: " + e.getMessage());
        }
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
