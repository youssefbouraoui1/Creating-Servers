package net.Bouraoui.TP4.SalonDeChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        int port = 80;
        Socket s = new Socket(hostname, port);


        new Thread(new Runnable() { public void run() {
            try{

                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String msg;
                while ((msg = br.readLine()) != null) {
                    System.out.println(msg);

                }
            }catch(IOException e){
                e.printStackTrace();
            }

        }}).start();

        Thread envoyer = new Thread(()->{
            try{
                OutputStream outp = s.getOutputStream();

                while(true){
                    Scanner scan = new Scanner(System.in);
                    String line = scan.nextLine();
                    try {
                        outp.write((line+"\n").getBytes());
                        outp.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        });
        envoyer.start();
    }
}
