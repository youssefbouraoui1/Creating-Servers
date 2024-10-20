package net.Bouraoui.TP4.SalonDeChat;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Server {
    static Map<String, Salon> salons = new HashMap<>();
    static List<Socket> clients = new ArrayList<Socket>();
    static Map<Socket, String> clientsnames = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        int port_d_ecoute = 80;
        ServerSocket listener = new ServerSocket(port_d_ecoute);
        try{
            while(true){
                Socket client = listener.accept();
                clients.add(client);
                //CountDownLatch latch = new CountDownLatch(1);
                Authentifier authentifier = new Authentifier(client, clientsnames, salons);
                //Thread auth = new Thread(new Authentifier(client,clientsnames,salons));
                Thread auth = new Thread(authentifier);
                auth.start();
                /*try {
                    auth.join();
                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }*/
                //latch.await();
                /*try{
                    String nomSalon = authentifier.getNomSalon();
                    Salon salon = salons.computeIfAbsent(nomSalon, k -> new Salon());
                    Thread t = new Thread(new Diffuser(client,salon,nomSalon,clientsnames));
                    t.start();
                } catch(Exception e){
                    throw new RuntimeException(e);
                }*/
            }
        } finally{
            listener.close();
        }
    }
}
