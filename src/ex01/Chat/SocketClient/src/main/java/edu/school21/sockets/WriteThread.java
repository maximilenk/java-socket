package edu.school21.sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class WriteThread extends Thread {
    private Socket socket;
    private BufferedWriter bufferedWriter;

    public WriteThread(Socket socket, BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        System.out.println("Start messaging");
        String message;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            message = scanner.nextLine().replace("\n","");
            try {
                bufferedWriter.write(message + "\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (Objects.equals(message, "exit")) {
                break;
            }
        }
    }
}
