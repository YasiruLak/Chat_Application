package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author : Yasiru Dahanayaka
 * @name : Chat Application
 * @date : 8/10/2022
 * @month : 08
 * @year : 2022
 * @since : 0.1.0
 **/
public class ClientFormController extends Thread implements Initializable {
    public AnchorPane clientMainContext;
    public Text lblClientName;
    public JFXButton btnSend;
    public JFXTextArea txtChatType;
    
    public JFXTextField txtType;
    public ComboBox<String> cmbInfo;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket=null;

    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    java.util.Date date = null;

    public void setData(String name) {
        lblClientName.setText(name);
    }

    public void sendMsgOnAction(ActionEvent actionEvent) {
        String msg = txtType.getText();
        writer.println(lblClientName.getText() + ": " + msg);
        txtChatType.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtChatType.appendText("Me: " + msg + "\n\n");
        txtType.setText("");
        if(msg.equalsIgnoreCase("Bye")) {
            System.exit(0);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fulmsg = new StringBuilder();
                for(int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println(fulmsg);
                if (cmd.equalsIgnoreCase(lblClientName.getText() + ":")) {
                    continue;
                }else if(fulmsg.toString().equalsIgnoreCase("Bye")) {
                    break;
                }
                txtChatType.appendText(msg + "\n\n");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbInfo.getItems().addAll(
                "Profile",
                "Logout"
        );

        cmbInfo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                if (newValue.equals("Logout")){
                    Stage window = (Stage) clientMainContext.getScene().getWindow();
                    try {
                        window.setScene(new Scene( FXMLLoader.load(getClass().getResource("../view/LogInForm.fxml"))));
                        reader.close();
                        writer.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            socket = new Socket("localhost", 6001);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
