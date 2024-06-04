package edu.school21.sockets;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        if (args.length == 1) {
            int port = Integer.parseInt(args[0].replace("--server-port=", ""));
            try (Socket socket = new Socket("localhost", port);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
                menu();

                Scanner scanner = new Scanner(System.in);
                int choose = Integer.parseInt(scanner.nextLine());
                if (!(choose == 2 || choose == 1)) {
                    throw new RuntimeException("wrong input");
                }
                writer.write(choose);
                writer.flush();

                System.out.println("your login");
                String email = scanner.nextLine().replace("\n", "");
                writer.write(email + "\n");
                writer.flush();

                System.out.println("your password");
                String password = scanner.nextLine().replace("\n", "");
                writer.write(password + "\n");
                writer.flush();

                String message = bufferedReader.readLine();
                if (message.isEmpty()) {
                    System.out.println("start working");
                    ReadThread readThread = new ReadThread(socket, bufferedReader);
                    WriteThread writeThread = new WriteThread(socket, writer);

                    writeThread.join();
                    readThread.join();
                } else {
                    System.out.println(message);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("wrong args");
        }
    }

    public static void menu() {
        System.out.println("1.sign up");
        System.out.println("2.login");

    }
}
