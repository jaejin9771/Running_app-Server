package cse.jaejin.running.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Friendship {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @CreatedDate
    private LocalDateTime createdAt;
}
