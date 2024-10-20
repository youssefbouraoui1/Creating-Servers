package net.Bouraoui.WebServerSimpleUsingThreads;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMessage implements Runnable {

    private Socket socket;

    public SendMessage(Socket socket){
        this.socket = socket;
    }

    public void run(){
        SendMessage();
    }

    public void SendMessage(){
        BufferedReader inputStream = null;

        try{

            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            //String corps = "<HTML><TITLE>Mon serveur</TITLE>Cette page a ete envoyee par mon <B>Serveur </B></HTML>";
            inputStream = new BufferedReader(new FileReader("D:\\Sockets\\src\\main\\java\\net\\Bouraoui\\WebServeurSimple\\corps.txt"));
            String l;
            StringBuilder corps = new StringBuilder();
            while((l=inputStream.readLine())!=null){
                corps.append(l);
            }
            out.println("HTTP/1.0 200 OK\n\n" + corps.toString());
            out.flush();
        }catch(IOException e){
            throw new RuntimeException(e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
