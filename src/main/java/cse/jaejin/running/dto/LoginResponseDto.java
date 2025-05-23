// LoginResponse.java
package cse.jaejin.running.dto;

import cse.jaejin.running.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LoginResponseDto {
    private boolean success;
    private String message;
    private UserDto user;

    @Getter @Setter
    public static class UserDto {
        private Long id;
        private String username;
        private String name;
        private int age;
        private String phone;
        private String birthDate;
        private String role;
    }

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setPhone(user.getPhone());
        dto.setBirthDate(user.getBirthDate().toString()); // yyyy-MM-dd
        dto.setRole(user.getRole().name());
        return dto;
    }
}
