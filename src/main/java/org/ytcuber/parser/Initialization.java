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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hibernate.sql.InFragment.NULL;

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
                                c = r.createCell(firstColumn);
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
        int tmp = 0;
        int tmpSub = 1;
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        Lesson lesson = new Lesson();
        int row = 1;
        int cell = 6;

            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
        switch (myExcelSheet.getRow(row).getCell(cell).getStringCellValue()) {
            case "Нечетная неделя" -> lesson.setOdd(1);
            case "Четная неделя" -> lesson.setOdd(2);
            default -> {
                System.err.println("!");
                System.err.println("Ошибка!");
                System.err.println("Неделя не опознана!");
                System.err.println("!");
            }
        }

        row++;
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
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

        row++;
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getNumericCellValue());
        lesson.setOrdinal((int) myExcelSheet.getRow(row).getCell(cell).getNumericCellValue()); // Внесение Номер пары

        cell++;
        if (Objects.equals(myExcelSheet.getRow(row).getCell(cell).getStringCellValue(), NULL)) {
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет


            String cellValue = myExcelSheet.getRow(row).getCell(cell).getStringCellValue();
            switch (cellValue) {
                case "(КП)", "(Лаб)", "(Пр)", "(Ин.яз)" -> lesson.setSubgroup(tmpSub);
                default -> lesson.setSubgroup(0);
            }

            row++;
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell = cell + 3 + tmp;
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()));
            System.out.println();

            lessonRepository.save(lesson);
        } else {
            cell = cell + 2;
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
            lesson.setSubject(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue())); // Внесение Предмет

            row++;
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
            lesson.setTeacher(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()); // Внесение Преподаватель

            cell = cell + 3 + tmp;
            System.out.println("!!! " + myExcelSheet.getRow(row).getCell(cell).getStringCellValue());
            lesson.setLocation(String.valueOf(myExcelSheet.getRow(row).getCell(cell).getStringCellValue()));
            System.out.println();

            lessonRepository.save(lesson);
        }
    }
}

