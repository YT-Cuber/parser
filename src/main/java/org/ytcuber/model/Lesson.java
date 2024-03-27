package org.ytcuber.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ytcuber.types.DayOfWeek;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    private Integer odd; // Чётная/Нечётная неделя
    private DayOfWeek datOfWeek; // День недели
    private Integer ordinal; // Номер пары
    private String subject; // Предмет
    private Integer subgroup; // Подгруппа
    private String teacher; // Преподаватель
    private String location; // Кабинет

    @ManyToOne
    private Group group; // Название группы(ID)
}
