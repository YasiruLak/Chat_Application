package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author : Yasiru Dahanayaka
 * @name : Chat Application
 * @date : 8/14/2022
 * @month : 08
 * @year : 2022
 * @since : 0.1.0
 **/
public class LogInFormController {
    public JFXTextField txtUserName;
    public JFXButton btnLogIn;
    public Label lblError;
    public AnchorPane loginContext;

    public void joinChatOnAction(ActionEvent actionEvent) throws IOException {
        if (txtUserName.getText().equals("")){
            lblError.setText("All fields are required.!");
            lblError.setStyle("-fx-text-fill: red");
        }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ClientForm.fxml"));
            Parent load = loader.load();
            ClientFormController controller=loader.<ClientFormController>getController();
            controller.setData(txtUserName.getText());
            Stage window = (Stage) loginContext.getScene().getWindow();
            window.setScene(new Scene(load));
        }
    }
}
