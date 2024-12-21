package com.join.lesson.share.participation;

public interface ParticipationService {

    void deleteAllByLessonId(Long lessonId);

    int getRemainingCount(Long memberId, Long lessonId);

    void decreaseRemainingCount(Long memberId, Long lessionId);

    void increaseRemainingCount(Long memberId, Long lessionId);


}

