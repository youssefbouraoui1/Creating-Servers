package net.Bouraoui.WebServeurSimple;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SendMessage {



    public SendMessage(Socket socket){

        BufferedReader inputStream = null;

        try{

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
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
        }

    }
}
