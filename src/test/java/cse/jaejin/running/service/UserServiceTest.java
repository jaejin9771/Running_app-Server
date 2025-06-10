package cse.jaejin.running.service;

import cse.jaejin.running.user.User;
import cse.jaejin.running.user.UserRepository;
import cse.jaejin.running.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        // given
        User user = new User();
        user.setUsername("bae123");  // 필수 필드 채워줘야 함
        user.setPassword("password"); // 예시
        user.setName("Bae");

        // when
        Long savedId = userService.register(user);
        User joinedUser = userRepository.findById(savedId).orElseThrow();

        // then
        Assertions.assertThat(joinedUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(joinedUser.getName()).isEqualTo("Bae");

    }
}
