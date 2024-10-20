package net.Bouraoui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        int port = 8189;
        Socket s ;
        s=new Socket(hostname,port);
        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String msg;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
        }finally{
            s.close();
        }
    }
}
