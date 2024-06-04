package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class UsersServiceImpl implements UsersService {

    UsersRepository<User> usersRepository;

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
}
