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
        URL fxmlLocation = getClass().getResource("/view/LoginView.fxml");
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
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}