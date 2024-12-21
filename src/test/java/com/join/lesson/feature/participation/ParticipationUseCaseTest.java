package com.join.lesson.feature.participation;

import com.join.lesson.IntegrationTestSupport;
import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.ParticipationEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.feature.lesson.repository.web.LessonRepository;
import com.join.lesson.feature.participation.dto.RegistParticipantRequest;
import com.join.lesson.feature.participation.dto.UpdateRemainingCountRequest;
import com.join.lesson.feature.participation.repository.web.ParticipationRepository;
import com.join.lesson.feature.user.repository.web.UserRepository;
import com.join.lesson.testUser.WithMockCustomAccount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
class ParticipationUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private ParticipationUseCase participationUseCase;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @DisplayName("레슨 프로가 레슨에 회원을 등록 할 때 이미 존재하면 예외가 발생한다.")
    @WithMockCustomAccount
    @Test
    public void registParticipantException() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");
        UserEntity member1 = createUser("testmember1", "kim", "01099999999", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        createParticipation(member1, lesson);

        RegistParticipantRequest registParticipantRequest = RegistParticipantRequest.builder()
                .lessonId(lesson.getId())
                .loginId(member1.getLoginId())
                .count(30)
                .build();

        //when //then
        assertThatThrownBy(() -> participationUseCase.registParticipant(registParticipantRequest))
                .isInstanceOf(BaseException.class)
                .satisfies(e -> {
                    BaseException baseException = (BaseException) e;
                    assertThat(baseException.getErrorCode()).isEqualTo(ErrorCode.ALREADY_EXIST_MEMBER);
                });

    }

    @DisplayName("레슨 프로가 레슨에 회원을 등록 할 수 있다.")
    @WithMockCustomAccount
    @Test
    public void registParticipant() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");
        UserEntity member1 = createUser("testmember1", "kim", "01099999999", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        RegistParticipantRequest registParticipantRequest = RegistParticipantRequest.builder()
                .lessonId(lesson.getId())
                .loginId(member1.getLoginId())
                .count(30)
                .build();

        //when
        participationUseCase.registParticipant(registParticipantRequest);

        List<ParticipationEntity> results = participationRepository.findByLessonId(lesson.getId());
        // then
        assertThat(results).hasSize(1);

    }

    @DisplayName("레슨 프로는 레슨 참여자의 이용권 횟수를 변경 할 수 있다.")
    @Test
    public void updateMemeberRemainingCountTest() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01014941234", "ROLE_PRO");
        UserEntity member1 = createUser("testmember1", "kim", "01099999999", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ParticipationEntity participation = createParticipation(member1, lesson);

        UpdateRemainingCountRequest updateRemainingCountRequest = UpdateRemainingCountRequest.builder()
                .participationId(participation.getId())
                .count(50)
                .build();
        //when
        Long participationId = participationUseCase.updateMemeberRemainingCount(updateRemainingCountRequest, pro.getId());

        ParticipationEntity result = participationRepository.findById(participationId).get();

        //then
        Assertions.assertThat(result.getRemainingCount()).isEqualTo(50);
    }

    @DisplayName("레슨 프로는 레슨 참가자를 삭제 할 수 있다.")
    @Test
    public void deleteParticipant() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");
        UserEntity member1 = createUser("testmember1", "kim", "01099999999", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ParticipationEntity participation = createParticipation(member1, lesson);
        //when

        participationUseCase.deleteParticipant(participation.getId(), pro.getId());

        //when //then
        assertThatThrownBy(() -> participationRepository.findById(participation.getId()).get())
                .isInstanceOf(NoSuchElementException.class);

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