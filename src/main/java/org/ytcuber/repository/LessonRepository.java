package org.ytcuber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ytcuber.model.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findBySubgroup(Integer subgroup);
//    @Query(
//        value = "SELECT * FROM lesson",
//            nativeQuery = true)
//    Lesson findBy

}
