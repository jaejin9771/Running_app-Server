package cse.jaejin.running.photo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Photos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사진이 연결될 대상의 ID (SharedCourse의 ID 또는 Post의 ID)
    @Column(nullable = false)
    private Long targetId;

    // 사진이 연결될 대상의 타입 (SHARED_COURSE 또는 POST)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhotoTargetType targetType;

    @Column(nullable = false)
    private String imageUrl; // 사진 파일 경로 또는 URL

    @Column(nullable = false)
    private int orderIndex; // 게시글 내에서 사진의 순서 (0부터 시작)
}
