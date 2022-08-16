package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
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
public class ClientFormController{
    public static String userName;
    public AnchorPane clientMainContext;
    public Text lblClientName;
    public JFXButton btnSend;
    public JFXTextArea txtChatType;
    public JFXTextField txtType;
    public ComboBox<String> cmbInfo;
    public ScrollPane scrollPane;
    public VBox messageText;
    public JFXButton btnEmoji;
    public JFXButton btnImage;
    public boolean saveControl = false;

    BufferedReader reader;
    BufferedWriter writer;
    Socket socket=null;

    public void setData(String name) {
        lblClientName.setText(name);
    }

    public void sendMsgOnAction(ActionEvent actionEvent) throws IOException {
        String msg = txtType.getText();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text("Me: " + msg);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#8b49d2;" + "-fx-background-radius:10px");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1, 1));
        hBox.getChildren().add(textFlow);
        messageText.getChildren().add(hBox);

        writer.write(userName + " : " + msg);
        writer.newLine();
        writer.flush();
        txtType.clear();
    }


    public void initialize() {
        try {
            this.socket = new Socket("localhost", 6000);
            //System.out.println("Socket is connected with server!");
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.writer.write(userName);
            this.writer.newLine();
            this.writer.flush();

            new Thread(() -> {
                String msg;
                try {
                    while (true) {
                        msg = reader.readLine();
//                        System.out.println("Message :"+msg);

                        if (msg.startsWith("IMG")) {

                            String replace = msg.replace("IMG", " ");
                            String[] split = replace.split("=");

                            System.out.println(split[0]);
                            System.out.println(split[1]);

                            HBox hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 5, 5, 10));

                            Text text1 = new Text(split[0] + " : ");
                            TextFlow textFlow1 = new TextFlow(text1);
                            textFlow1.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#8b49d2;");
                            textFlow1.setPadding(new Insets(5, 10, 5, 10));
                            text1.setFill(Color.color(1, 1, 1, 1));

                            ImageView imageView = new ImageView();
                            //Setting image to the image view
                            imageView.setImage(new Image(new File(split[1]).toURI().toString()));
                            //Setting the image view parameters
                            imageView.setFitWidth(300);
                            imageView.setPreserveRatio(true);

                            hBox.getChildren().add(textFlow1);
                            hBox.getChildren().add(imageView);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    messageText.getChildren().add(hBox);
                                }
                            });

                        }else {
                            HBox hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 5, 5, 10));

                            Text text = new Text(msg);
                            TextFlow textFlow = new TextFlow(text);

                            textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#8b49d2;" + "-fx-background-radius:10px");

                            textFlow.setPadding(new Insets(5, 10, 5, 10));
                            text.setFill(Color.color(1, 1, 1, 1));
                            hBox.getChildren().add(textFlow);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    messageText.getChildren().add(hBox);
                                }
                            });
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendEmojiOnAction(ActionEvent actionEvent) {
    }

    public void sendImageOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Image");
        File file = fileChooser.showOpenDialog(null);

        writer.write("IMG" + userName + " =" + file.getPath());
        writer.newLine();
        writer.flush();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text("Me : ");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#cf8bf6;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1, 1));


        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(new Image(new File(file.getPath()).toURI().toString()));
        //Setting the image view parameters
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        hBox.getChildren().add(textFlow);
        hBox.getChildren().add(imageView);

        messageText.getChildren().add(hBox);
    }
}
