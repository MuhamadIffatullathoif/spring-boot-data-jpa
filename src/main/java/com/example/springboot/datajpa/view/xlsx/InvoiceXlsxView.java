package com.example.springboot.datajpa.view.xlsx;

import com.example.springboot.datajpa.domain.Invoice;
import com.example.springboot.datajpa.domain.ItemInvoice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.util.Map;

@Component("invoice/ver.xlsx")
public class InvoiceXlsxView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // add name for file
        response.setHeader("Content-Disposition","attachment; filename=\"invoice_view.xlsx\"");

        Invoice invoice = (Invoice) model.get("invoice");
        Sheet sheet = workbook.createSheet("Invoice");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Customer Data");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(invoice.getCustomer().getName() + " " + invoice.getCustomer().getLastName());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(invoice.getCustomer().getEmail());

        // chaining method
        sheet.createRow(3).createCell(0).setCellValue("Invoice Data");
        sheet.createRow(4).createCell(0).setCellValue("Invoice ID: " + invoice.getId());
        sheet.createRow(5).createCell(0).setCellValue("Description: " + invoice.getDescription());
        sheet.createRow(6).createCell(0).setCellValue("Date: " + invoice.getCreatedAt());

        // Looping data
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Product");
        header.createCell(1).setCellValue("Price");
        header.createCell(2).setCellValue("Amount");
        header.createCell(3).setCellValue("Total");

        int rownum = 10;
        for (ItemInvoice item: invoice.getItems()) {
            Row rowLoop = sheet.createRow(rownum ++);
            rowLoop.createCell(0).setCellValue(item.getProduct().getName());
            rowLoop.createCell(1).setCellValue(item.getProduct().getPrice());
            rowLoop.createCell(2).setCellValue(item.getAmount());
            rowLoop.createCell(3).setCellValue(item.calculateAmount());
        }

        Row rowTotal = sheet.createRow(rownum);
        rowTotal.createCell(2).setCellValue("Grand Total:");
        rowTotal.createCell(3).setCellValue(invoice.getTotal());
    }
}
