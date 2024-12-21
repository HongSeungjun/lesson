package com.join.lesson.core.entity.web;

import com.join.lesson.core.common.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecordEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    //    @Column(name = "feedback", nullable = false, length = 1000)
//    private String feedback;
//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @Builder
    public RecordEntity(String content, UserEntity user, ScheduleEntity schedule) {
        this.content = content;
        this.user = user;
        this.schedule = schedule;
    }


    public static RecordEntity toEntity(String content, UserEntity user, ScheduleEntity schedule) {
        return RecordEntity.builder()
                .content(content)
                .user(user)
                .schedule(schedule)
                .build();
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
