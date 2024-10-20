package net.Bouraoui.TP4.SalonDeChat;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ConversationSaver implements Runnable {
    private String logFile;
    private String message;

    public ConversationSaver(String logFile, String message) {
        this.logFile = logFile;
        this.message = message;
    }

    @Override
    public void run() {
        logMessage();
    }

    private void logMessage() {
        try (FileWriter fw = new FileWriter(logFile, true);
             PrintWriter out = new PrintWriter(fw)) {
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
