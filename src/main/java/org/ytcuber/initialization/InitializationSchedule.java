package org.ytcuber.initialization;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.database.model.Schedule;
import org.ytcuber.database.repository.ScheduleRepository;
import org.ytcuber.database.types.DayOfWeek;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class InitializationSchedule {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @PostConstruct
    public void init() {
        String[][] weekdaySchedule = {
                {"08:30", "10:10"},
                {"10:10", "11:40"},
                {"12:20", "13:50"},
                {"14:20", "15:50"},
                {"16:00", "17:30"},
                {"17:40", "19:10"},
                {"19:20", "20:50"}
        };
        String[][] saturdaySchedule = {
                {"08:30", "10:10"},
                {"10:10", "11:40"},
                {"11:50", "13:20"},
                {"13:30", "15:00"},
                {"15:10", "16:40"},
                {"16:50", "18:20"}
        };

        DayOfWeek[] daysOfWeek = DayOfWeek.values();

        for (DayOfWeek dayOfWeek : daysOfWeek) {
            String[][] daySchedule = dayOfWeek == DayOfWeek.SATURDAY ? saturdaySchedule : weekdaySchedule;
            for (int j = 0; j < daySchedule.length; j++) {
                try {
                    Schedule schedule = new Schedule();
                    schedule.setOrdinal(j + 1);
                    schedule.setStarttime(LocalTime.parse(daySchedule[j][0]));
                    schedule.setEndtime(LocalTime.parse(daySchedule[j][1]));
                    schedule.setDayOfWeek(dayOfWeek);

                    scheduleRepository.save(schedule);
                } catch (DateTimeParseException e) {
                    System.err.println("Ошибка парсинга времени: " + e.getMessage());
                }
            }
        }
    }
}


