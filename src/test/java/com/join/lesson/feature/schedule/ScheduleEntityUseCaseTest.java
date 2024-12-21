package com.join.lesson.feature.schedule;

import com.join.lesson.IntegrationTestSupport;
import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.ScheduleEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.lesson.repository.web.LessonRepository;
import com.join.lesson.feature.schedule.dto.RegistScheduleRequest;
import com.join.lesson.feature.schedule.dto.ScheduleResponse;
import com.join.lesson.feature.schedule.dto.UpdateScheduleRequest;
import com.join.lesson.feature.schedule.repository.web.ScheduleRepositroy;
import com.join.lesson.feature.user.repository.web.UserRepository;
import com.join.lesson.testUser.WithMockCustomAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ScheduleEntityUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleUseCase scheduleUseCase;

    @Autowired
    private ScheduleRepositroy scheduleRepositroy;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("레슨 프로는 레슨의 스케줄을 등록 할 수 있다.")
    @Test
    public void registScheduleTest() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        RegistScheduleRequest registScheduleRequest = RegistScheduleRequest.builder()
                .lessonId(lesson.getId())
                .start(LocalDateTime.now().minusDays(1).minusHours(1))
                .end(LocalDateTime.now().minusDays(1))
                .maxCount(8)
                .title("그룹 2회차")
                .build();
        //when
        Long scheduleId = scheduleUseCase.registSchedule(registScheduleRequest);
        ScheduleEntity result = scheduleRepositroy.findById(scheduleId).get();

        //then
        assertThat(result.getScheduleName()).isEqualTo("그룹 2회차");

    }

    @DisplayName("레슨 프로는 레슨 스케줄의 내용을 수정 할 수 있다.")
    @Test
    public void updateScheduleTest() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012341234", "ROLE_PRO");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 30);

        UpdateScheduleRequest updateScheduleRequest = UpdateScheduleRequest.builder()
                .scheduleId(schedule.getId())
                .title("2회차")
                .start(LocalDateTime.now().minusDays(1).minusHours(1))
                .end(LocalDateTime.now().minusDays(1))
                .maxCount(20)
                .build();
        //when
        Long scheduleId = scheduleUseCase.updateSchedule(updateScheduleRequest);

        ScheduleEntity result = scheduleRepositroy.findById(scheduleId).get();

        log.info("start time :{}", result.getScheduleStart());
        //then
        assertThat(result.getScheduleName()).isEqualTo("2회차");
    }

    @DisplayName("회원은 자신이 속한 스케쥴을 조회 할 수 있다.")
    @WithMockCustomAccount
    @Test
    public void getScheduleForMember() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012345678", "ROLE_PRO");

        UserEntity memeber = createUser("mem1", "mem1", "01012351234", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule0 = createSchedule(lesson, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1), "OT", 30);
        ScheduleEntity schedule1 = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 30);
        ScheduleEntity schedule2 = createSchedule(lesson, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5), "2회차", 30);
        ScheduleEntity schedule3 = createSchedule(lesson, LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(7), "3회차", 30);
        ScheduleEntity schedule4 = createSchedule(lesson, LocalDateTime.now().plusDays(8), LocalDateTime.now().plusDays(8), "3회차", 30);

        //when
        List<ScheduleResponse> results = scheduleUseCase.getSchedule(lesson.getId());

        //then
        assertThat(results).hasSize(3);
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

    private ScheduleEntity createSchedule(LessonEntity lesson, LocalDateTime start, LocalDateTime end, String name, int maxCount) {
        return scheduleRepositroy.save(
                ScheduleEntity.builder()
                        .scheduleName(name)
                        .scheduleStart(start)
                        .scheduleEnd(end)
                        .maxCount(maxCount)
                        .lesson(lesson)
                        .state(0)
                        .reservedCount(0)
                        .build()
        );
    }
}