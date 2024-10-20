package net.Bouraoui.TP4.TicTacToe;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class GestionDesJoueurs implements Runnable{
    List<Socket> clients_en_attente ;
    //PrintWriter out;
    Socket client;
    Set<Socket> pairs;
    private final Lock lock;

    public GestionDesJoueurs(List<Socket> clients_en_attente,Socket client,Set<Socket> pairs,Lock lock) {
        this.clients_en_attente = clients_en_attente;
        this.client = client;
        this.pairs = pairs;
        this.lock = lock;
    }

    public void run() {
        try {
            associer(this.clients_en_attente,this.client,this.pairs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void associer(List<Socket> clients_en_attente,Socket client,HashSet<Socket> pairs) throws IOException {

            OutputStream output = client.getOutputStream();
            PrintWriter out = new PrintWriter(output, true);
            while (clients_en_attente.size() == 0 || clients_en_attente.size() % 2 == 1 || !pairs.contains(client)) {
                out.println("En attendant pour un autre joueur de se connecter");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            List<Socket> pair = new ArrayList<>(2);
            pair.add(clients_en_attente.removeLast());
            pairs.add(pair.get(0));
            pair.add(clients_en_attente.removeLast());
            pairs.add(pair.get(1));

            out.println("L'autre joueur est connecté vous pouvez jouer maintenant");
            Thread joue = new Thread(new Jouer(pair));
            joue.start();

    }*/
    private void associer(List<Socket> clients_en_attente,Socket client,Set<Socket> pairs) throws IOException {
        OutputStream output = client.getOutputStream();
        PrintWriter out = new PrintWriter(output, true);

        while (true) {
            lock.lock();
            try {
                if (clients_en_attente.size() < 2 && !pairs.contains(client)) {
                    out.println("En attendant pour un autre joueur de se connecter");
                } else {
                    List<Socket> pair = new ArrayList<>(2);
                    pair.add(clients_en_attente.remove(clients_en_attente.size() - 1));
                    pair.add(clients_en_attente.remove(clients_en_attente.size() - 1));
                    pairs.add(pair.get(0));
                    pairs.add(pair.get(1));
                    OutputStream output1 = pair.get(0).getOutputStream();
                    PrintWriter out1 = new PrintWriter(output1, true);

                    OutputStream output2 = pair.get(1).getOutputStream();
                    PrintWriter out2 = new PrintWriter(output2, true);

                    out1.println("L'autre joueur est connecté vous pouvez jouer maintenant");
                    out2.println("L'autre joueur est connecté vous pouvez jouer maintenant");
                    new Thread(new Jouer(pair)).start();
                    break;
                }
            } finally {
                lock.unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
