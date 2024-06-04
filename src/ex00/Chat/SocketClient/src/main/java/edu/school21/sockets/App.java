package edu.school21.sockets;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        int port = Integer.parseInt(args[0].replace("--server-port=", ""));
        try (Socket socket = new Socket("localhost", port);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("enter your email ");
            String email = scanner.nextLine();
            writer.write(email + "\n");
            writer.flush();
            System.out.println("enter your password ");
            String password = scanner.nextLine();
            writer.write(password + "\n");
            writer.flush();
            System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
