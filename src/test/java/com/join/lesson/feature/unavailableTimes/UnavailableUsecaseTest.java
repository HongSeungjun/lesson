package com.join.lesson.feature.unavailableTimes;

import com.join.lesson.IntegrationTestSupport;
import com.join.lesson.core.entity.web.UnavailableTimesEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.unavailableTimes.dto.RegistUnavailalbeTimeRequest;
import com.join.lesson.feature.unavailableTimes.dto.UpdateUnavailableTimeRequest;
import com.join.lesson.feature.unavailableTimes.repository.web.UnavailableRepository;
import com.join.lesson.feature.user.repository.web.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class UnavailableUsecaseTest extends IntegrationTestSupport {

    @Autowired
    private UnavailableUsecase unavailableUsecase;

    @Autowired
    private UnavailableRepository unavailableRepository;

    @Autowired
    private UserRepository userRepository;


    @DisplayName("레슨 프로는 레슨 불가한 시간을 등록 할 수 있다.")
    @Test
    public void registUnavailableTime() throws Exception {

        //given
        UserEntity pro = createUser("protest", "hong", "01000341234", "ROLE_PRO");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        RegistUnavailalbeTimeRequest registUnavailalbeTimeRequest = RegistUnavailalbeTimeRequest.builder()
                .startTime(now)
                .endTime(end)
                .build();

        //when
        Long unavailableId = unavailableUsecase.registUnavailableTime(registUnavailalbeTimeRequest, pro.getId());

        UnavailableTimesEntity unavailableTimesEntity = unavailableRepository.findById(unavailableId).get();

        //then
        assertThat(unavailableTimesEntity.getDaysOfWeek()).isEqualTo(2);

    }


    @DisplayName("레슨 프로는 레슨 불가능 시간을 변경 할 수 있다.")
    @Test
    public void updateUnavailableTime() throws Exception {
        //given

        UserEntity pro = createUser("protest", "hong", "01000341234", "ROLE_PRO");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        UnavailableTimesEntity unavailableTimesEntity = createUnavailTime(now, end, pro);

        UpdateUnavailableTimeRequest updateUnavailableTimeRequest = UpdateUnavailableTimeRequest.builder()
                .unavailableId(unavailableTimesEntity.getId())
                .startTime(now.plusHours(1))
                .endTime(end.plusHours(1))
                .build();

        //when
        unavailableUsecase.updateUnavailableTime(updateUnavailableTimeRequest, pro.getId());

        //then
        assertThat(updateUnavailableTimeRequest.getEndTime()).isEqualTo(end.plusHours(1));

    }

    private UnavailableTimesEntity createUnavailTime(LocalDateTime now, LocalDateTime end, UserEntity userEntity) {
        return unavailableRepository.save(
                UnavailableTimesEntity.builder()
                        .startTime(now.toLocalTime())
                        .endTime(end.toLocalTime())
                        .daysOfWeek(1)
                        .userEntity(userEntity)
                        .build()
        );
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
}