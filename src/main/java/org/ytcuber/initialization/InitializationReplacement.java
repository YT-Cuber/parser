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
import org.ytcuber.database.repository.ReplacementRepository;
import org.ytcuber.database.types.DayOfWeek;
import org.ytcuber.parser.GroupProcessor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
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
    private Initialization initialization;
    private GroupProcessor groupProcessor;

    @Autowired
    public void ApplicationInitializer(GroupProcessor groupProcessor, GroupRepository groupRepository) {
        this.groupProcessor = groupProcessor;
        this.groupRepository = groupRepository;
    }

    @PostConstruct
    public void init() throws IOException {

        String replacementDate = "28.11.24-30.11.24";
        String inputFilePath = "./mainexcel/replacement/" + replacementDate + ".xlsx";
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(inputFilePath));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        minusUnion(inputFilePath);
        minusUnion(inputFilePath);

        List<Replacement> lessonsList = parseExcelReplacement(myExcelSheet, null);
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

    public List<Replacement> parseExcelReplacement(XSSFSheet myExcelSheet, Group groupId) {
        final List<Replacement> replacementList = new ArrayList<>();
        int rowId = 1;
        int cellId = 0;
        int mainRowId;
        Cell tmpCell;
        DayOfWeek dayOfWeek = null;
        String replacementDate = null;
        int para = 0;
        String replacementString = "";
        String groupName = null;
        int tmp = 0;
        Replacement replacement = new Replacement();

        try { dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId)); } catch (Exception ignored) {}
        while (dayOfWeek == null) {
            rowId++;
            try { dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId)); } catch (Exception ignored) {}
        }
        mainRowId = rowId - 1;
        dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId)); // День недели
        cellId += 1;
        replacementDate = myExcelSheet.getRow(rowId).getCell(cellId).getStringCellValue(); // Дата замены
//        while (tmp == 0) {
//        tmpCell = myExcelSheet.getRow(rowId).getCell(cellId);
        logicalReplacementAll(replacement, dayOfWeek, replacementDate, para, replacementString, groupName, myExcelSheet, mainRowId, cellId, rowId);
//        dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId));
//        cellId += 1;
//        replacementDate = myExcelSheet.getRow(rowId).getCell(cellId).getStringCellValue(); // Внесение Даты замены
//        cellId += 1;
//        para = (int) myExcelSheet.getRow(rowId).getCell(cellId).getNumericCellValue(); // Внесение Номер пары
//        cellId += 1;
//        try { replacementString = myExcelSheet.getRow(rowId).getCell(cellId).getStringCellValue(); } catch (Exception ignored) {} // Внесение Предмет (если есть)
//        groupName = myExcelSheet.getRow(mainRowId).getCell(cellId).getStringCellValue();

//        }
        return replacementList;
    }

    public void logicalReplacementAll(Replacement replacement, DayOfWeek dayOfWeek, String replacementDate, int para, String replacementString, String groupName, XSSFSheet myExcelSheet, int mainRowId, int cellId, int rowId) {
        logicalReplacement(replacement, dayOfWeek, replacementDate, para, replacementString, groupName, myExcelSheet, mainRowId, cellId, rowId);
        cellId += 3;
        dayOfWeek = parseDayOfWeek(myExcelSheet.getRow(rowId).getCell(cellId));
        if(dayOfWeek == null) {

        }
    }

    public void logicalReplacement(Replacement replacement, DayOfWeek dayOfWeek, String replacementDate, int para, String replacementString, String groupName, XSSFSheet myExcelSheet, int mainRowId, int cellId, int rowId) {
        cellId += 1;
        para = (int) myExcelSheet.getRow(rowId).getCell(cellId).getNumericCellValue(); // Внесение Номер пары
        cellId += 1;
        try { replacementString = myExcelSheet.getRow(rowId).getCell(cellId).getStringCellValue(); } catch (Exception ignored) {} // Внесение Предмет (если есть)
        groupName = myExcelSheet.getRow(mainRowId).getCell(cellId).getStringCellValue();
        Optional<Group> groupId = groupRepository.findByGroupName(groupName);
        if (replacementString != null) {
            replacement.setOrdinal(para);
            replacement.setDatOfWeek(dayOfWeek);
            replacement.setSubject(replacementString);
            replacement.setDate(Date.valueOf(replacementDate));
            replacement.setGroup(groupId.get());
            replacementRepository.save(replacement);
        }
//        private Integer subgroup; // Подгруппа
//        private String teacher; // Преподаватель
//        private String location; // Кабинет
//        private Date date; // Дата замены
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