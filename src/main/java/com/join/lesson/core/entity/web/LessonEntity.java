package com.join.lesson.core.entity.web;

import com.join.lesson.core.common.TimeBaseEntity;
import com.join.lesson.feature.lesson.dto.RegistLessonRequest;
import com.join.lesson.feature.lesson.dto.UpdateLessonRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "lesson")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LessonEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;

    @Column(name = "lesson_name", nullable = false, length = 15)
    private String lessonName;

    @Column(name = "lesson_type", nullable = false)
    private int lessonType;

    @Column(name = "lesson_property", nullable = false)
    private int lessonProperty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Builder
    private LessonEntity(String lessonName, int lessonType, int lessonProperty, UserEntity userEntity) {
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.lessonProperty = lessonProperty;
        this.userEntity = userEntity;
    }

    public static LessonEntity toEntity(RegistLessonRequest registLessonRequest, UserEntity pro) {
        return LessonEntity.builder()
                .lessonName(registLessonRequest.getLessonName())
                .lessonProperty(registLessonRequest.getLessonProperty())
                .lessonType(registLessonRequest.getLessonType())
                .userEntity(pro)
                .build();
    }

    public void update(UpdateLessonRequest updateLessonRequest) {
        this.lessonName = updateLessonRequest.getLessonName();
        this.lessonType = updateLessonRequest.getLessonType();
        this.lessonProperty = updateLessonRequest.getLessonProperty();
    }
}
