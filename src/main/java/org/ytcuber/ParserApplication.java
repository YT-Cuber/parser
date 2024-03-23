package org.ytcuber;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.ytcuber.model.Lesson;
import org.ytcuber.parser.ExcelProperties;
import org.ytcuber.parser.TestParser;
import org.ytcuber.repository.LessonRepository;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class ParserApplication {

	public static void main(String[] args) throws IOException {
//		new ParserApplication();
		SpringApplication.run(ParserApplication.class, args);
//		new TestParser().parse();
	}

	ParserApplication() throws IOException {
//		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("./excel_test/ИСпПК-21-1.xlsx"));
//		XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
//		XSSFRow row = myExcelSheet.getRow(3);
//		System.out.println("11 " + new String(row.getCell(0).getStringCellValue().getBytes(StandardCharsets.UTF_8)));
//		System.out.println();
	}
}
