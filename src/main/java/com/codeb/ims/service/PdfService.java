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

    public ByteArrayInputStream generateInvoicePdf(Invoice invoice) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // --- 1. FONTS & COLORS ---
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

            // --- 2. HEADER SECTION ---
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_RIGHT);
            document.add(title);

            LineSeparator ls = new LineSeparator();
            ls.setLineColor(new BaseColor(200, 200, 200));
            document.add(new Chunk(ls));
            document.add(Chunk.NEWLINE);

            // --- 3. INFO SECTION (Fixing the Error Here) ---
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            // Note: Removed setBorderWidth(0) as it doesn't exist for PdfPTable

            // Left Side: Business Info
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER); // This removes the border for the cell
            leftCell.addElement(new Paragraph("Your Company Name", boldFont));
            leftCell.addElement(new Paragraph("123 Business Street, Pune", normalFont));
            leftCell.addElement(new Paragraph("GSTIN: 27AAAAA0000A1Z5", normalFont));
            infoTable.addCell(leftCell);

            // Right Side: Invoice Details
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER); // This removes the border for the cell
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

            // --- 4. CLIENT SECTION ---
            document.add(new Paragraph("BILL TO:", boldFont));
            document.add(new Paragraph(invoice.getEmailId() != null ? invoice.getEmailId() : "Valued Customer", normalFont));
            document.add(Chunk.NEWLINE);

            // --- 5. MAIN ITEMS TABLE ---
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 1, 2, 2});

            Stream.of("Service Description", "Qty", "Rate", "Total").forEach(columnTitle -> {
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

            // --- 6. SUMMARY SECTION ---
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(40);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summaryTable.addCell(new Phrase("Subtotal:", normalFont));
            summaryTable.addCell(new Phrase(String.valueOf(invoice.getAmountPayable()), normalFont));

            PdfPCell balanceCell = new PdfPCell(new Phrase("BALANCE DUE:", boldFont));
            balanceCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            summaryTable.addCell(balanceCell);

            PdfPCell balanceVal = new PdfPCell(new Phrase("Rs. " + invoice.getBalance(), boldFont));
            balanceVal.setBackgroundColor(BaseColor.LIGHT_GRAY);
            summaryTable.addCell(balanceVal);

            document.add(summaryTable);

            // --- 7. FOOTER ---
            document.add(Chunk.NEWLINE);
            Paragraph footer = new Paragraph("Thank you for your business!", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}