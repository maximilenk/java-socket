package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessageRepository;
import edu.school21.sockets.repositories.MessageRepositoryImpl;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersRepository<User> usersRepository;

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepositoryJdbcTemplateImpl") UsersRepository<User> usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public String signUp(String email, String password) {
        if (usersRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("email is used by another user");
        }
        User user = new User(1L, email,  passwordEncoder.encode(password));
        usersRepository.save(user);
        return user.getPassword();
    }

    @Override
    public boolean logIn(String email, String password) {
        boolean success = false;
        if (usersRepository.findByEmail(email).isPresent()) {
            User u = usersRepository.findByEmail(email).get();
            success = passwordEncoder.matches(password, u.getPassword());
        }
        return success;
    }

    @Override
    public boolean addMessage(Message message) {
        if (usersRepository.findById(message.getUserId()).isPresent()) {
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> user = usersRepository.findByEmail(username);
        return user.orElseGet(() -> new User(10L, "Default", "Default"));
    }
}
