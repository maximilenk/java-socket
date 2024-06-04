package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message> {
    List<Message> getMessagesByUserId(Long userId);
}
