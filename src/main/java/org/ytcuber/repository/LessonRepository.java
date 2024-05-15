package org.ytcuber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ytcuber.model.Lesson;

import java.util.List;
import java.util.Objects;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findBySubgroup(Integer subgroup);
    @Query("select l from Lesson l where l.subgroup = :subgroup or l.subgroup = 0 order by l.odd, l.dayOfWeek, l.ordinal")
    List<Lesson> getLessons(Integer subgroup);
//    @Query(
//        value = "SELECT * FROM lesson",
//            nativeQuery = true)
//    Lesson findBy

}
