import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author : Yasiru Dahanayaka
 * @name : Chat Application
 * @date : 8/10/2022
 * @month : 08
 * @year : 2022
 * @since : 0.1.0
 **/
public class ClientAppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/LogInForm.fxml"))));
        primaryStage.setTitle("Play Tech Chat");
        primaryStage.show();
    }
}
