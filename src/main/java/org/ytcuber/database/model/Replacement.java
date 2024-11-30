package org.ytcuber.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ytcuber.database.types.DayOfWeek;
import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Replacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer ordinal; // Номер пары
    private DayOfWeek datOfWeek; // День недели
    private String subject; // Предмет
    private Integer subgroup; // Подгруппа
    private String teacher; // Преподаватель
    private String location; // Кабинет
    private Date date; // Дата замены
    @ManyToOne
    private Group group; // Название группы(ID)
}