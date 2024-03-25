package org.ytcuber.parser;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTableColumn;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.model.Group;
import org.ytcuber.repository.GroupRepository;

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

@AllArgsConstructor
@NoArgsConstructor
@Component
public class Initialization {

    @Autowired
    private GroupRepository groupRepository;

    @PostConstruct
    public void init() throws IOException {
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
        minusUnion("ИСпПК-21-1" + ".xlsx");
        parseExcel("ИСпПК-21-1" + ".xlsx");
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
                        // Убираем объединение только если ячейки объединены вертикально
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
            System.out.println();
            System.out.println("Объединённые ячейки разъединены успешно.");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseExcel(String file) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./mainexcel/squad2/" + file));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        XSSFRow row2 = myExcelSheet.getRow(2);
        System.out.println("!!! " + new String(row2.getCell(0).getStringCellValue().getBytes(StandardCharsets.UTF_8)));
        System.out.println();
    }
}

