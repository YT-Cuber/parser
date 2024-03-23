package org.ytcuber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ytcuber.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
