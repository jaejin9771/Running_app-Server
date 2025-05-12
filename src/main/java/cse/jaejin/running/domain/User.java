package cse.jaejin.running.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Users")
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // @Column(nullable = false, unique = true)
    private String username;

    // @Column(nullable = false)
    private String password; // 해싱 필수

    // @Column(nullable = false)
    private String name;

    private int age;

    @Column(unique = true)
    private String phone;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public enum Role {
        USER, ADMIN
    }

}

