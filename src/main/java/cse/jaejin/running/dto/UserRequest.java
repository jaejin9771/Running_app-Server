package cse.jaejin.running.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cse.jaejin.running.domain.User.Role;
import java.time.LocalDate;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String name;
    private int age;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Role role = Role.USER; // 기본값 설정
}
