package controller;

import java.io.*;
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
public class ClientController extends Thread{

    private ArrayList<ClientController> clients;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private String username;

    public ClientController(Socket socket, ArrayList<ClientController> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while (socket.isConnected()) {
                msg = reader.readLine();
                for (ClientController cl : clients) {
                    if (!cl.username.equals(username)){
                        cl.writer.write(msg);
                        cl.writer.newLine();
                        cl.writer.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
