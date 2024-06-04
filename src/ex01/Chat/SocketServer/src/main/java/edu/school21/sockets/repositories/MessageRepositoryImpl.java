package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class MessageRepositoryImpl implements MessageRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public MessageRepositoryImpl(@Qualifier("hikariDataSource") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Message> findById(Long id) {
        String query = "SELECT * FROM messages WHERE id = :id";
        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource()
                        .addValue("id", id),
                new BeanPropertyRowMapper<>(Message.class)));
    }

    @Override
    public List<Message> findAll() {
        String query = "SELECT * FROM messages";
        return namedParameterJdbcTemplate.query(query, new BeanPropertyRowMapper<>(Message.class));
    }

    @Override
    public void save(Message entity) {
        String query = "INSERT INTO messages (text, date, sender) VALUES (:text, :date, :sender)";
        namedParameterJdbcTemplate.update(query, new MapSqlParameterSource()
                .addValue("text", entity.getText())
                .addValue("date", entity.getLocalDateTime())
                .addValue("sender", entity.getUserId()));
    }

    @Override
    public void update(Message entity) {
        String query = "UPDATE messages SET text = :text, date = :date, sender = :sender WHERE id = :id";
        namedParameterJdbcTemplate.update(query, new MapSqlParameterSource()
                .addValue("id", entity.getId())
                .addValue("text", entity.getText())
                .addValue("date", entity.getLocalDateTime())
                .addValue("sender", entity.getUserId()));

    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM messages WHERE id = :id;";
        namedParameterJdbcTemplate.update(query, new MapSqlParameterSource()
                .addValue("id", id));
    }

    @Override
    public List<Message> getMessagesByUserId(Long userId) {
        String query = "SELECT * FROM messages WHERE sender = " + userId;
        return namedParameterJdbcTemplate.query(query, new BeanPropertyRowMapper<>(Message.class));
    }
}
