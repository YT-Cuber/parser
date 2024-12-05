package org.ytcuber.handler;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.database.dto.LessonDTO;
import org.ytcuber.database.dto.ReplacementDTO;
import org.ytcuber.database.model.Lesson;
import org.ytcuber.database.model.Replacement;
import org.ytcuber.database.repository.LessonRepository;
import org.ytcuber.database.repository.ReplacementRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeacherSchedule {
    private final LessonRepository lessonRepository;
    private final ReplacementRepository replacementRepository;

    @Autowired
    public TeacherSchedule(LessonRepository lessonRepository, ReplacementRepository replacementRepository) {
        this.lessonRepository = lessonRepository;
        this.replacementRepository = replacementRepository;
    }

    @PostConstruct
    private void init() { System.out.println("TeacherSchedule успешно инициализирован"); }

    public List<Object> giveSchedule(String teacherName, Integer odd) throws Exception {

        // Получаем данные из репозиториев
        List<Lesson> lessons = lessonRepository.findLessonsByTeacherAndOdd(teacherName, odd);
        List<Replacement> replacements = replacementRepository.findReplacementsByTeacherAndOdd(teacherName);

        // Преобразуем Lesson в LessonDTO
        List<LessonDTO> lessonDTOS = lessons.stream()
                .map(lesson -> new LessonDTO(
                        lesson.getDayOfWeek(),
                        lesson.getOrdinal(),
                        lesson.getLocation(),
                        lesson.getSubject(),
                        lesson.getTeacher()
                ))
                .toList();

        // Преобразуем Replacement в ReplacementDTO
        List<ReplacementDTO> replacementDTOS = replacements.stream()
                .map(replacement -> new ReplacementDTO(
                        replacement.getDate(),
                        replacement.getDatOfWeek(),
                        replacement.getOrdinal(),
                        replacement.getLocation(),
                        replacement.getSubject(),
                        replacement.getTeacher()
                ))
                .toList();

        // Создаём результирующий список
        List<Object> mergedList = new ArrayList<>(lessonDTOS);

        // Заменяем элементы в lessonDTOS элементами из replacementDTOS при совпадении
        for (ReplacementDTO replacement : replacementDTOS) {
            boolean replaced = false;

            // Ищем элемент в lessonDTOS, совпадающий по критериям
            for (int i = 0; i < lessonDTOS.size(); i++) {
                LessonDTO lesson = lessonDTOS.get(i);

                if (lesson.getOrdinal() == replacement.getOrdinal() &&
                        lesson.getDayOfWeek().equals(replacement.getDatOfWeek())) {
                    // Заменяем элемент в mergedList
                    mergedList.set(i, replacement);
                    replaced = true;
                    break;
                }
            }

            // Если replacement не нашёл пары, добавляем его в конец списка
            if (!replaced) {
                mergedList.add(replacement);
            }
        }

        return mergedList;
    }
}
