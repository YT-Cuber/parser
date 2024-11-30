package org.ytcuber;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.ytcuber.database.model.Lesson;
import org.ytcuber.database.repository.LessonRepository;

@Transactional
@SpringBootTest
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    @Commit
    public void test() {
        var lesson = Lesson.builder().subject("Aaaa").build();
        lessonRepository.save(lesson);
    }
}