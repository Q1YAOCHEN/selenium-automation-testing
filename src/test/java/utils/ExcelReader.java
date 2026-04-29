package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Utility class to read test data from Excel files
public class ExcelReader {

    private static final String FILE_PATH = "test-data/TestData.xlsx";

    // Reads key-value pairs from LoginData sheet
    // Password is stored as Base64 encoded for security
    public static Map<String, String> getLoginData() throws IOException {
        Map<String, String> data = new HashMap<>();
        FileInputStream fis = new FileInputStream(FILE_PATH);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("LoginData");

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String key = getCellValue(row.getCell(0));
                String value = getCellValue(row.getCell(1));
                // Decode Base64 password
                if (key.equals("password")) {
                    value = new String(java.util.Base64.getDecoder().decode(value));
                }
                data.put(key, value);
            }
        }
        workbook.close();
        fis.close();
        return data;
    }

    // Reads event data from EventData sheet
    // Returns a 2D array: each row is one event
    public static String[][] getEventData() throws IOException {
        FileInputStream fis = new FileInputStream(FILE_PATH);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("EventData");

        int rowCount = sheet.getLastRowNum();
        int colCount = sheet.getRow(0).getLastCellNum();
        String[][] data = new String[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = getCellValue(row.getCell(j));
            }
        }
        workbook.close();
        fis.close();
        return data;
    }

    // Helper method to get cell value as String
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Format date as "MMM d, yyyy" e.g. "Apr 5, 2026"
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM d, yyyy");
                    return sdf.format(cell.getDateCellValue());
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}