package edu.school21.sockets.models;

import java.time.LocalDateTime;

public class Message {
    private String text;
    private LocalDateTime localDateTime;
    private Long id;

    private Long userId;
    public Message() {
    }

    public Message(String text, LocalDateTime localDateTime, Long id, Long userId) {
        this.text = text;
        this.localDateTime = localDateTime;
        this.id = id;
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", localDateTime=" + localDateTime +
                ", id=" + id +
                ", userId=" + userId +
                '}';
    }
}
