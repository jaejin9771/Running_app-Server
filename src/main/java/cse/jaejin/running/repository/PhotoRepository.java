package cse.jaejin.running.repository;

import cse.jaejin.running.domain.Photo;
import cse.jaejin.running.domain.PhotoTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // 특정 targetId와 targetType을 가진 사진들을 순서(orderIndex)에 따라 오름차순으로 조회
    List<Photo> findByTargetIdAndTargetTypeOrderByOrderIndexAsc(Long targetId, PhotoTargetType targetType);

    // 특정 targetId와 targetType을 가진 사진들을 삭제
    @Modifying // DML 쿼리(INSERT, UPDATE, DELETE)를 실행할 때 사용
    @Query("DELETE FROM Photo p WHERE p.targetId = :targetId AND p.targetType = :targetType")
    void deleteByTargetIdAndTargetType(@Param("targetId") Long targetId, @Param("targetType") PhotoTargetType targetType);
}
