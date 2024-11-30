package org.ytcuber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ytcuber.model.Group;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    // Возвращает squad по названию группы (нужно проверить, что поле squad существует)
    @Query("SELECT g.squad FROM groups g WHERE LOWER(g.title) LIKE LOWER(:groupName)")
    Integer findSquadByGroupName(@Param("groupName") String groupName);

    @Query("SELECT g FROM groups g WHERE LOWER(g.title) LIKE LOWER(:groupName)")
    Optional<Group> findByGroupName(@Param("groupName") String groupName);
}