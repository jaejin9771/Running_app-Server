package cse.jaejin.running.repository;

import cse.jaejin.running.domain.Friendship;
import cse.jaejin.running.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser(User user);
    Optional<Friendship> findByUserAndFriend(User user, User friend);
}
