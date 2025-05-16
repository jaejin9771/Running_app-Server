package cse.jaejin.running.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SharedCourseImages")
@Getter
@Setter
public class SharedCourseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shared_course_id", nullable = false)
    private SharedCourse sharedCourse;

    @Column(nullable = false)
    private String imageUrl;
}

