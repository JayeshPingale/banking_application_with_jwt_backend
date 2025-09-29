package com.testlab.services.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.testlab.entities.Transaction;
import com.testlab.services.PdfGenerationService;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService {

    @Override
    public byte[] generatePassbookPdf(String accountNumber, Double currentBalance, List<Transaction> transactions) {
        
        // ByteArrayOutputStream ek in-memory output stream hai.
        // PDF file disk pe save karne ke bajaye memory mein banegi.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // 1. Title Add Karo
            document.add(new Paragraph("📑 Bank Passbook Statement")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER));

            // 2. Account Details Add Karo
            document.add(new Paragraph("Account Number: " + accountNumber).setMarginTop(20));
            document.add(new Paragraph("Current Balance: Rs." + String.format("%.2f", currentBalance)).setBold());

            // 3. Transactions ki Table Banao
            document.add(new Paragraph("Recent Transactions").setBold().setMarginTop(20));

            // Table ke columns
            float[] columnWidths = {1, 2, 2, 3};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Table Headers
            table.addHeaderCell(new Cell().add(new Paragraph("Txn ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Type").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount (Rs.)").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));

            // Table mein data daalo
            for (Transaction txn : transactions) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(txn.getTransId()))));
                table.addCell(new Cell().add(new Paragraph(txn.getTransType())));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", txn.getAmount()))));
                table.addCell(new Cell().add(new Paragraph(txn.getDate().toString())));
            }

            document.add(table);
            document.close(); // Document ko close karna bahut zaroori hai!

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray(); // PDF data ko byte array mein return karo
    }
}