package com.codeb.ims.service;

import com.codeb.ims.entity.Invoice;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    // Define Professional Colors
    private static final Color BRAND_COLOR = new Color(63, 81, 181); // Material Blue
    private static final Color HEADER_BG = new Color(240, 242, 245); // Light Gray
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BRAND_COLOR);
    private static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.GRAY);
    private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
    private static final Font TOTAL_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);

    public ByteArrayInputStream generateInvoicePdf(Invoice invoice) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // --- 1. HEADER SECTION (Split Left & Right) ---
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 1}); // Equal width

            // Left: Company Name
            PdfPCell leftHeader = new PdfPCell();
            leftHeader.addElement(new Paragraph("CODE-B", TITLE_FONT));
            leftHeader.addElement(new Paragraph("Management System", SUBTITLE_FONT));
            leftHeader.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(leftHeader);

            // Right: Invoice Meta Data
            PdfPCell rightHeader = new PdfPCell();
            Paragraph invoiceInfo = new Paragraph();
            invoiceInfo.add(new Chunk("INVOICE\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.GRAY)));
            invoiceInfo.add(new Chunk("#" + invoice.getInvoiceNumber() + "\n", TEXT_FONT));
            invoiceInfo.add(new Chunk("Date: " + invoice.getCreatedAt().toLocalDate(), TEXT_FONT));
            invoiceInfo.setAlignment(Element.ALIGN_RIGHT);

            rightHeader.addElement(invoiceInfo);
            rightHeader.setBorder(Rectangle.NO_BORDER);
            rightHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(rightHeader);

            document.add(headerTable);
            document.add(new Paragraph("\n")); // Spacer

            // Separator Line
            LineSeparator ls = new LineSeparator();
            ls.setLineColor(Color.LIGHT_GRAY);
            document.add(ls);
            document.add(new Paragraph("\n"));

            // --- 2. ADDRESS SECTION ---
            PdfPTable addressTable = new PdfPTable(2);
            addressTable.setWidthPercentage(100);

            // From (Store)
            PdfPCell fromCell = new PdfPCell();
            fromCell.addElement(new Paragraph("FROM:", SUBTITLE_FONT));
            fromCell.addElement(new Paragraph(invoice.getLocation().getBrand().getChain().getChainName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            fromCell.addElement(new Paragraph(invoice.getLocation().getLocationName(), TEXT_FONT));
            fromCell.addElement(new Paragraph(invoice.getLocation().getAddress(), TEXT_FONT));
            fromCell.addElement(new Paragraph("GST: " + invoice.getLocation().getBrand().getChain().getGstNumber(), TEXT_FONT));
            fromCell.setBorder(Rectangle.NO_BORDER);
            addressTable.addCell(fromCell);

            // To (Customer)
            PdfPCell toCell = new PdfPCell();
            toCell.addElement(new Paragraph("BILL TO:", SUBTITLE_FONT));
            toCell.addElement(new Paragraph(invoice.getCustomerName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            toCell.setBorder(Rectangle.NO_BORDER);
            addressTable.addCell(toCell);

            document.add(addressTable);
            document.add(new Paragraph("\n\n"));

            // --- 3. ITEMS TABLE ---
            PdfPTable table = new PdfPTable(2); // 2 Columns
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1}); // Description is wider than Amount
            table.setHeaderRows(1);

            // Table Headers
            PdfPCell h1 = new PdfPCell(new Phrase("DESCRIPTION", HEADER_FONT));
            h1.setBackgroundColor(BRAND_COLOR);
            h1.setPadding(8);
            h1.setBorderColor(BRAND_COLOR);
            table.addCell(h1);

            PdfPCell h2 = new PdfPCell(new Phrase("AMOUNT (INR)", HEADER_FONT));
            h2.setBackgroundColor(BRAND_COLOR);
            h2.setPadding(8);
            h2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            h2.setBorderColor(BRAND_COLOR);
            table.addCell(h2);

            // Row 1: Base Amount
            addStyledCell(table, "Base Amount", false);
            addStyledCell(table, String.format("%.2f", invoice.getAmount()), true);

            // Row 2: Tax
            addStyledCell(table, "Tax (18% GST)", false);
            addStyledCell(table, String.format("%.2f", invoice.getTaxAmount()), true);

            document.add(table);

            // --- 4. TOTAL SECTION ---
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{3, 1});

            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(emptyCell);

            PdfPCell totalCell = new PdfPCell();
            Paragraph totalText = new Paragraph("TOTAL: ₹ " + String.format("%.2f", invoice.getTotalAmount()), TOTAL_FONT);
            totalText.setAlignment(Element.ALIGN_RIGHT);
            totalCell.addElement(totalText);
            totalCell.setBorder(Rectangle.TOP);
            totalCell.setPaddingTop(10);
            totalTable.addCell(totalCell);

            document.add(totalTable);

            // --- 5. FOOTER ---
            document.add(new Paragraph("\n\n\n"));
            Paragraph footer = new Paragraph("Thank you for your business!", SUBTITLE_FONT);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Helper method to style table cells cleanly
    private void addStyledCell(PdfPTable table, String text, boolean alignRight) {
        PdfPCell cell = new PdfPCell(new Phrase(text, TEXT_FONT));
        cell.setPadding(8);
        cell.setBorderColor(Color.LIGHT_GRAY);
        if (alignRight) {
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }
        table.addCell(cell);
    }
}