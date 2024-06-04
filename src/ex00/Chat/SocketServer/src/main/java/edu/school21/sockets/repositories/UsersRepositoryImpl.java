package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


@Component("usersRepositoryJdbcTemplateImpl")
public class UsersRepositoryImpl implements UsersRepository<User> {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(@Qualifier("hikariDataSource") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(Long id) {
        String query = "SELECT * FROM users WHERE id = :id";
        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource()
                        .addValue("id", id),
                new BeanPropertyRowMapper<>(User.class)));
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return namedParameterJdbcTemplate.query(query, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void save(User entity) {
        String query = "INSERT INTO users (email, password) VALUES (:email, :password)";
        namedParameterJdbcTemplate.update(query, new MapSqlParameterSource()
                .addValue("email", entity.getEmail())
                .addValue("password", entity.getPassword()));
    }

    @Override
    public void update(User entity) {
        String query = "UPDATE users SET email = :email, password = :password WHERE id = :id";
        namedParameterJdbcTemplate.update(query, new MapSqlParameterSource()
                .addValue("id", entity.getId())
                .addValue("email", entity.getEmail())
                .addValue("password", entity.getPassword()));
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM users WHERE id = :id;";
        namedParameterJdbcTemplate.update(query, new MapSqlParameterSource()
                .addValue("id", id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = :email";
        try {
            User user = namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource()
                            .addValue("email", email),
                    new BeanPropertyRowMapper<>(User.class));
            assert user != null;
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
