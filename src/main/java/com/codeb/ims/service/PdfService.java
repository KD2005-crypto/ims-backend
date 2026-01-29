package com.codeb.ims.service;

import com.codeb.ims.entity.Invoice;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

@Service
public class PdfService {

    // Renamed to createInvoicePdf to match your Controller's expectation
    public ByteArrayInputStream createInvoicePdf(Invoice invoice) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Header Styling
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_RIGHT);
            document.add(title);

            LineSeparator ls = new LineSeparator();
            ls.setLineColor(new BaseColor(200, 200, 200));
            document.add(new Chunk(ls));
            document.add(Chunk.NEWLINE);

            // 2. Info Section Table
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);

            // Left Side: Company
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.addElement(new Paragraph("Codeb Enterprises LTD", boldFont));
            leftCell.addElement(new Paragraph("Codeb Office,Pune", normalFont));
            leftCell.addElement(new Paragraph("GSTIN: 27AAAAA0000A1Z5", normalFont));
            infoTable.addCell(leftCell);

            // Right Side: Invoice Info
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            Paragraph pNo = new Paragraph("Invoice No: " + invoice.getInvoiceNo(), normalFont);
            pNo.setAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(pNo);

            Paragraph pDate = new Paragraph("Date: " + invoice.getDateOfService(), normalFont);
            pDate.setAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(pDate);

            Paragraph pGroup = new Paragraph("Group: " + (invoice.getGroupName() != null ? invoice.getGroupName() : "N/A"), boldFont);
            pGroup.setAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(pGroup);
            infoTable.addCell(rightCell);

            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // 3. Bill To
            document.add(new Paragraph("BILL TO:", boldFont));
            document.add(new Paragraph(invoice.getEmailId() != null ? invoice.getEmailId() : "Customer", normalFont));
            document.add(Chunk.NEWLINE);

            // 4. Main Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 1, 2, 2});

            Stream.of("Description", "Qty", "Rate", "Total").forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(new BaseColor(63, 81, 181));
                header.setPadding(8);
                header.setPhrase(new Phrase(columnTitle, headerFont));
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(header);
            });

            table.addCell(new PdfPCell(new Phrase(invoice.getServiceDetails(), normalFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(invoice.getQuantity()), normalFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(invoice.getCostPerQty()), normalFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(invoice.getAmountPayable()), normalFont)));

            document.add(table);

            // 5. Summary
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(40);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summaryTable.addCell(new Phrase("Total Amount:", normalFont));
            summaryTable.addCell(new Phrase("Rs. " + invoice.getAmountPayable(), normalFont));

            PdfPCell balanceCell = new PdfPCell(new Phrase("BALANCE DUE:", boldFont));
            balanceCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            summaryTable.addCell(balanceCell);

            PdfPCell balanceVal = new PdfPCell(new Phrase("Rs. " + invoice.getBalance(), boldFont));
            balanceVal.setBackgroundColor(BaseColor.LIGHT_GRAY);
            summaryTable.addCell(balanceVal);

            document.add(summaryTable);
            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}