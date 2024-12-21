package com.join.lesson.feature.lesson.repository.web;

import com.join.lesson.IntegrationTestSupport;
import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.feature.user.repository.web.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class LessonQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private LessonQueryRepository lessonQueryRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

//    @DisplayName("레슨 프로는 등록한 레슨을 타입별로 조회 할 수 있다.")
//    @Test
//    public void getLessonTest() throws Exception {
//        //given
//        UserEntity pro = createUser();
//
//        LessonEntity personalLesson = createLesson("개인 레슨", pro, 1);
//        LessonEntity groupLesson = createLesson("그룹 레슨", pro, 0);
//        LessonEntity groupLesson2 = createLesson("그룹 레슨2", pro, 0);
//        //when
//        List<LessonResponse> allResults = lessonQueryRepository.getLesson(pro.getId(), null);
//        List<LessonResponse> personalResults = lessonQueryRepository.getLesson(pro.getId(), 1);
//        List<LessonResponse> groupResults = lessonQueryRepository.getLesson(pro.getId(), 0);
//
//
//        //then
//        assertThat(allResults).hasSize(3);
//        assertThat(personalResults).hasSize(1);
//        assertThat(groupResults).hasSize(2);
//
//    }


    @Test
    public void mapperTest() throws Exception {
        //given
        UserEntity pro = createUser();
        LessonEntity lessonEntity = LessonEntity.builder()
                .lessonName("test")
                .lessonProperty(0)
                .lessonType(0)
                .userEntity(pro)
                .build();

//        Lesson lesson = LessonMapper.INSTANCE.entityToDomain(lessonEntity);
        //when

//        System.out.println(lesson.getLessonName());
        //then
    }

    private UserEntity createUser() {
        return userRepository.save(
                UserEntity.builder()
                        .loginId("hong")
                        .loginPw("password")
                        .name("pro")
                        .tel("01011111111")
                        .active(0)
                        .role("ROLE_PRO")
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
}