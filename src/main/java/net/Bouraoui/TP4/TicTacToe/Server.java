package net.Bouraoui.TP4.TicTacToe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    static List<Socket> clients_en_attente = Collections.synchronizedList(new ArrayList<>());
    static Set<Socket> pairs= Collections.synchronizedSet(new HashSet<>());
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws IOException {
        int port_d_ecoute = 80;
        ServerSocket listener = new ServerSocket(port_d_ecoute);
        try{
            while(true){
                Socket client = listener.accept();
                clients_en_attente.add(client);
                Thread gdj = new Thread(new GestionDesJoueurs(clients_en_attente,client,pairs,lock));
                gdj.start();
            }
        }finally {
            listener.close();
        }
    }
}
