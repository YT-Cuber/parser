package org.ytcuber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ytcuber.model.Replacement;

@Repository
public interface ReplacementRepository extends JpaRepository<Replacement, Long>  {
}
