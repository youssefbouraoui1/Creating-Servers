package net.Bouraoui.TP4.SalonDeChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Diffuser implements Runnable {
    private Socket socket;
    private Salon salon;
    private String nomSalon;
    private Map<Socket, String> clientsnames;

    Diffuser(Socket socket, Salon salon, String nomSalon, Map<Socket, String> clientsnames) {
        this.socket = socket;
        this.salon = salon;
        this.nomSalon = nomSalon;
        this.clientsnames = clientsnames;
    }

    @Override
    public void run() {
        diffuserMessage();
    }

    public void diffuserMessage() {
        try (BufferedReader inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            String msg;
            String clientName = clientsnames.get(this.socket);

            while (true) {
                msg = inputStream.readLine();

                if (msg == null) {
                    System.out.println(clientName + " has disconnected.");
                    break;
                }

                String formattedMessage = "-"+clientName + ": " + msg;
                String filepath = "D://SalonChat//" + nomSalon + ".txt";


                new Thread(new ConversationSaver(filepath, formattedMessage)).start();

                Set<Socket> clients = salon.getClients();
                for (Socket clientSocket : clients) {
                    if (clientSocket != this.socket) {
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println(formattedMessage);
                        out.flush();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error in diffuserMessage: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Socket closed for client.");
            }
        } catch (IOException e) {
            System.err.println("Failed to close socket: " + e.getMessage());
        }
    }
}
