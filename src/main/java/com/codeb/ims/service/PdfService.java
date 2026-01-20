package com.codeb.ims.service;

import com.codeb.ims.entity.Invoice;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public ByteArrayInputStream generateInvoicePdf(Invoice invoice) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Title
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("CODE-B INVOICE", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // 2. Create a Table for Details
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // 3. Add Rows (Using ONLY fields that exist in your new Invoice Entity)
            addTableRow(table, "Invoice No:", String.valueOf(invoice.getInvoiceNo()));
            addTableRow(table, "Estimate ID:", String.valueOf(invoice.getEstimatedId()));
            addTableRow(table, "Chain ID:", String.valueOf(invoice.getChainId()));
            addTableRow(table, "Service Details:", invoice.getServiceDetails());

            addTableRow(table, "Quantity:", String.valueOf(invoice.getQuantity()));
            addTableRow(table, "Cost per Qty:", "Rs. " + invoice.getCostPerQty());
            addTableRow(table, "Total Amount:", "Rs. " + invoice.getAmountPayable());

            addTableRow(table, "Date of Service:", String.valueOf(invoice.getDateOfService()));
            addTableRow(table, "Delivery Details:", invoice.getDeliveryDetails());
            addTableRow(table, "Email:", invoice.getEmailId());

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Helper method to make the table look nice
    private void addTableRow(PdfPTable table, String header, String value) {
        PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        headerCell.setPadding(5);
        table.addCell(headerCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "-"));
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }
}