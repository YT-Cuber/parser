package org.ytcuber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ytcuber.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select g.id from groups g where g.title = :name")
    Group findByName(String name);
}
