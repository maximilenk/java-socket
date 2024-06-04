package edu.school21.sockets.server;

import edu.school21.sockets.models.User;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

@Component
public class Server {
    @Autowired
    private UsersService usersService;
    private Socket clientSocket;
    private ServerSocket serverSocket;

    public void startWork(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        System.out.println("connection set");
        registration();
    }

    public void registration() throws IOException {
        BufferedReader in
                = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String username = in.readLine();
        String password = in.readLine();
        try {
            usersService.signUp(username, password);
            sendMessage("User with username " + username + " successfully registered");
        } catch (RuntimeException e) {
            sendMessage("User with username " + username + " exists");
        }
    }

    public void sendMessage(String s) throws IOException {
        BufferedWriter out
                = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(s + "\n");
        out.flush();
    }
}
