package com.join.lesson.feature.participation.repository;

import com.join.lesson.IntegrationTestSupport;
import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.ParticipationEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.lesson.repository.web.LessonQueryRepository;
import com.join.lesson.feature.lesson.repository.web.LessonRepository;
import com.join.lesson.feature.participation.dto.ParticipationResponse;
import com.join.lesson.feature.participation.repository.web.ParticipationQueryRepository;
import com.join.lesson.feature.participation.repository.web.ParticipationRepository;
import com.join.lesson.feature.user.repository.web.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ParticipationQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private LessonQueryRepository lessonQueryRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private ParticipationQueryRepository participationQueryRepository;

    @DisplayName("레슨 프로는 레슨 참여자를 조회 할 수 있다.")
    @Test
    public void getParticipationTest() throws Exception {
        //given
        UserEntity pro = createUser("pro", "hong", "01012341234", "USER_PRO");
        UserEntity member1 = createUser("member1", "kim", "01033333333", "USER_MEMBER");
        UserEntity member2 = createUser("member2", "park", "0103333323", "USER_MEMBER");

        LessonEntity lesson = createLesson("참가자 테스트", pro, 0);

        createParticipation(member1, lesson);
        createParticipation(member2, lesson);
        //when

        List<ParticipationResponse> results = participationQueryRepository.getLessonParticipaion(lesson.getId());
        //then
        assertThat(results).hasSize(2);
    }

    private UserEntity createUser(String loginId, String name, String tel, String role) {
        return userRepository.save(
                UserEntity.builder()
                        .loginId(loginId)
                        .loginPw("password")
                        .name(name)
                        .tel(tel)
                        .active(0)
                        .role(role)
                        .build()
        );
    }

    private LessonEntity createLesson(String lessonName, UserEntity user, int type) {
        return lessonRepository.save(
                LessonEntity.builder()
                        .lessonName(lessonName)
                        .lessonType(type)
                        .lessonProperty(0)
                        .userEntity(user)
                        .build());
    }

    private ParticipationEntity createParticipation(UserEntity user, LessonEntity lesson) {
        return participationRepository.save(
                ParticipationEntity.builder()
                        .user(user)
                        .lesson(lesson)
                        .remainingCount(30)
                        .build());
    }
}