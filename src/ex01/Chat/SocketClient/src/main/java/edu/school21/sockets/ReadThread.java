package edu.school21.sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ReadThread extends Thread {
    private Socket socket;
    private BufferedReader bufferedReader;

    public ReadThread(Socket socket, BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = bufferedReader.readLine();
                if (message.replaceAll("\n", "").equals("exit")) {
                    System.out.println("you have left the chat");
                    break;
                } else {
                    System.out.println(message.replaceAll("\n", ""));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
