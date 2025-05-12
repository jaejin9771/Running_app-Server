package cse.jaejin.running.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Friendship {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn
    private User friend;

    private String status;

    private LocalDateTime createdAt;
}
