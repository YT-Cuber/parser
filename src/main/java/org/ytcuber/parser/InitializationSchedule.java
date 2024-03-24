/*
package org.ytcuber.parser;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.model.Schedule;
import org.ytcuber.repository.ScheduleRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class InitializationSchedule {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PostConstruct
    public void init() {
        String[] daysOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
        String[][] weekdaySchedule = {
                {"8:30", "10:10"},
                {"10:10", "11:40"},
                {"12:20", "13:50"},
                {"14:20", "15:50"},
                {"16:00", "17:30"},
                {"17:40", "19:10"},
                {"19:20", "20:50"}
        };
        String[][] saturdaySchedule = {
                {"8:30", "10:10"},
                {"10:10", "11:40"},
                {"11:50", "13:20"},
                {"13:30", "15:00"},
                {"15:10", "16:40"},
                {"16:50", "18:20"}
        };

        for (String day : daysOfWeek) {
            String[][] daySchedule = day.equals("Суббота") ? saturdaySchedule : weekdaySchedule;
            for (int i = 0; i < daySchedule.length; i++) {
                Schedule schedule = new Schedule();
                schedule.setOrdinal(Integer.toString(i + 1));
                schedule.setStarttime(LocalTime.parse(daySchedule[i][0]));
                schedule.setEndtime(LocalTime.parse(daySchedule[i][1]));
                schedule.setDayOfWeek(DayOfWeek.valueOf(day));

                scheduleRepository.save(schedule);
            }
        }
    }
}
*/