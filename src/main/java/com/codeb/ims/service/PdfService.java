package com.codeb.ims.service;

import com.codeb.ims.entity.Invoice;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

@Service
public class PdfService {

    // --- COLORS ---
    private static final BaseColor THEME_COLOR = new BaseColor(26, 35, 126); // Navy Blue
    private static final BaseColor ACCENT_COLOR = new BaseColor(240, 240, 240); // Light Gray

    public ByteArrayInputStream createInvoicePdf(Invoice invoice) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // --- FONTS (Created dynamically to avoid errors) ---
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.WHITE);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

            // 1. HEADER SECTION (Blue Banner)
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 1});

            // Left Side: Company Info
            PdfPCell companyCell = new PdfPCell();
            companyCell.setBackgroundColor(THEME_COLOR);
            companyCell.setBorder(Rectangle.NO_BORDER);
            companyCell.setPadding(20);
            companyCell.addElement(new Paragraph("CODE-B ENTERPRISES", titleFont));
            companyCell.addElement(new Paragraph("123, Tech Park, Innovation City", subtitleFont));
            companyCell.addElement(new Paragraph("support@codeb.com | +91-9876543210", subtitleFont));
            headerTable.addCell(companyCell);

            // Right Side: Invoice Meta Data
            PdfPCell metaCell = new PdfPCell();
            metaCell.setBackgroundColor(THEME_COLOR);
            metaCell.setBorder(Rectangle.NO_BORDER);
            metaCell.setPadding(20);
            metaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            Paragraph invTitle = new Paragraph("INVOICE", titleFont);
            invTitle.setAlignment(Element.ALIGN_RIGHT);
            metaCell.addElement(invTitle);

            Paragraph invNo = new Paragraph("No: #" + invoice.getInvoiceNo(), subtitleFont);
            invNo.setAlignment(Element.ALIGN_RIGHT);
            metaCell.addElement(invNo);

            Paragraph date = new Paragraph("Date: " + invoice.getDateOfService(), subtitleFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            metaCell.addElement(date);

            headerTable.addCell(metaCell);
            document.add(headerTable);

            document.add(Chunk.NEWLINE);

            // 2. BILL TO & STATUS SECTION
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{2, 1}); // Wide Left, Narrow Right

            // Bill To
            PdfPCell billToCell = new PdfPCell();
            billToCell.setBorder(Rectangle.NO_BORDER);

            // FIX: Use FontFactory.getFont here
            Font billTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, THEME_COLOR);
            billToCell.addElement(new Paragraph("BILL TO:", billTitleFont));

            billToCell.addElement(new Paragraph("Client ID: " + (invoice.getEmailId() != null ? invoice.getEmailId() : "N/A"), dataFont));
            infoTable.addCell(billToCell);

            // Status Stamp (Visual Flair)
            PdfPCell statusCell = new PdfPCell();
            statusCell.setBorder(Rectangle.NO_BORDER);
            statusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            String statusText = invoice.getStatus();

            // FIX: Create Status Font dynamically based on color
            BaseColor statusColor = statusText.equals("PAID") ? BaseColor.GREEN : BaseColor.RED;
            Font statusFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, statusColor);

            Paragraph statusPara = new Paragraph(statusText, statusFont);
            statusPara.setAlignment(Element.ALIGN_RIGHT);

            // Draw a box around status
            PdfPTable statusBox = new PdfPTable(1);
            PdfPCell boxCell = new PdfPCell(statusPara);
            boxCell.setBorderColor(statusColor);
            boxCell.setBorderWidth(2f);
            boxCell.setPadding(5);
            statusBox.addCell(boxCell);

            statusCell.addElement(statusBox);
            infoTable.addCell(statusCell);

            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // 3. THE TABLE (Professional Grid)
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 1, 1}); // Description is wider
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Headers
            addHeaderCell(table, "Service Description", headerFont);
            addHeaderCell(table, "Qty", headerFont);
            addHeaderCell(table, "Price", headerFont);
            addHeaderCell(table, "Total", headerFont);

            // Data Rows
            addCell(table, invoice.getServiceDetails(), dataFont);
            addCell(table, String.valueOf(invoice.getQuantity()), dataFont);
            addCell(table, formatCurrency(invoice.getCostPerQty()), dataFont);
            addCell(table, formatCurrency(invoice.getAmountPayable()), dataFont);

            document.add(table);

            // 4. TOTALS SECTION
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addTotalRow(totalTable, "Subtotal:", invoice.getAmountPayable(), dataFont);
            addTotalRow(totalTable, "Tax (0%):", 0.0f, dataFont);

            // Grand Total (Bold)
            PdfPCell labelCell = new PdfPCell(new Paragraph("Grand Total:", boldFont));
            labelCell.setBorder(Rectangle.TOP);
            labelCell.setPadding(5);
            totalTable.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Paragraph(formatCurrency(invoice.getAmountPayable()), boldFont));
            valueCell.setBorder(Rectangle.TOP);
            valueCell.setPadding(5);
            valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(valueCell);

            document.add(totalTable);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

            // 5. PAYMENT INFO & FOOTER
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidthPercentage(100);

            // Left: Payment Details
            PdfPCell bankCell = new PdfPCell();
            bankCell.setBorder(Rectangle.NO_BORDER);
            bankCell.addElement(new Paragraph("Payment Info:", boldFont));

            if (invoice.getStatus().equals("PAID")) {
                bankCell.addElement(new Paragraph("Payment Received via: " + invoice.getPaymentMode(), dataFont));
                bankCell.addElement(new Paragraph("Transaction ID: " + invoice.getTransactionId(), dataFont));
                bankCell.addElement(new Paragraph("Date: " + invoice.getDateOfPayment(), dataFont));
            } else {
                bankCell.addElement(new Paragraph("Bank: HDFC Bank", dataFont));
                bankCell.addElement(new Paragraph("Account: 1234567890", dataFont));
                bankCell.addElement(new Paragraph("IFSC: HDFC0001234", dataFont));
            }
            footerTable.addCell(bankCell);

            // Right: Signature
            PdfPCell signCell = new PdfPCell();
            signCell.setBorder(Rectangle.NO_BORDER);
            signCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            signCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            signCell.setPaddingTop(30);
            signCell.addElement(new Paragraph("For Code-B Enterprises", boldFont));
            signCell.addElement(new Paragraph("(Authorized Signatory)", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8)));
            footerTable.addCell(signCell);

            document.add(footerTable);

            // Bottom Note
            Paragraph thankYou = new Paragraph("Thank you for your business!", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
            thankYou.setAlignment(Element.ALIGN_CENTER);
            thankYou.setSpacingBefore(30);
            document.add(thankYou);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // --- HELPER METHODS FOR CLEAN CODE ---

    private void addHeaderCell(PdfPTable table, String title, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(title, font));
        cell.setBackgroundColor(THEME_COLOR);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private void addTotalRow(PdfPTable table, String label, float amount, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(3);
        table.addCell(labelCell);

        PdfPCell valCell = new PdfPCell(new Phrase(formatCurrency(amount), font));
        valCell.setBorder(Rectangle.NO_BORDER);
        valCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valCell.setPadding(3);
        table.addCell(valCell);
    }

    private String formatCurrency(float amount) {
        DecimalFormat df = new DecimalFormat("Rs. #,##0.00");
        return df.format(amount);
    }
}