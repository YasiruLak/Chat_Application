package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.input.MouseEvent;
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
import java.nio.charset.StandardCharsets;
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
    public JFXButton btnImage;
    public boolean saveControl = false;
    public AnchorPane emojiBox;
    public ImageView emoji1;
    public ImageView emoji2;
    public ImageView emoji3;
    public ImageView emoji4;
    public ImageView emoji5;
    public ImageView emoji6;
    public FontAwesomeIconView imgEmoji;

    BufferedReader reader;
    BufferedWriter writer;
//    PrintWriter printWriter;
    Socket socket=null;
    String []ePath=new String[6];

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

        textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#0abde3;" + "-fx-background-radius:10px");

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
            this.socket = new Socket("localhost", 7000);
            //System.out.println("Socket is connected with server!");
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.writer.write(userName);
            this.writer.newLine();
            this.writer.flush();

            {
                for (int i = 0; i < ePath.length; i++) {
                    ePath[i] = "assests/emojis/" + (i + 1) + ".png";
                    System.out.println(ePath[i]);

                }
                System.out.println("Emojis path set to array");
            }


            new Thread(() -> {
                String msg;
                try {
                    while (true) {
                        msg = reader.readLine();
                        String[] tokens = msg.split(" ");
                        String cmd = tokens[0];
                        StringBuilder fulMsg = new StringBuilder();
                        for (int i = 1; i < tokens.length; i++) {
                            fulMsg.append(tokens[i]);
                        }

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
                            imageView.setFitWidth(100);
                            imageView.setPreserveRatio(true);

                            hBox.getChildren().add(textFlow1);
                            hBox.getChildren().add(imageView);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    messageText.getChildren().add(hBox);
                                }
                            });

                        }else if (fulMsg.toString().startsWith("assests/emojis/") ) {
                            HBox hBox = new HBox();
                            System.out.println("Emoji path "+fulMsg);
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 10, 5, 5));
                            Text text = new Text(cmd + " ");
                            ImageView imageView = new ImageView();
                            Image image = new Image(String.valueOf(fulMsg));
                            imageView.setImage(image);
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            TextFlow textFlow = new TextFlow(text, imageView);
                            VBox vBox = new VBox(textFlow);
                            vBox.setAlignment(Pos.CENTER_LEFT);
                            vBox.setPadding(new Insets(5, 10, 5, 5));
                            hBox.getChildren().add(vBox);

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
        textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#0abde3;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1, 1));


        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(new Image(new File(file.getPath()).toURI().toString()));
        //Setting the image view parameters
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        hBox.getChildren().add(textFlow);
        hBox.getChildren().add(imageView);

        messageText.getChildren().add(hBox);
    }

    public void sendEmojiOnMouseClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            switch (icon.getId()) {
                case "emoji1":
                    byte[] emojiBytes1 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81};
                    String emojiAsString1 = new String(emojiBytes1, StandardCharsets.UTF_8);
                    if (txtType.getText().equalsIgnoreCase("") || txtType.getText().equalsIgnoreCase(null)) {
                        ImageView imageView = new ImageView();
                        Image image = new Image(ePath[0]);
                        imageView.setImage(image);
                        imageView.setFitWidth(35);
                        imageView.setFitHeight(35);
                        VBox vBox = new VBox(imageView);
                        vBox.setAlignment(Pos.CENTER_RIGHT);
                        vBox.setPadding(new Insets(5, 10, 5, 5));
                        messageText.getChildren().add(vBox);
                        writer.write(userName + ": " + ePath[0]);
                        emojiBox.setVisible(false);
                        imgEmoji.setVisible(true);
                    } else {
                        txtType.appendText(emojiAsString1);

                    }
                    break;
                case "emoji2":
                    byte[] emojiBytes2 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x82};
                    String emojiAsString2 = new String(emojiBytes2, StandardCharsets.UTF_8);

                    if (txtType.getText().equalsIgnoreCase("") || txtType.getText().equalsIgnoreCase(null)) {
                        ImageView imageView = new ImageView();
                        Image image = new Image(ePath[1]);
                        imageView.setImage(image);
                        imageView.setFitWidth(35);
                        imageView.setFitHeight(35);
                        VBox vBox = new VBox(imageView);
                        vBox.setAlignment(Pos.CENTER_RIGHT);
                        vBox.setPadding(new Insets(5, 10, 5, 5));
                        messageText.getChildren().add(vBox);
                        writer.write(userName + ": " + ePath[1]);
                        emojiBox.setVisible(false);
                        imgEmoji.setVisible(true);
                    } else {
                        txtType.appendText(emojiAsString2);
                    }
                    break;
                case "emoji3":
                    byte[] emojiBytes3 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83};
                    String emojiAsString3 = new String(emojiBytes3, StandardCharsets.UTF_8);

                    if (txtType.getText().equalsIgnoreCase("") || txtType.getText().equalsIgnoreCase(null)) {
                        ImageView imageView = new ImageView();
                        Image image = new Image(ePath[2]);
                        imageView.setImage(image);
                        imageView.setFitWidth(35);
                        imageView.setFitHeight(35);
                        VBox vBox = new VBox(imageView);
                        vBox.setAlignment(Pos.CENTER_RIGHT);
                        vBox.setPadding(new Insets(5, 10, 5, 5));
                        messageText.getChildren().add(vBox);
                        writer.write(userName + ": " + ePath[2]);
                        emojiBox.setVisible(false);
                        imgEmoji.setVisible(true);
                    } else {
                        txtType.appendText(emojiAsString3);
                    }
                    break;
                case "emoji4":
                    byte[] emojiBytes4 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x84};
                    String emojiAsString4 = new String(emojiBytes4, StandardCharsets.UTF_8);

                    if (txtType.getText().equalsIgnoreCase("") || txtType.getText().equalsIgnoreCase(null)) {
                        ImageView imageView = new ImageView();
                        Image image = new Image(ePath[3]);
                        imageView.setImage(image);
                        imageView.setFitWidth(35);
                        imageView.setFitHeight(35);
                        VBox vBox = new VBox(imageView);
                        vBox.setAlignment(Pos.CENTER_RIGHT);
                        vBox.setPadding(new Insets(5, 10, 5, 5));
                        messageText.getChildren().add(vBox);
                        writer.write(userName + ": " + ePath[3]);
                        emojiBox.setVisible(false);
                        imgEmoji.setVisible(true);
                    } else {
                        txtType.appendText(emojiAsString4);
                    }
                    break;
                case "emoji5":
                    byte[] emojiBytes5 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x85};
                    String emojiAsString5 = new String(emojiBytes5, StandardCharsets.UTF_8);
                    if (txtType.getText().equalsIgnoreCase("") || txtType.getText().equalsIgnoreCase(null)) {
                        ImageView imageView = new ImageView();
                        Image image = new Image(ePath[4]);
                        imageView.setImage(image);
                        imageView.setFitWidth(35);
                        imageView.setFitHeight(35);
                        VBox vBox = new VBox(imageView);
                        vBox.setAlignment(Pos.CENTER_RIGHT);
                        vBox.setPadding(new Insets(5, 10, 5, 5));
                        messageText.getChildren().add(vBox);
                        writer.write(userName + ": " + ePath[4]);
                        emojiBox.setVisible(false);
                        imgEmoji.setVisible(true);
                    } else {
                        txtType.appendText(emojiAsString5);
                    }

                    break;
                case "emoji6":
                    byte[] emojiBytes6 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x86};
                    String emojiAsString6 = new String(emojiBytes6, StandardCharsets.UTF_8);
                    if (txtType.getText().equalsIgnoreCase("") || txtType.getText().equalsIgnoreCase(null)) {
                        ImageView imageView = new ImageView();
                        Image image = new Image(ePath[5]);
                        imageView.setImage(image);
                        imageView.setFitWidth(35);
                        imageView.setFitHeight(35);
                        VBox vBox = new VBox(imageView);
                        vBox.setAlignment(Pos.CENTER_RIGHT);
                        vBox.setPadding(new Insets(5, 10, 5, 5));
                        messageText.getChildren().add(vBox);
                        writer.write(userName + ": " + ePath[5]);
                        emojiBox.setVisible(false);
                        imgEmoji.setVisible(true);
                    } else {
                        txtType.appendText(emojiAsString6);
                    }
                    break;
            }
        }
    }

    public void chooseEmojiOnAction(MouseEvent mouseEvent) {
        if (emojiBox.isVisible()){
            emojiBox.setVisible(false);
        } else {
            emojiBox.setVisible(true);
        }
    }
}
