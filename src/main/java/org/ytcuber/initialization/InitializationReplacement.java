package org.ytcuber.initialization;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.database.model.Group;
import org.ytcuber.database.model.Replacement;
import org.ytcuber.database.repository.GroupRepository;
import org.ytcuber.database.repository.LocationRepository;
import org.ytcuber.database.repository.ReplacementRepository;
import org.ytcuber.database.types.DayOfWeek;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class InitializationReplacement {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ReplacementRepository replacementRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    public void ApplicationInitializer(GroupRepository groupRepository, LocationRepository locationRepository) {
        this.groupRepository = groupRepository;
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    private void init() {
        System.out.println("Можно написать инициализацию, но я не буду");
    }

    public void processExcelReplacementParse(String replacementDate) throws IOException, ParseException {
        String inputFilePath = "./mainexcel/replacement/" + replacementDate + ".xlsx";
//        String inputFilePath = "/home/ytcuber/site/mainexcel/replacement/" + replacementDate + ".xlsx";
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(inputFilePath));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        minusUnion(inputFilePath);
        minusUnion(inputFilePath);

        List<Replacement> lessonsList = parseExcelReplacement(myExcelSheet);
        replacementRepository.saveAllAndFlush(lessonsList);
    }

    public void minusUnion(String file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Сохраняем количество объединений, так как список изменяется во время итерации
            int mergedRegionsCount = sheet.getNumMergedRegions();

            // Удаляем все объединённые ячейки
            for (int i = mergedRegionsCount - 1; i >= 0; i--) {
                sheet.removeMergedRegion(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            System.out.println("Все объединённые ячейки успешно разъединены!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Replacement> parseExcelReplacement(XSSFSheet myExcelSheet) throws ParseException {
        final List<Replacement> replacementList = new ArrayList<>();
        int rowId = 1;
        int cellId = 0;
        int mainRowId;
        DayOfWeek dayOfWeek = null;
        String replacementDate;
        int para;
        String replacementString = "";
        int tmp = 1;
        boolean isDay = false;
        int limit = 0;
        int tmpCellId;
        int tmpLimit = 4;
        boolean isEndReplacement = false;
        // Запрашиваем все названия кабинетов один раз и сохраняем в список
        List<String> locationNames = new ArrayList<>();
        int lastId = locationRepository.findLastId();
        for (int i = 1; i <= lastId; i++) {
            locationNames.add(locationRepository.findNameById(i));
        }
        try {
            dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId));
        } catch (Exception ignored) {
        }
        while (dayOfWeek == null) {
            rowId++;
            try {
                dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId));
            } catch (Exception ignored) {
            }
        }
        mainRowId = rowId - 1;
        for (int i = 1; i <= 3; i++) {
            while (!isEndReplacement) {
                dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId)); // День недели
                cellId++;
                replacementDate = myExcelSheet.getRow(rowId).getCell(cellId).getStringCellValue(); // Дата замены
                cellId++;
                tmpCellId = cellId;
                tmpLimit = countLessons(tmpLimit, myExcelSheet, rowId, cellId);
                while (tmp <= tmpLimit) {
                    para = (int) myExcelSheet.getRow(rowId).getCell(cellId).getNumericCellValue(); // Внесение Номер пары
                    cellId++;
                    while (!isDay) {
                        isDay = logicalReplacementAll(locationNames, dayOfWeek, replacementDate, para, replacementString, myExcelSheet, mainRowId, cellId, rowId, limit);
                        cellId++;
                        if (tmp == 1) {
                            boolean isEnd = isEndReplacement(rowId, cellId, myExcelSheet, mainRowId);
                            if (isEnd) {
                                System.out.println("Замен дальше нет");
                                isEndReplacement = true;
                                cellId--;
                                break;
                            }
                        }
                    }
                    if (tmp == 1) {
                        limit = cellId;
                    }
                    isDay = false;
                    cellId = tmpCellId;
                    rowId++;
                    tmp++;
                }
                cellId = limit;
                if(i == 1) {
                    rowId = mainRowId + i;
                } else if (i == 2) {
                    rowId = mainRowId + i + tmpLimit;
                } else if (i == 3) {
                    rowId = mainRowId + i + tmpLimit + tmpLimit;
                }
                tmp = 1;
                limit = 0;
            }
            cellId = 0;
            rowId = mainRowId + 2 + tmpLimit;
            if (i == 2) {
                rowId = mainRowId + 3 + tmpLimit + tmpLimit;
            }
            isEndReplacement = false;
        }
        return replacementList;
    }

    public boolean logicalReplacementAll(List<String> locationNames, DayOfWeek dayOfWeek, String replacementDate, int para, String replacementString, XSSFSheet myExcelSheet, int mainRowId, int cellId, int rowId, int limit) throws ParseException {
        if (limit == 0) {
            logicalReplacement(locationNames, dayOfWeek, replacementDate, para, replacementString, myExcelSheet, mainRowId, cellId, rowId);
            cellId++;
            dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId));
            return dayOfWeek != null;
        } else {
            while (cellId <= limit) {
                logicalReplacement(locationNames, dayOfWeek, replacementDate, para, replacementString, myExcelSheet, mainRowId, cellId, rowId);
                cellId++;
            }
            return true;
        }
    }

    public void logicalReplacement(List<String> locationNames, DayOfWeek dayOfWeek, String replacementDate, int para, String replacementString, XSSFSheet myExcelSheet, int mainRowId, int cellId, int rowId) throws ParseException {
        String teacher = "";
        int subgroup = 0;
        String para1 = "";
        String para2 = "";
        String location = "";
        Replacement replacement = new Replacement();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try { replacementString = myExcelSheet.getRow(rowId).getCell(cellId).getStringCellValue(); } catch (Exception ignored) {} // Внесение Предмет (если есть)
        String subject = replacementString;
        if(replacementString.startsWith("\n")) {
            replacementString = replacementString.replaceFirst("\n", "");
        }
        if(replacementString.endsWith("\n")) {
            replacementString = replacementString.substring(0, replacementString.length() - 1);
        }
        if(replacementString.contains("\n2.")) {
            String[] parts = replacementString.split("\n2.", 2); // Разделяет только на Учителя и оставшееся
            para1 = parts[0];
            para2 = parts[1];
            para2 = "2." + para2;
        }

        // Типа добаление второй пары
        if (!para2.equals("")) {
            if(para2.contains("\n")) {
                try {
                    String[] parts = para2.split("\n", 2); // Разделяет только на Учителя и оставшееся
                    subject = parts[0];
                    teacher = parts[1];
                } catch (Exception ignored) {}
            }
            subject = subject.split(2 + ". ")[1];
            subgroup = 2;

            String groupName = myExcelSheet.getRow(mainRowId).getCell(cellId).getStringCellValue();
            Optional<Group> groupId = groupRepository.findByGroupName(groupName);
            // Группа не найдена в БД
            if (groupId.isEmpty()) {
                groupId = groupRepository.findByGroupName("АХАХА");
            }
            // Группа содержит "/"
            if (groupName.contains("/")) {
                String result = groupName.split("/")[0];
                groupId = groupRepository.findByGroupName(result);
                if (groupId.isEmpty()) {
                    groupId = groupRepository.findByGroupName("АХАХА");
                } else {
                    groupId = groupRepository.findByGroupName(result);
                }
            }

            // Используем сохранённые названия кабинетов для проверки
            for (String locationName : locationNames) {
                if (subject.contains(locationName)) {
                    String[] parts = subject.split(locationName, 2); // Разделяет на предмет и локацию
                    subject = parts[0].trim();
                    location = locationName;
                    break; // Прерываем, так как локация найдена
                }
            }

            replacement.setOrdinal(para);
            replacement.setDatOfWeek(dayOfWeek);
            replacement.setSubject(subject.trim());
            replacement.setDate(new Date(formatter.parse(replacementDate).getTime()));
            replacement.setGroup(groupId.get());

            replacement.setSubgroup(subgroup); // Подгруппа
            replacement.setTeacher(teacher.trim()); // Преподаватель
            replacement.setLocation(location); // Кабинет

            replacementRepository.save(replacement);

            replacement = new Replacement();
        }

        if (!para1.equals("")) {
            subject = para1;
            teacher = "";

            if(para1.contains("\n")) {
                try {
                    String[] parts = para1.split("\n", 2); // Разделяет только на Учителя и оставшееся
                    subject = parts[0];
                    teacher = parts[1];
                } catch (Exception ignored) {}
            }
            try {
                subject = subject.split(1 + ". ")[1];
            } catch (Exception ignored) {}
            subgroup = 1;

            String groupName = myExcelSheet.getRow(mainRowId).getCell(cellId).getStringCellValue();
            Optional<Group> groupId = groupRepository.findByGroupName(groupName);
            // Группа не найдена в БД
            if (groupId.isEmpty()) {
                groupId = groupRepository.findByGroupName("АХАХА");
            }
            // Группа содержит "/"
            if (groupName.contains("/")) {
                String result = groupName.split("/")[0];
                groupId = groupRepository.findByGroupName(result);
                if (groupId.isEmpty()) {
                    groupId = groupRepository.findByGroupName("АХАХА");
                } else {
                    groupId = groupRepository.findByGroupName(result);
                }
            }
            // Используем сохранённые названия кабинетов для проверки
            for (String locationName : locationNames) {
                if (subject.contains(locationName)) {
                    String[] parts = subject.split(locationName, 2); // Разделяет на предмет и локацию
                    subject = parts[0].trim();
                    location = locationName;
                    break; // Прерываем, так как локация найдена
                }
            }

            replacement.setOrdinal(para);
            replacement.setDatOfWeek(dayOfWeek);
            replacement.setSubject(subject.trim());
            replacement.setDate(new Date(formatter.parse(replacementDate).getTime()));
            replacement.setGroup(groupId.get());

            replacement.setSubgroup(subgroup); // Подгруппа
            replacement.setTeacher(teacher.trim()); // Преподаватель
            replacement.setLocation(location); // Кабинет

            replacementRepository.save(replacement);
        } else if(replacementString.contains("\n")) {
            try {
                String[] parts = replacementString.split("\n", 2); // Разделяет только на Учителя и оставшееся
                subject = parts[0];
                teacher = parts[1];
            } catch (Exception ignored) {}
        }
        if (para1.equals("") && para2.equals("")) {
            for (int number = 0; number <= 10; number++) {
                String tmp = number + ". ";
                if (replacementString.startsWith(tmp)) {

                    if (number == 10) {
                        subject = subject.substring(4);
                    } else {
                        subject = subject.substring(3);
                    }
                    subgroup = number;
//                    subject = subject.split(tmp)[1];
                    break;
                }
            }
            if (!replacementString.equals("")) {
                String groupName = myExcelSheet.getRow(mainRowId).getCell(cellId).getStringCellValue();
                Optional<Group> groupId = groupRepository.findByGroupName(groupName);
                // Группа не найдена в БД
                if (groupId.isEmpty()) {
                    groupId = groupRepository.findByGroupName("АХАХА");
                }
                // Группа содержит "/"
                if (groupName.contains("/")) {
                    String result = groupName.split("/")[0];
                    groupId = groupRepository.findByGroupName(result);
                    if (groupId.isEmpty()) {
                        groupId = groupRepository.findByGroupName("АХАХА");
                    } else {
                        groupId = groupRepository.findByGroupName(result);
                    }
                }
                // Используем сохранённые названия кабинетов для проверки
                for (String locationName : locationNames) {
                    if (subject.contains(locationName)) {
                        String[] parts = subject.split(locationName, 2); // Разделяет на предмет и локацию
                        subject = parts[0].trim();
                        location = locationName;
                        break; // Прерываем, так как локация найдена
                    }
                }
                replacement.setOrdinal(para);
                replacement.setDatOfWeek(dayOfWeek);
                replacement.setSubject(subject.trim());
                replacement.setDate(new Date(formatter.parse(replacementDate).getTime()));
                replacement.setGroup(groupId.get());

                replacement.setSubgroup(subgroup); // Подгруппа
                replacement.setTeacher(teacher.trim()); // Преподаватель
                replacement.setLocation(location); // Кабинет

                replacementRepository.save(replacement);
            }
        }
    }

    public int countLessons(Integer tmpLimit, XSSFSheet myExcelSheet, int rowId, int cellId) {
        int tmpLimitInt;
        tmpLimitInt = (int) myExcelSheet.getRow(rowId).getCell(cellId).getNumericCellValue();
        while (tmpLimitInt != 0) {
            try {
                Cell cell = myExcelSheet.getRow(rowId).getCell(cellId);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    tmpLimitInt = (int) cell.getNumericCellValue();
                    tmpLimit = tmpLimitInt;
                } else {
                    tmpLimitInt = 0; // Не числовое значение
                }
            } catch (Exception e) {
                // Обработка ошибки, если ячейка недоступна
                tmpLimitInt = 0;
            }
            rowId++;
        }
        return tmpLimit;
    }

    public boolean isEndReplacement(Integer rowId, Integer cellId, XSSFSheet myExcelSheet, Integer mainRowId) {
        Cell tmpCell = myExcelSheet.getRow(mainRowId).getCell(cellId);
        DayOfWeek dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId));
        String groupName;
        if (tmpCell == null || tmpCell.getCellType() != CellType.STRING) {
            groupName = null;
        } else {
            groupName = tmpCell.getStringCellValue();
        }

        return dayOfWeek == null && groupName == null;
    }

    public DayOfWeek parseDayOfWeek(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.STRING) return null;

        return switch (cell.getStringCellValue()) {
            case "Понедельник" -> DayOfWeek.MONDAY;
            case "Вторник" -> DayOfWeek.TUESDAY;
            case "Среда" -> DayOfWeek.WEDNESDAY;
            case "Четверг" -> DayOfWeek.THURSDAY;
            case "Пятница" -> DayOfWeek.FRIDAY;
            case "Суббота" -> DayOfWeek.SATURDAY;
            default -> null;
        };
    }
}