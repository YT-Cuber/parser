package org.ytcuber.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ytcuber.database.model.Group;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    // Возвращает squad по названию группы (нужно проверить, что поле squad существует)
    @Query("SELECT g.squad FROM groups g WHERE LOWER(g.title) LIKE LOWER(:groupName)")
    Integer findSquadByGroupName(@Param("groupName") String groupName);
    // Возвращает группу по названию
    @Query("SELECT g FROM groups g WHERE LOWER(g.title) LIKE LOWER(:groupName)")
    Optional<Group> findByGroupName(@Param("groupName") String groupName);
    // Возвращает id группы по названию
    @Query("SELECT g.id FROM groups g WHERE LOWER(g.title) LIKE LOWER(:groupName)")
    Integer findByName(@Param("groupName") String groupName);
    // Нахождение названия группы по id
    @Query("SELECT g.title FROM groups g WHERE g.id = :groupId")
    String findNameById(@Param("groupId") Integer groupId);
    // Нахождние id последней группы
    @Query("SELECT g.id FROM groups g ORDER BY g.id DESC LIMIT 1")
    Integer findLastId();
}