package net.Bouraoui.TP4.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TicTacToeClient extends JFrame {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 80;
    private char[][] board = new char[3][3];
    private char playerSymbol;
    private JButton[][] buttons = new JButton[3][3];
    private JLabel[][] labels = new JLabel[3][3];
    private PrintWriter out;
    private BufferedReader in;

    public TicTacToeClient() {
        setTitle("Tic Tac Toe");
        setSize(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40)); // Smaller font
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);

                labels[i][j] = new JLabel(); // Create label for each button
                labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                add(labels[i][j]); // Add label below the button
            }
        }

        setVisible(true);
        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            readServerMessages();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readServerMessages() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);

                    String[] parts = message.split(":");
                    if (parts.length > 1) {
                        int position = Integer.parseInt(parts[1].trim());
                        int row = position / 3;
                        int col = position % 3;
                        labels[row][col].setText(message);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading server message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void updateBoard(String message) {
        String[] parts = message.split(":");
        int position = Integer.parseInt(parts[1].trim());
        int row = position / 3;
        int col = position % 3;
        char symbol = (playerSymbol == 'X') ? 'X' : 'O';
        board[row][col] = symbol;
        buttons[row][col].setText(String.valueOf(symbol));
    }

    private void resetBoard() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                labels[i][j].setText(""); // Clear labels
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[row][col] == 0) {
                out.println(row * 3 + col);
                buttons[row][col].setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeClient::new);
    }
}
