package org.ytcuber.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ytcuber.database.model.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
//    List<Lesson> findBySubgroup(Integer subgroup);
    // Возврашение всех пар
    @Query("select l from Lesson l where (l.subgroup = :subgroup or l.subgroup = 0) order by l.odd, l.dayOfWeek, l.ordinal")
    List<Lesson> getLessons(Integer subgroup);

    @Query("SELECT l FROM Lesson l " +
            "WHERE (l.subgroup = :subgroup OR l.subgroup = 0) AND l.group.id = :groupId AND l.odd = :odd " +
            "ORDER BY l.dayOfWeek")
    List<Lesson> findLessonsByGroupIdAndSubgroupAndOdd(@Param("groupId") Integer groupId,
                                                          @Param("subgroup") Integer subgroup,
                                                          @Param("odd") Integer odd);
}