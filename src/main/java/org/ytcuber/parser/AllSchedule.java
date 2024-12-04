package org.ytcuber.parser;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.database.model.Group;
import org.ytcuber.database.repository.GroupRepository;
import org.ytcuber.handler.GroupSchedule;
import org.ytcuber.initialization.Initialization;
import org.ytcuber.initialization.InitializationLocations;
import org.ytcuber.initialization.InitializationReplacement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AllSchedule {
    private Initialization initialization;
    private GroupProcessor groupProcessor;
    private GroupRepository groupRepository;
    private InitializationReplacement initializationReplacement;
    private InitializationLocations initializationLocations;
    private GroupSchedule groupSchedule;

    @Autowired
    public void ApplicationInitializer(GroupSchedule groupSchedule, InitializationLocations initializationLocations, GroupProcessor groupProcessor, Initialization initialization, GroupRepository groupRepository, InitializationReplacement initializationReplacement) {
        this.initialization = initialization;
        this.groupProcessor = groupProcessor;
        this.groupRepository = groupRepository;
        this.initializationReplacement = initializationReplacement;
        this.initializationLocations = initializationLocations;
        this.groupSchedule = groupSchedule;
    }

    @PostConstruct
    public void init() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Заполнение групп
        for (int i = 1; i <= 4; i++) {
            groupProcessor.processGroups(String.valueOf(i));
        }

        // Заполнение кабинетов
        initializationLocations.processLocationParse("Cab2");

        // Группа заглушка (для не существующих групп)
        List<Group> groupsToSave = new ArrayList<>();
        Group group = new Group();
        group.setTitle("АХАХА");
        group.setSquad(10);
        groupsToSave.add(group);
        groupRepository.saveAll(groupsToSave);

        // Запуск парсинга замен в отдельном потоке
        Callable<Void> replacementTask = () -> {
            initializationReplacement.processExcelReplacementParse("02.12.24-04.12.24");
            initializationReplacement.processExcelReplacementParse("05.12.24-07.12.24");
            return null;
        };

        // Запуск парсинга расписания в отдельном потоке
        Callable<Void> scheduleTask = () -> {
            int lastId = groupRepository.findLastId() - 1;
            for (int i = 1; i <= lastId; i++) {
                String groupName = String.valueOf(groupRepository.findNameById(i));
                initialization.processExcelParse(groupName);
            }
            return null;
        };

        // Запускаем оба парсинга параллельно
        List<Callable<Void>> tasks = new ArrayList<>();
        tasks.add(replacementTask);
        tasks.add(scheduleTask);

        try {
            // Ожидаем выполнения всех задач
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Обрабатываем прерывание
        } finally {
            executorService.shutdown(); // Закрываем пул потоков
        }
//
//        List<Object> as = groupSchedule.giveSchedule("ИСпПК-21-1", 2, 1);
//
//        // Красивый вывод результирующего списка
//        System.out.println("=== Итоговый список уроков и замен ===");
//        for (Object item : as) {
//            if (item instanceof LessonDTO lesson) {
//                System.out.printf(
//                        "Урок: День недели: %s, Номер: %d, Локация: %s, Предмет: %s, Учитель: %s%n",
//                        lesson.getDayOfWeek(),
//                        lesson.getOrdinal(),
//                        lesson.getLocation(),
//                        lesson.getSubject(),
//                        lesson.getTeacher()
//                );
//            } else if (item instanceof ReplacementDTO replacement) {
//                System.out.printf(
//                        "Замена: День недели: %s, Номер: %d, Локация: %s, Предмет: %s, Учитель: %s%n",
//                        replacement.getDatOfWeek(),
//                        replacement.getOrdinal(),
//                        replacement.getLocation(),
//                        replacement.getSubject(),
//                        replacement.getTeacher()
//                );
//            }
//        }

//        // Заполнение замен
//        initializationReplacement.processExcelReplacementParse("02.12.24-04.12.24");
//
//        // Заполнение расписания
//        int lastId = groupRepository.findLastId() - 1;
//        for (int i = 1; i <= lastId; i++) {
//            String groupName = String.valueOf(groupRepository.findNameById(i));
//            initialization.processExcelParse(groupName);
//        }

//        Нет субботы у 25, 54, 67, Странная фигня у КС-23-1
//        String groupName = String.valueOf(groupRepository.findNameById(38)); initialization.processExcelParse(groupName);
    }
}
