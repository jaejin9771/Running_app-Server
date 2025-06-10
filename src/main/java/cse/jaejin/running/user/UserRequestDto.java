package cse.jaejin.running.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cse.jaejin.running.user.User.Role;
import java.time.LocalDate;

@Data
public class UserRequestDto {
    private String username;
    private String password;
    private String name;
    private int age;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Role role = Role.USER; // 기본값 설정
}
