package net.Bouraoui.ServeurDeChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    static List<Socket> clients = new ArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        int port_d_ecoute = 8080;
        ServerSocket listener = new ServerSocket(port_d_ecoute);
        try{
            while(true){
                Socket client = listener.accept();
                clients.add(client);
                try{
                    Thread t = new Thread(new Diffuser(client));
                    t.start();
                } catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }finally{
            listener.close();
        }
    }
}
