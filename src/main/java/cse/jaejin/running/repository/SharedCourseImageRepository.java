package cse.jaejin.running.repository;

import cse.jaejin.running.domain.SharedCourseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedCourseImageRepository extends JpaRepository<SharedCourseImage, Long> {
    List<SharedCourseImage> findBySharedCourseId(Long sharedCourseId);
}
