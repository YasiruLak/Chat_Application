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

    public ServerAppInitializer()
    {
        try {
            ServerSocket ss=new ServerSocket(6001);
            System.out.println ("Waiting for request");
            Socket s=ss.accept();
            System.out.println ("Connected With"+s.getInetAddress().toString());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

            String req=(String)ois.readObject();
            System.out.println (req);

            File f=new File(req);
            FileInputStream fin=new FileInputStream(f);

            int c;
            int sz=(int)f.length();
            byte b[]=new byte [sz];
            oos.writeObject(new Integer(sz));
            oos.flush();
            int j=0;
            while ((c = fin.read()) != -1) {

                b[j]=(byte)c;
                j++;
            }

            fin.close();
            oos.flush();
            oos.write(b,0,b.length);
            oos.flush();
            System.out.println ("Size "+sz);
            System.out.println ("buf size"+ss.getReceiveBufferSize());
            oos.writeObject(new String("Ok"));
            oos.flush();
            ss.close();
        }
        catch (Exception ex) {
            System.out.println ("Error"+ex);
        }
    }

    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(6001);
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

        ServerAppInitializer ob=new ServerAppInitializer();
    }
}
