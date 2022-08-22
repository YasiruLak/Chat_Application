import controller.ClientController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : Yasiru Dahanayaka
 * @name : Chat Application
 * @date : 8/14/2022
 * @month : 08
 * @year : 2022
 * @since : 0.1.0
 **/
public class ServerAppInitializer {

    private static final ArrayList<ClientController> clients = new ArrayList<ClientController>();


    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(7000);
            while(true) {
                System.out.println("Waiting for clients...");
                socket = serverSocket.accept();
                System.out.println("Connected");
                ClientController clientThread = new ClientController(socket, clients);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
