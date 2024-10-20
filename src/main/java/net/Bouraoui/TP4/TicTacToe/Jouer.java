package net.Bouraoui.TP4.TicTacToe;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Jouer implements Runnable{

    List<Socket> pair;
    private final char[] board = new char[9];
    private boolean isPlayer1Turn;


    public Jouer(List<Socket> pair) {
        this.pair = pair;
        initializeBoard();
    }

    @Override
    public void run() {

    }
    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = '-';
        }
    }

    private synchronized boolean checkWin(char symbol) {

        return (board[0] == symbol && board[1] == symbol && board[2] == symbol) ||
                (board[3] == symbol && board[4] == symbol && board[5] == symbol) ||
                (board[6] == symbol && board[7] == symbol && board[8] == symbol) ||
                (board[0] == symbol && board[3] == symbol && board[6] == symbol) ||
                (board[1] == symbol && board[4] == symbol && board[7] == symbol) ||
                (board[2] == symbol && board[5] == symbol && board[8] == symbol) ||
                (board[0] == symbol && board[4] == symbol && board[8] == symbol) ||
                (board[2] == symbol && board[4] == symbol && board[6] == symbol);
    }

    private synchronized void updateBoard(int position, char symbol) {
        if (position >= 0 && position < 9 && board[position] == '-') {
            board[position] = symbol;
        }
    }

    public void jouer(Socket joueur) throws IOException {
        int indice = pair.indexOf(joueur);
        char symbol = (indice == 0) ? 'X' : 'O';
        PrintWriter out = new PrintWriter(joueur.getOutputStream(), true);
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(joueur.getInputStream()));
        out.println();

        if (isPlayer1Turn && symbol == 'X' || !isPlayer1Turn && symbol == 'O') {
            out.println("C'est votre tour de jouer !");


            try {
                String inputLine = inputStream.readLine();
                int position = Integer.parseInt(inputLine);


                if (position < 0 || position > 8) {
                    out.println("Position invalide ! Veuillez entrer un nombre entre 0 et 8.");
                    return;
                }


                updateBoard(position, symbol);
                out.println("Mouvement effectué à la position: " + position);
                broadcastBoard();


                if (checkWin(symbol)) {
                    out.println("Félicitations ! Le joueur " + symbol + " a gagné !");
                    broadcastEndGame();
                    return;
                }


                isPlayer1Turn = !isPlayer1Turn;
            } catch (NumberFormatException e) {
                out.println("Entrée invalide ! Veuillez entrer un nombre entre 0 et 8.");
            }
        } else {
            out.println("Ce n'est pas votre tour !");
        }
    }

    private void broadcastBoard() {
        StringBuilder boardState = new StringBuilder("État du plateau: ");
        for (char c : board) {
            boardState.append(c).append(' ');
        }

        for (Socket player : pair) {
            try {
                PrintWriter playerOut = new PrintWriter(player.getOutputStream(), true);
                playerOut.println(boardState.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastEndGame() {
        for (Socket player : pair) {
            try {
                PrintWriter playerOut = new PrintWriter(player.getOutputStream(), true);
                playerOut.println("Le jeu est terminé !");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
