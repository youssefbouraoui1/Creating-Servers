package net.Bouraoui.TP4.SalonDeChat;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Authentifier implements Runnable{

    Socket client;
    private Map<Socket, String> clientsnames;
    private Map<String, Salon> salons;
    String nomSalon;
   // private final CountDownLatch latch;

    public Authentifier(Socket client,Map<Socket, String> clientsnames,Map<String, Salon> salons) {
        this.client = client;
        this.salons = salons;
        this.clientsnames = clientsnames;
        //this.latch = latch;
    }

    public void run(){


            authentifier(this.client);

    }

    public void authentifier(Socket client) {
        BufferedReader inputStream = null;
        try {
            //InputStream input = client.getInputStream();
            //BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream output = client.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            //Scanner scanner = new Scanner(input);


            writer.println("-Tapez votre nom:");
            String nomClient = inputStream.readLine();
            //String nomClient = scanner.nextLine();
            writer.println("-welcome "+nomClient);
            clientsnames.put(client,nomClient);
            while(true) {
                writer.println("-Tapez C pour créer un Salon et R pour rejoindre un salon");
                String reponse = inputStream.readLine();
                if (reponse.equals("C")) {
                    writer.println("-Veuillez entrer le nom du Salon à Rejoindre");
                    String nomSalon = inputStream.readLine();
                    createSalon(nomSalon, writer);
                    setNomSalon(nomSalon);
                    Salon salon = salons.computeIfAbsent(nomSalon, k -> new Salon());
                    salon.addClient(client);
                    break;
                } else if (reponse.equals("R")) {
                    writer.println("-Veuillez entrer le nom du Salon à rejoindre");
                    String nomSalon = inputStream.readLine();
                    recherchSalon(nomSalon,writer);
                    setNomSalon(nomSalon);
                    Salon salon = salons.computeIfAbsent(nomSalon, k -> new Salon());
                    salon.addClient(client);
                    break;
                } else {
                    writer.println("-Veuillez entrez une réponse valide");
                }


            }
            String nomSalon = this.getNomSalon();
            Salon salon = salons.computeIfAbsent(nomSalon, k -> new Salon());
            Thread t = new Thread(new Diffuser(client,salon,nomSalon,clientsnames));
            t.start();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public void createSalon(String nomSalon,PrintWriter writer) {
        String filepath = "D://SalonChat//" + nomSalon + ".txt";
        File file = new File(filepath);
        File salons = new File("D://SalonChat//Salons");

        try {
            if (file.exists()) {
                writer.println("Salon already exists");
            } else {
                if (file.createNewFile()) {
                    try (FileWriter salonsWriter = new FileWriter(salons, true);
                         PrintWriter salonWriter = new PrintWriter(salonsWriter)) {
                        salonWriter.println(nomSalon);

                    }
                    writer.println("Salon " + nomSalon + " created succesfuly you can chat now");
                } else {
                    writer.println("Salon creation failed");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void recherchSalon(String nomSalon,PrintWriter writer){
            String filepath = "D://SalonChat//"+nomSalon+".txt";
            File file = new File(filepath);
            if(file.exists()){
                Salon salon = salons.computeIfAbsent(nomSalon, k -> new Salon());
                salon.addClient(client);
                try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                    String line;
                    writer.println("You are now in the salon " + nomSalon + ". Here is the conversation history:");


                    while ((line = fileReader.readLine()) != null) {
                        writer.println(line);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer.println("Write a message");

            }else{
                writer.println("This salon does not exist");
            }
        }

    public String getNomSalon(){
        return nomSalon;
    }
    public void setNomSalon(String nomSalon){
        this.nomSalon = nomSalon;
    }

}
