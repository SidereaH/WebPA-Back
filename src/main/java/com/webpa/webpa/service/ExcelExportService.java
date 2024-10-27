package com.webpa.webpa.service;

import com.webpa.webpa.ProductCard;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {
    
    public byte[] exportToExcel(List<ProductCard> products) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            // Create header row
            Row headerRow = sheet.createRow(0);
            createHeaderCell(headerRow, 0, "ID");
            createHeaderCell(headerRow, 1, "Name");
            createHeaderCell(headerRow, 2, "Price");
            createHeaderCell(headerRow, 3, "Marketplace");
            createHeaderCell(headerRow, 4, "URL");
            createHeaderCell(headerRow, 5, "Main Info");
            createHeaderCell(headerRow, 6, "Main Characteristics");
            createHeaderCell(headerRow, 7, "Additional Information");
            createHeaderCell(headerRow, 8, "Description");

            // Create data rows
            int rowNum = 1;
            for (ProductCard product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getPrice());
                row.createCell(3).setCellValue(product.getMarketplace());
                row.createCell(4).setCellValue(product.getUrl());
                row.createCell(5).setCellValue(mapToString(product.getMainInfo()));
                row.createCell(6).setCellValue(mapToString(product.getMainCharacteristics()));
                row.createCell(7).setCellValue(mapToString(product.getAdditionalInformation()));
                row.createCell(8).setCellValue(product.getDescription());
            }

            // Autosize columns
            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeaderCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        // You can add styling here if needed
    }

    private String mapToString(Map<String, Object> map) {
        if (map == null) return "";
        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> sb.append(key).append(": ").append(value).append(", "));
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }
}