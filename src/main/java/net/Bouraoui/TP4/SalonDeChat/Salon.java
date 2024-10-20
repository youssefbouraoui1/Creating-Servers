package net.Bouraoui.TP4.SalonDeChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Salon {
    private Set<Socket> clients = new HashSet<>();

    public synchronized void addClient(Socket socket) {
        if (clients.add(socket)) {
            System.out.println("Client connected: " + socket);
        } else {
            System.out.println("Client already connected: " + socket);
        }
    }

    public synchronized void removeClient(Socket socket) {
        if (clients.remove(socket)) {
            System.out.println("Client disconnected: " + socket);
        } else {
            System.out.println("Client not found: " + socket);
        }
    }

    public Set<Socket> getClients() {
        return clients;
    }

}