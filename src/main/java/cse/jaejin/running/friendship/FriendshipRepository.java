package cse.jaejin.running.friendship;

import cse.jaejin.running.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser(User user);
    Optional<Friendship> findByUserAndFriend(User user, User friend);
    List<Friendship> findByFriend(User friend);
}
