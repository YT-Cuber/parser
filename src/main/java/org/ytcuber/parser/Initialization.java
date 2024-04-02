package org.ytcuber.parser;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.model.Group;
import org.ytcuber.model.Lesson;
import org.ytcuber.repository.GroupRepository;
import org.ytcuber.repository.LessonRepository;
import org.ytcuber.types.DayOfWeek;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class Initialization {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        try {
            // Указываем URL-адрес веб-страницы
            Document doc = Jsoup.connect("https://newlms.magtu.ru/mod/folder/view.php?id=1223699").get();

            // Извлекаем все элементы с определенным селектором
            Elements elements = doc.select("a");

            boolean startPrinting = false;
            // Перебираем все извлеченные элементы и выводим их на экран
            for (Element element : elements) {
                String title = element.text(); // Название файла
                String link = element.absUrl("href"); // Ссылка на скачивание файла

                if (link.equals("https://newlms.magtu.ru/pluginfile.php/1936752/mod_folder/content/0/%D0%98%D0%A1%D0%BF%D0%92-20-1.xlsx?forcedownload=1")) {
                    startPrinting = true;
                }

                if (title.equals("◄ ОТДЕЛЕНИЕ № 1 «ОБЩЕОБРАЗОВАТЕЛЬНАЯ ПОДГОТОВКА»")) {
                    break;
                }

                if (startPrinting && !title.endsWith(".pdf")) {
                    System.out.println(new String(title.getBytes(StandardCharsets.UTF_8)));
                    System.out.println();
                    // System.out.println("Ссылка: " + link);

                    // Скачиваем файл
                    try (InputStream in = new URL(link).openStream()) {
                        Files.copy(in, Path.of("C:/My_Space/More/Java/parser/mainexcel/squad2/" + title), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Group groups = new Group();
                    if (title.endsWith(".xlsx")) {
                        title = title.substring(0, title.length() - 5);
                    }
                    groups.setTitle(title);
                    groups.setSquad(2);

                    groupRepository.save(groups);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputFile = "ИСпПК-21-1";
        minusUnion(inputFile + ".xlsx");
        minusUnion(inputFile + ".xlsx");
        Thread.sleep(1000);
//         XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + inputFile + ".xlsx"));
//         parseExcel(myExcelBook);

        List<Lesson> lessonsList = parseExcel(inputFile + ".xlsx");

        lessonRepository.saveAllAndFlush(lessonsList);

        Thread.sleep(2000);
    }

    public void minusUnion(String file) {
        String filePath = "./mainexcel/squad2/" + file;

        List<Integer> columnsToUnmerge = Arrays.asList(0, 6, 12); // Столбцы A, G, M

        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
                if (mergedRegion != null) {
                    int firstRow = mergedRegion.getFirstRow();
                    int lastRow = mergedRegion.getLastRow();
                    int firstColumn = mergedRegion.getFirstColumn();
                    int lastColumn = mergedRegion.getLastColumn();

                    if (columnsToUnmerge.contains(firstColumn) && firstColumn == lastColumn) {
                        sheet.removeMergedRegion(i);
                        for (int row = firstRow; row <= lastRow; row++) {
                            Row r = sheet.getRow(row);
                            Cell c = r.getCell(firstColumn);
                            if (c == null) {
                                r.createCell(firstColumn);
                            }
                        }
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            System.out.println("Объединённые ячейки разъединены успешно!");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Lesson> parseExcel(String file) throws IOException {
        int tmpSub = 1;
        int odd = 0;
        final List<Lesson> lessonsList = new ArrayList<>();
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        int rowId = 1;
        int cellId = 0;
        int u = 1;
        DayOfWeek datOfWeek;

        Lesson lesson = new Lesson();
        int weekOdd;
        DayOfWeek dayOfWeek;

        for (int i = 1; i <= 3; i++) {

            while (u <= 2) {
                // Проверка недели
//                viewString(file, rowId, cellId);
                weekOdd = weekOdd(file, odd, rowId, cellId);

                int tmpDay = 1;

                while (tmpDay <= 2) {
                    rowId++;
                    // Проверка и внесение из enum дня
//                    viewString(file, rowId, cellId);
                    dayOfWeek = weekDay(file, rowId, cellId);
                    datOfWeek = weekDay(file, rowId, cellId);

                    rowId++;
                    boolean tmpLes = false;
                    while (!tmpLes) {
//                        viewNumeric(file, rowId, cellId);
                        lesson.setOrdinal((int) myExcelSheet.getRow(rowId).getCell(cellId).getNumericCellValue()); // Внесение Номер пары
                        int para = (int) myExcelSheet.getRow(rowId).getCell(cellId).getNumericCellValue(); // Запоминание пары

                        lesson.setOdd(weekOdd);
                        lesson.setDayOfWeek(datOfWeek);

                        logicalAll(file, lesson, rowId, cellId, tmpSub, para, weekOdd, datOfWeek);
                        lesson = new Lesson();
                        rowId += 2;

                        Cell cell = myExcelSheet.getRow(rowId).getCell(cellId);
                        dayOfWeek = parseDayOfWeek(cell);

                        if (cell == null) {
                            tmpLes = true;
                        } else if (dayOfWeek != null) {
                            tmpLes = true;
                        }
                    }
                    boolean isEndDayOfWeek = endDayOfWeek(datOfWeek);
//                    if (u <= 2 && isEndDayOfWeek) {
                        while (tmpLes) { // Проверка, что там написан день недели или Тип недели
                            Row row = myExcelSheet.getRow(rowId);

                            if (row == null) {
                                rowId++;
                                continue;
                            }

                            Cell cell = row.getCell(cellId);
                            dayOfWeek = parseDayOfWeek(cell);

//                    boolean isOddWeek = cell % 2 != 0;
                            boolean isOddWeek = parseOddWeek(cell);

                            if (!isOddWeek) {
                                tmpLes = false;
                                rowId++;
                            } else if (dayOfWeek != null) {
                                tmpLes = false;
                            } else {
                                rowId += 2;
                            }
                        }
                        tmpDay++;
                        rowId--;
                    }
//                }
//                u++;
            }
//     lessonsList.add(lesson);
        }
        return lessonsList;
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

    public boolean endDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case THURSDAY, FRIDAY, SATURDAY -> false;
            default -> true;
        };
    }

    public boolean parseOddWeek(Cell cell) {
        if (cell == null) return true;

        return switch (cell.getStringCellValue()) {
            case "Нечетная неделя" -> false;
            case "Четная неделя" -> false;
            default -> true;
        };
    }

    public void logicalAll(String file, Lesson lesson, int row, int cell, int tmpSub, int para1, int weekOdd, DayOfWeek datOfWeek) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        cell++;
        if (!Objects.equals(myExcelSheet.getRow(row).getCell(cell).getStringCellValue(), "")) {

            String cellValue = myExcelSheet.getRow(row).getCell(cell).getStringCellValue();
            tmpSub = cellVal(cellValue, tmpSub); // Проверка какая пара, Общая/Не Общая
            if (tmpSub == 0) {
                lesson.setSubgroup(tmpSub);
//                viewString(file, row, cell);
                lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

                row++;
//                viewString(file, row, cell);
                lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

                cell += 3;
//                viewString(file, row, cell);
                lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
                System.out.println();

                lessonRepository.save(lesson);
            } else {
                logicalSub1(file, lesson, row, cell, para1, weekOdd, datOfWeek);
            }

        } else {
            cell += 2;
            logicalSub2(file, lesson, row, cell, para1, weekOdd, datOfWeek);
        }

    }

    public void logicalSub1(String file, Lesson lesson, int row, int cell, int para, int weekOdd, DayOfWeek datOfWeek) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        if (!Objects.equals(myExcelSheet.getRow(row).getCell(cell).getStringCellValue(), "")) {
            lesson.setSubgroup(1);

//            insertDB(file, lesson, row, cell);
//            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

            row++;
//            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell++;
//            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
//            System.out.println();

            lessonRepository.save(lesson);

            cell++;
            row--;
            logicalSub2(file, lesson, row, cell, para, weekOdd, datOfWeek);
        } else {
            cell += 2;
            logicalSub2(file, lesson, row, cell, para, weekOdd, datOfWeek);
        }
    }

    public void logicalSub2(String file, Lesson lesson, int row, int cell, int para, int weekOdd, DayOfWeek datOfWeek) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        lesson = new Lesson();
        if (!Objects.equals(myExcelSheet.getRow(row).getCell(cell).getStringCellValue(), "")) {
            lesson.setOdd(weekOdd);
            lesson.setDayOfWeek(datOfWeek);

            lesson.setOrdinal(para);
            lesson.setSubgroup(2);

//            insertDB(file, lesson, row, cell);
//            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

            row++;
//            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell++;
//            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
//            System.out.println();

            lessonRepository.save(lesson);
        }
    }


    public void insertDB(String file, Lesson lesson, int row, int cell) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

        row++;
        lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

        cell++;
        lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета

        lessonRepository.save(lesson);
    }

//    public void viewString(String file, int row, int cell) throws IOException {
//        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
//        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
//        System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
//    }

//    public void viewNumeric(String file, int row, int cell) throws IOException {
//        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
//        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
//        System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getNumericCellValue());
//    }

    public int weekOdd(String file, int odd, int row, int cell) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        switch (myExcelSheet.getRow(row).getCell(cell).getStringCellValue()) {
            case "Нечетная неделя" -> //lesson.setOdd(odd);
                    odd = 1;
            case "Четная неделя" -> //lesson.setOdd(odd);
                    odd = 2;
            default -> {
                System.err.println("!");
                System.err.println("Ошибка!");
                System.err.println("Неделя не опознана!");
                System.err.println("!");
            }
        }
        return odd;
    }

    public DayOfWeek weekDay(String file, int row, int cell) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        String weekDayRawValue = myExcelSheet.getRow(row).getCell(cell).getStringCellValue();

        return DayOfWeek.valueOfLabel(weekDayRawValue);
    }

    public int cellVal(String cellValue, int tmpSub) {
        if (cellValue.startsWith("(КП)") | cellValue.startsWith("(Лаб)") | cellValue.startsWith("(Пр)") | cellValue.startsWith("(Ин.яз)")) {
            tmpSub = 1;
        } else {
            tmpSub = 0;
        }
        return tmpSub;
    }

//    public int initial(int tmpSub, int tmp) {
//        switch (tmpSub) {
//            case 0 -> {
//                tmp = 2;
//                return tmp;
//            }
//            case 1 -> {
//                tmp = 0;
//                return tmp;
//            }
//            case 2 -> {
//                tmp = 2;
//                return tmp;
//            }
//            default -> {
//                System.err.println("! ОШИБКА ИНИЦИАЛИЗАЦИИ ! ");
//                System.err.println("! ОШИБКА ! ");
//                System.err.println("! ОШИБКА ИНИЦИАЛИЗАЦИИ ! ");
//            }
//        }
//        return tmpSub;
//    }
}

