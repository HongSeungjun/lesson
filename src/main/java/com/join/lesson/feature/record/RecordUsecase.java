package com.join.lesson.feature.record;

import com.join.lesson.core.entity.web.RecordEntity;
import com.join.lesson.core.entity.web.ScheduleEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.core.common.util.SecurityUtils;
import com.join.lesson.feature.record.dto.RecordResponse;
import com.join.lesson.feature.record.dto.RegistRecordRequest;
import com.join.lesson.feature.record.dto.UpdateRecordRequest;
import com.join.lesson.feature.record.repository.web.RecordRepository;
import com.join.lesson.share.schedule.ScheduleService;
import com.join.lesson.share.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class RecordUsecase {

    private final RecordRepository recordRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;

    public Long registRecord(RegistRecordRequest registRecordRequest) {

        UserEntity pro = userService.getUserByLoginId(SecurityUtils.getLoginId());
        ScheduleEntity schedule = scheduleService.getScheduleById(registRecordRequest.getScheduleId());

        RecordEntity record = RecordEntity.toEntity(registRecordRequest.getContent(), pro, schedule);

        recordRepository.save(record);

        return record.getId();
    }

    public RecordResponse getRecord(Long scheduleId) {

        RecordEntity recordEntity = recordRepository.findByScheduleId(scheduleId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RECORD));

        return RecordResponse.builder()
                .content(recordEntity.getContent())
                .build();
    }

    @Transactional
    public Long updateRecord(UpdateRecordRequest updateRecordRequest) {

        RecordEntity record = recordRepository.findByScheduleId(updateRecordRequest.getScheduleId()).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RECORD));

        record.changeContent(updateRecordRequest.getContent());

        return record.getId();
    }
}
