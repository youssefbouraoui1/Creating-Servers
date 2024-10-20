package net.Bouraoui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SendProverb {
    List<String> proverbs = new ArrayList<>();
    Random rand = new Random();
    public SendProverb(Socket socket){
        proverbs.add("flouss ki rouz");
        proverbs.add("drb l7did ma7do skhon");
        proverbs.add("alf tkhmima w tkhmima wla drbha blm9ss");
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(proverbs.get(rand.nextInt(3)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
