package net.Bouraoui.WebServeurSimple;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    public static void main(String[] args) throws IOException {

        int port_d_ecoute = 8000;
        ServerSocket listener = new ServerSocket(port_d_ecoute);
        try{
            while(true){
                Socket client = listener.accept();
                try{
                    new SendMessage(client);
                }finally {
                    client.close();
                }
            }
        }finally {
            listener.close();
        }
    }
}
