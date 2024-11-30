package org.ytcuber.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ytcuber.database.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>  { }