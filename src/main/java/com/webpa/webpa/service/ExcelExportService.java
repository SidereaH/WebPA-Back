package com.webpa.webpa.service;

import com.webpa.webpa.models.ProductCard;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

// ExcelExportService.java
@Service
public class ExcelExportService {
    public byte[] exportToExcel(List<ProductCard> products) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Products");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Name", "Price", "Marketplace", "URL"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            
            // Create data rows
            int rowNum = 1;
            for (ProductCard product : products) {
                Row row = sheet.createRow(rowNum++);
                
                // Safely handle potential null values
                row.createCell(0).setCellValue(product.getId() != null ? product.getId() : 0);
                row.createCell(1).setCellValue(product.getName() != null ? product.getName() : "");
                row.createCell(2).setCellValue(product.getPrice() != null ? product.getPrice() : 0.0);
                row.createCell(3).setCellValue(product.getMarketplace() != null ? product.getMarketplace() : "");
                row.createCell(4).setCellValue(product.getUrl() != null ? product.getUrl() : "");
            }
            
            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export products to Excel", e);
        }
    }
}