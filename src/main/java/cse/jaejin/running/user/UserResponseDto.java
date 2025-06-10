package cse.jaejin.running.user;

import cse.jaejin.running.user.User.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class UserResponseDto {
    private Long id;
    private String username;
    private String name;
    private int age;
    private String phone;
    private LocalDate birthDate;
    private Role role;

    private double totalDistance; // 누적 거리
    private String tier;          // 티어

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setPhone(user.getPhone());
        dto.setBirthDate(user.getBirthDate());
        dto.setRole(user.getRole());
        dto.setTotalDistance(user.getTotalDistance());
        dto.setTier(user.getTier());
        return dto;
    }
}

