package org.ytcuber.initialization;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.database.model.Location;
import org.ytcuber.database.repository.LocationRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class InitializationLocations {
    private LocationRepository locationRepository;

    @Autowired
    public void ApplicationInitializer(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    private void init() {
        System.out.println("Можно написать инициализацию, но я не буду");
    }

    public void processLocationParse(String fileName) throws IOException {

        String inputFilePath = "./mainexcel/locations/" + fileName + ".xlsx";
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(inputFilePath));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);

        List<Location> locationList = parseLocationExcel(myExcelSheet);
//        locationRepository.saveAllAndFlush(locationList);
    }

    public List<Location> parseLocationExcel(XSSFSheet myExcelSheet) {
        final List<Location> locationList = new ArrayList<>();
        int rowId = 2;
        int cellId = 0;
        Location location;
        String locationName = "";
        String locationType = "";
        boolean tmpLoc = false;
            while (!tmpLoc) {
                location = new Location();
                locationName = myExcelSheet.getRow(rowId).getCell(cellId).getStringCellValue().trim();
//                locationType = myExcelSheet.getRow(rowId).getCell(cellId + 1).getStringCellValue().trim();
                location.setLocation(locationName);
//                location.setType(locationType);

                locationRepository.save(location);

                rowId++;

                Row rowT = myExcelSheet.getRow(rowId);
                Cell cell = null;
                if (rowT != null) {
                    cell = rowT.getCell(cellId);
                }

                // Проверка, является ли ячейка null или содержит пустую строку
                if (cell == null ||
                        (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) ||
                        (cell.getCellType() == CellType.BLANK)) {
                    tmpLoc = true;
                }
            }
        return locationList;
    }

}
