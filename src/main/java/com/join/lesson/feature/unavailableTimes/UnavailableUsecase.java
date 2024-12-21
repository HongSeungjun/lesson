package com.join.lesson.feature.unavailableTimes;

import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.core.entity.web.UnavailableTimesEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.unavailableTimes.dto.RegistUnavailalbeTimeRequest;
import com.join.lesson.feature.unavailableTimes.dto.UnavailableTimeResponse;
import com.join.lesson.feature.unavailableTimes.dto.UpdateUnavailableTimeRequest;
import com.join.lesson.feature.unavailableTimes.repository.web.UnavailableQueryRepository;
import com.join.lesson.feature.unavailableTimes.repository.web.UnavailableRepository;
import com.join.lesson.share.unavailabletime.UnavailableService;
import com.join.lesson.share.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UnavailableUsecase implements UnavailableService {

    private final UnavailableRepository unavailableRepository;
    private final UnavailableQueryRepository unavailableQueryRepository;
    private final UserService userService;

    public Long registUnavailableTime(RegistUnavailalbeTimeRequest registUnavailalbeTimeRequest, Long proId) {

        UserEntity pro = userService.getUserById(proId);

        UnavailableTimesEntity unavailableTimesEntity = UnavailableTimesEntity.builder()
                .daysOfWeek(registUnavailalbeTimeRequest.getStartTime().getDayOfWeek().getValue() % 7)
                .startTime(registUnavailalbeTimeRequest.getStartTime().toLocalTime())
                .endTime(registUnavailalbeTimeRequest.getEndTime().toLocalTime())
                .userEntity(pro)
                .build();

        unavailableRepository.save(unavailableTimesEntity);

        return unavailableTimesEntity.getId();

    }

    @Transactional
    public Long updateUnavailableTime(UpdateUnavailableTimeRequest updateUnavailableTimeRequest, Long proId) {

        UnavailableTimesEntity unavailableTimesEntity = unavailableRepository.findById(updateUnavailableTimeRequest.getUnavailableId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_UNAVAILALBE_TIME));

        if (!isAuthorAuthorized(proId, unavailableTimesEntity.getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        unavailableTimesEntity.changeTime(updateUnavailableTimeRequest.getStartTime(), updateUnavailableTimeRequest.getEndTime());

        return unavailableTimesEntity.getId();

    }

    @Override
    public List<UnavailableTimeResponse> getUnavailableTimes(Long proId) {

        return unavailableQueryRepository.getUnavailableTimes(proId);

    }

    @Override
    public Long createTemporaryUnavailableTime(LocalDateTime requestedStart, LocalDateTime requestedEnd, Long proId) {
        return registUnavailableTime(RegistUnavailalbeTimeRequest.builder()
                        .startTime(requestedStart)
                        .endTime(requestedEnd)
                        .build(),
                proId);
    }

    @Override
    public Long deleteTemporaryUnavailableTime(Long unavailableId) {
        UnavailableTimesEntity unavailableTimesEntity = unavailableRepository.findById(unavailableId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_UNAVAILALBE_TIME));

        unavailableRepository.delete(unavailableTimesEntity);

        return unavailableTimesEntity.getId();
    }


    @Transactional
    public Long deleteUnavailableTime(Long unavailableId, Long proId) {

        UnavailableTimesEntity unavailableTimesEntity = unavailableRepository.findById(unavailableId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_UNAVAILALBE_TIME));

        if (!isAuthorAuthorized(proId, unavailableTimesEntity.getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        unavailableRepository.delete(unavailableTimesEntity);

        return unavailableTimesEntity.getId();
    }

    private boolean isAuthorAuthorized(Long userId, Long ownerId) {

        if (userId != ownerId) return false;

        return true;
    }

}
