package com.example.springboot.datajpa.view.pdf;

import com.example.springboot.datajpa.domain.Invoice;
import com.example.springboot.datajpa.domain.ItemInvoice;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.Map;

@Component("invoice/ver")
public class InvoicePdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Invoice invoice = (Invoice) model.get("invoice");

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);
        table.addCell("Customer Data");
        table.addCell(invoice.getCustomer().getName() + " " + invoice.getCustomer().getLastName());
        table.addCell(invoice.getCustomer().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table.setSpacingAfter(20);
        table2.addCell("Invoice Data");
        table2.addCell("Invoice: " + invoice.getId());
        table2.addCell("Description: " + invoice.getDescription());
        table2.addCell("Date: " + invoice.getCreatedAt());

        document.add(table);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(4);
        table3.addCell("Product");
        table3.addCell("Price");
        table3.addCell("Amount");
        table3.addCell("Total");

        for (ItemInvoice item: invoice.getItems()) {
            table3.addCell(item.getProduct().getName());
            table3.addCell(item.getProduct().getPrice().toString());
            table3.addCell("No data available");
            table3.addCell(item.calculateAmount().toString());
        }

        PdfPCell cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell);
        table3.addCell(invoice.getTotal().toString());

        document.add(table3);
    }
}
