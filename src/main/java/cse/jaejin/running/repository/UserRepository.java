//package cse.jaejin.running.repository;
//
//import cse.jaejin.running.domain.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.NoResultException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//@RequiredArgsConstructor
//public class UserRepository {
//
//    // 원래 표준 어노테이션 @PersistenceContext 를 사용해야하지만 스프링부트가 @Autowired 도 되도록 만들어줌.
//    private final EntityManager em;
//
//    public void save(User User) {
//        em.persist(User);
//    }
//
//    public User findOne(Long id) {
//        return em.find(User.class, id);
//    }
//
//    public List<User> findAll() {
//        return em.createQuery("select m from User m", User.class)
//                .getResultList();
//    }
//
//    public Optional<User> findByUsername(String username) {
//        try {
//            User user = em.createQuery("select u from User u where u.username = :username", User.class)
//                    .setParameter("username", username)
//                    .getSingleResult();
//            return Optional.of(user);
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }
//}
package cse.jaejin.running.repository;

import cse.jaejin.running.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username 기준으로 사용자 조회
    Optional<User> findByUsername(String username);
}
