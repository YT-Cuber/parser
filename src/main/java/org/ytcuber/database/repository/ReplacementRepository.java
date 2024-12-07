package org.ytcuber.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ytcuber.database.model.Replacement;

import java.util.List;

@Repository
public interface ReplacementRepository extends JpaRepository<Replacement, Long> {
    @Query("SELECT r FROM Replacement r " +
            "WHERE (r.subgroup = :subgroup OR r.subgroup = 0) AND r.group.id = :groupId " +
            "ORDER BY r.datOfWeek, r.ordinal")
    List<Replacement> findReplacementsByGroupIdAndSubgroup(@Param("groupId") Integer groupId, @Param("subgroup") Integer subgroup);

    @Query("SELECT r FROM Replacement r WHERE r.group.title = :groupName AND (r.subgroup = :subgroup OR r.subgroup = 0)")
    List<Replacement> findReplacementsByGroupTitleAndSubgroup( @Param("groupName") String groupName, @Param("subgroup") Integer subgroup );

    @Query("SELECT r FROM Replacement r WHERE LOWER(r.teacher) LIKE LOWER(CONCAT('%', :teacherName, '%')) ORDER BY r.datOfWeek, r.ordinal")
    List<Replacement> findReplacementsByTeacher(@Param("teacherName") String teacherName);

    @Query("SELECT r FROM Replacement r WHERE r.location = :locationName")
    List<Replacement> findReplacementsByLocation(@Param("locationName") String locationName);
}
