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
import java.util.Arrays;
import java.util.List;

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
        // XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + inputFile + ".xlsx"));
        // parseExcel(myExcelBook);
        parseExcel(inputFile + ".xlsx");
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

    public void parseExcel(String file) throws IOException {
        int tmpSub = 1;
        int odd = 0;
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        Lesson lesson = new Lesson();
        int row = 1;
        int cell = 0;

        // Проверка недели
        viewString(file, row, cell);
        weekOdd(lesson, file, odd, row, cell);

//        int week = ;

        row++;
        // Проверка и внесение из enum дня
        viewString(file, row, cell);
        weekDay(lesson, file, row, cell);

//        int day =     ; // Запоминание дня

        row++;

        viewNumeric(file, row, cell);
        lesson.setOrdinal((int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue()); // Внесение Номер пары

        int para = (int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue(); // Запоминание пары

        logicalAll(file, lesson, row, cell, tmpSub, para);

        lesson = new Lesson();

        row = row + 2;
        viewNumeric(file, row, cell);
        lesson.setOrdinal((int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue()); // Внесение Номер пары

        para = (int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue(); // Запоминание пары

        logicalAll(file, lesson, row, cell, tmpSub, para);

        lesson = new Lesson();

        row = row + 2;
        viewNumeric(file, row, cell);
        lesson.setOrdinal((int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue()); // Внесение Номер пары

        para = (int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue(); // Запоминание пары

        logicalAll(file, lesson, row, cell, tmpSub, para);

     /*
        if (myExcelSheet.getRow(row).getCell(cell).getStringCellValue() != null) {
            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет
            String cellValue = myExcelSheet.getRow(row).getCell(cell).getStringCellValue();
            cellVal(cellValue, lesson, tmpSub); // Проверка какая пара, Общая/Не Общая

            initial(tmpSub, tmp); // Проверка инициализации подгруппы

            row++;
            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель
            cell = cell + 3 + tmp;
            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
            System.out.println();

        } else {
            tmpSub = 2;
            lesson.setSubgroup(tmpSub);
            cell = cell + 2;
            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет
            row++;
            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель
            cell = cell + 3 + tmp;
            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()));
            System.out.println();

        }
        lessonRepository.save(lesson); // Сохранение в БД

        lesson = new Lesson();

        row++;
        cell = cell - 4;

        viewNumeric(file, row, cell);
        lesson.setOrdinal((int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue()); // Внесение Номер пары

        cell++;
        if (myExcelSheet.getRow(row).getCell(cell).getStringCellValue() != null) {
            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

            String cellValue = myExcelSheet.getRow(row).getCell(cell).getStringCellValue();
            cellVal(cellValue, lesson, tmpSub); // Проверка какая пара, Общая/Не Общая

            initial(tmpSub, tmp); // Проверка инициализации подгруппы

            row++;
            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell = cell + 1 + tmp;
            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
            System.out.println();

        } else {
            tmpSub = 2;
            lesson.setSubgroup(tmpSub);
            cell = cell + 2;
            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

            row++;
            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell = cell + 3 + tmp;
            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()));
            System.out.println();
        }*/
        lessonRepository.save(lesson); // Сохранение в БД

    }

    public void logicalAll(String file, Lesson lesson, int row, int cell, int tmpSub, int para) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        cell++;
        if (myExcelSheet.getRow(row).getCell(cell).getStringCellValue() != null){

            String cellValue = myExcelSheet.getRow(row).getCell(cell).getStringCellValue();
            tmpSub = cellVal(cellValue, tmpSub); // Проверка какая пара, Общая/Не Общая
            if (tmpSub == 0) {
                lesson.setSubgroup(tmpSub);
                viewString(file, row, cell);
                lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

                row++;
                viewString(file, row, cell);
                lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

                cell = cell + 3;
                viewString(file, row, cell);
                lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
                System.out.println();

                lessonRepository.save(lesson);
            } else {
                logicalSub1(file, lesson, row, cell, para);
            }

        } else {
            cell = cell + 2;
            logicalSub2(file, lesson, row, cell, para);
        }

    }

    public void logicalSub1(String file, Lesson lesson, int row, int cell, int para) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        if(myExcelSheet.getRow(row).getCell(cell).getStringCellValue() != null) {
            lesson.setSubgroup(1);

            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

            row++;
            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell++;
            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
            System.out.println();

            lessonRepository.save(lesson);

            cell++;
            row--;
            logicalSub2(file, lesson, row, cell, para);
        } else {
            cell = cell + 2;
            logicalSub2(file, lesson, row, cell, para);
        }
    }

    public void logicalSub2(String file, Lesson lesson, int row, int cell, int para) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        lesson = new Lesson();
        if(myExcelSheet.getRow(row).getCell(cell).getStringCellValue() != null) {
            lesson.setOrdinal(para);
            lesson.setSubgroup(2);

            viewString(file, row, cell);
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

            row++;
            viewString(file, row, cell);
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell++;
            viewString(file, row, cell);
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Кабинета
            System.out.println();

            lessonRepository.save(lesson);
        }
    }


    public void viewString(String file, int row, int cell) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
    }

    public void viewNumeric(String file, int row, int cell) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getNumericCellValue());
    }

    public int weekOdd(Lesson lesson, String file, int odd, int row, int cell) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        switch (myExcelSheet.getRow(row).getCell(cell).getStringCellValue()) {
            case "Нечетная неделя" -> {
                odd = 1;
                lesson.setOdd(odd);
            }
            case "Четная неделя" -> {
                odd = 2;
                lesson.setOdd(odd);
            }
            default -> {
                System.err.println("!");
                System.err.println("Ошибка!");
                System.err.println("Неделя не опознана!");
                System.err.println("!");
            }
        }
        return odd;
    }

    public void weekDay(Lesson lesson, String file, int row, int cell) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        switch (myExcelSheet.getRow(row).getCell(cell).getStringCellValue()) {
            case "Понедельник" -> lesson.setDatOfWeek(DayOfWeek.MONDAY);
            case "Вторник" -> lesson.setDatOfWeek(DayOfWeek.TUESDAY);
            case "Среда" -> lesson.setDatOfWeek(DayOfWeek.WEDNESDAY);
            case "Четверг" -> lesson.setDatOfWeek(DayOfWeek.THURSDAY);
            case "Пятница" -> lesson.setDatOfWeek(DayOfWeek.FRIDAY);
            case "Суббота" -> lesson.setDatOfWeek(DayOfWeek.SATURDAY);
            default -> {
                System.err.println("!");
                System.err.println("Ошибка!");
                System.err.println("День не опознан!");
                System.err.println("!");
            }
        }
    }

    public int cellVal(String cellValue, int tmpSub) {
        if (cellValue.startsWith("(КП)") | cellValue.startsWith("(Лаб)") | cellValue.startsWith("(Пр)")| cellValue.startsWith("(Ин.яз)")) {
            tmpSub = 1;
        } else {
            tmpSub = 0;
        }
        return tmpSub;
    }

    public int initial(int tmpSub, int tmp) {
        switch (tmpSub) {
            case 0 -> {
                tmp = 2;
                return tmp;
            }
            case 1 -> {
                tmp = 0;
                return tmp;
            }
            case 2 -> {
                tmp = 2;
                return tmp;
            }
            default -> {
                System.err.println("! ОШИБКА ИНИЦИАЛИЗАЦИИ ! ");
                System.err.println("! ОШИБКА ! ");
                System.err.println("! ОШИБКА ИНИЦИАЛИЗАЦИИ ! ");
            }
        }
        return tmpSub;
    }
}

