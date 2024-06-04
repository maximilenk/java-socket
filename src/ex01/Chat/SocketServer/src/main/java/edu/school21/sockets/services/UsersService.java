package edu.school21.sockets.services;


import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;

public interface UsersService {
    String signUp(String email, String password);

    boolean logIn(String email, String password);

    User findUserByUsername(String username);

    boolean addMessage(Message message);
}
