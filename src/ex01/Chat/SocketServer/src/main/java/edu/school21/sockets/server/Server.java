package edu.school21.sockets.server;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class Server {
    @Autowired
    private UsersService usersService;
    private ServerSocket serverSocket;

    private BufferedReader in;
    private BufferedWriter out;
    private ArrayList<ThreadForUser> list = new ArrayList<>();

    public void startWork(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            int loginOrSignup = in.read();
            String email = in.readLine();
            String password = in.readLine();
            String result = "";
            if (loginOrSignup == 1) {
                try {
                    usersService.signUp(email, password);
                } catch (RuntimeException e) {
                    result = e.getMessage();
                }
            } else {
                if (!usersService.logIn(email, password)) {
                    result = "incorrect username or password";
                }
            }
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            out.write(result + "\n");
            out.flush();
            if (result.isEmpty()) {
                User user = usersService.findUserByUsername(email);
                list.add(new ThreadForUser(clientSocket, user));
                sendMessage(clientSocket);
            }
        }
    }

    public void sendMessage(Socket socket) throws IOException {
        BufferedWriter out
                = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write("Hello from server\n");
        out.flush();
    }


    public class ThreadForUser extends Thread {
        private User user;
        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;

        public ThreadForUser(Socket socket, User user) throws IOException {
            this.user = user;
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            start();
        }

        @Override
        public void run() {

            String word;
            try {

                while (true) {
                    word = in.readLine();
                    if (word != null) {
                        word = word.replace("\n", "");
                        if (!word.isEmpty()) {
                            if (Objects.equals(word, "exit")) {
                                list.remove(this);
                                send("exit");
                                sendToEveryOne(user.getEmail() + " has left the chat");
//                                for (ThreadForUser tfu : list) {
//                                    if (!tfu.user.equals(user)) {
//                                        tfu.send(user.getEmail() + " has left the chat");
//                                    }
//                                }
                                break;
                            }
                            usersService.addMessage(new Message(word, LocalDateTime.now(), 1L, user.getId()));
                            sendToEveryOne(user.getEmail() + ": " + word);
//                            for (ThreadForUser tfu : list) {
//                                if (!tfu.user.equals(user)) {
//                                    tfu.send(user.getEmail() + ": " + word);
//                                }
//                            }
                        }
                    }
                }

            } catch (IOException e) {
            }
        }

        public void sendToEveryOne(String message) throws IOException {
            for (ThreadForUser tfu : list) {
                if (!tfu.user.equals(user)) {
                    tfu.send(message);
                }
            }
        }


        public void send(String message) {
            try {
                if (message != null) {
                    message = message.replace(("\n"), (""));
                }
                out.write(message + "\n");
                out.flush();
            } catch (IOException ignored) {}
        }
    }
}
