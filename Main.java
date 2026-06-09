import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Mengambil file desain MainView.fxml yang ada di folder proyekmu
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));

        primaryStage.setTitle("Aplikasi KUTAMALO");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}