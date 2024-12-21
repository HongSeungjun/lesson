package com.join.lesson.feature.lesson;

import com.join.lesson.IntegrationTestSupport;
import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.ParticipationEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.lesson.dto.LessonResponse;
import com.join.lesson.feature.lesson.dto.RegistLessonRequest;
import com.join.lesson.feature.lesson.dto.UpdateLessonRequest;
import com.join.lesson.feature.lesson.repository.web.LessonRepository;
import com.join.lesson.feature.participation.repository.web.ParticipationRepository;
import com.join.lesson.feature.user.repository.web.UserRepository;
import com.join.lesson.testUser.WithMockCustomAccount;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class LessonUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private LessonUseCase lessonUseCase;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @DisplayName("레슨 프로는 레슨을 등록 할 수 있다.")
    @WithMockCustomAccount
    @Test
    public void registLessonTest() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");
        RegistLessonRequest request = RegistLessonRequest
                .builder()
                .lessonName("골프레슨 입문자 반")
                .lessonType(0)
                .lessonProperty(0)
                .build();
        //when
        Long lessonId = lessonUseCase.registLesson(request,pro.getId());

        LessonEntity result = lessonRepository.findById(lessonId).get();
        //then
        assertThat(result.getLessonName()).isEqualTo("골프레슨 입문자 반");
    }

    @DisplayName("레슨 프로는 레슨을 정보를 수정 할 수 있다.")
    @WithMockCustomAccount
    @Test
    public void updateLessonTest() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");
        LessonEntity lesson = createLesson("레슨 입문자반", pro, 0);
        UpdateLessonRequest request = UpdateLessonRequest.builder()
                .lessonId(lesson.getId())
                .lessonName("레슨 중급자반")
                .lessonProperty(0)
                .lessonType(0)
                .build();
        //when
        Long lessonId = lessonUseCase.updateLesson(request, pro.getId());

        LessonEntity result = lessonRepository.findById(lessonId).get();
        //then
        assertThat(result.getLessonName()).isEqualTo("레슨 중급자반");
    }

    @DisplayName("레슨 프로는 레슨을 삭제 할 수 있다.")
    @WithMockCustomAccount
    @Test
    public void deleteLessonTest() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");
        LessonEntity lesson = createLesson("레슨 입문자반", pro, 0);
        //when
        lessonUseCase.deleteLesson(lesson.getId(), pro.getId());

        //then
        assertThat(lessonRepository.count()).isEqualTo(0);
    }

    @Disabled
    @DisplayName("레슨 프로는 등록한 레슨과 레슨의 참여자를 타입별로 조회 할 수 있다.")
    @WithMockCustomAccount
    @Test
    public void getLessonType() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");
        UserEntity member1 = createUser("member1", "kim", "01033333333", "ROLE_MEMBER");
        UserEntity member2 = createUser("member2", "park", "0103333323", "ROLE_MEMBER");

        LessonEntity lesson1 = createLesson("참가자 테스트", pro, 0);
        LessonEntity lesson2 = createLesson("참가자 테스트", pro, 0);


        createParticipation(member1, lesson1);
        createParticipation(member2, lesson1);
        createParticipation(member1, lesson2);


        //when
        List<LessonResponse> results = lessonUseCase.getLesson(pro.getId());

        //then
        assertThat(results).hasSize(2);
    }

    @Transactional
    @DisplayName("레슨 프로는 등록한 레슨을 조회 할 수 있다.")
    @WithMockCustomAccount
    @Test
    public void getLesson() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");

        LessonEntity lesson1 = createLesson("참가자 테스트", pro, 0);
        LessonEntity lesson2 = createLesson("참가자 테스트", pro, 0);

        //when
        List<LessonResponse> results = lessonUseCase.getLesson(pro.getId());

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