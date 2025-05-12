// LoginResponse.java
package cse.jaejin.running.dto;

import cse.jaejin.running.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private UserDto user;

    @Getter @Setter
    public static class UserDto {
        private String username;
        private String password;  // 실제 서비스에서는 생략하거나 마스킹
        private String name;
        private int age;
        private String phone;
        private String birthDate;
        private String role;
    }

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setPhone(user.getPhone());
        dto.setBirthDate(user.getBirthDate().toString()); // yyyy-MM-dd
        dto.setRole(user.getRole().name());
        return dto;
    }
}
