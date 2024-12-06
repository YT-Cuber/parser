package org.ytcuber.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ytcuber.database.model.Group;
import org.ytcuber.database.types.DayOfWeek;

@Data
@AllArgsConstructor
public class LessonDTO {
    private DayOfWeek dayOfWeek; // День недели
    private Integer ordinal;     // Номер пары
    private String location;     // Кабинет
    private String subject;      // Предмет
    private String teacher;      // Преподаватель
    private Group group;        // Группа
}