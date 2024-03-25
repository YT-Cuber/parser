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
    private Long id;
    private String odd;
    private String ordinal;
    private DayOfWeek datOfWeek;
    private String subject;
    private String teacher;
    private String location;

    @ManyToOne
    private Group group;
}
