package cse.jaejin.running.dto;

import cse.jaejin.running.domain.User.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private int age;
    private String phone;
    private LocalDate birthDate;
    private Role role;
}
