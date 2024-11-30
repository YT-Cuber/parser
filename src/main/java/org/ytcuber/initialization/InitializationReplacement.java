package org.ytcuber.initialization;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.database.model.Group;
import org.ytcuber.database.model.Replacement;
import org.ytcuber.parser.GroupProcessor;
import org.ytcuber.database.repository.GroupRepository;
import org.ytcuber.database.repository.ReplacementRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        List<Replacement> lessonsList = parseExcel(myExcelSheet, null);
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

    public List<Replacement> parseExcel(XSSFSheet myExcelSheet, Group groupId) {
        final List<Replacement> replacementList = new ArrayList<>();
        int rowId = 1;
        int cellId = 0;

        return replacementList;
    }

}