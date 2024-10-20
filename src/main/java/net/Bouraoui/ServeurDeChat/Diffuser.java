package net.Bouraoui.ServeurDeChat;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Diffuser implements Runnable{


    Socket socket;

    Diffuser(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        diffuserMessage();
    }

    public void diffuserMessage(){
        BufferedReader inputStream = null;
        try{
            //PrintWriter outputStream = new PrintWriter(this.socket.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String msg;

            while(true) {
                msg = inputStream.readLine();
                for (Socket socket : Serveur.clients) {
                    if (socket != this.socket) {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(msg);
                        out.flush();
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
