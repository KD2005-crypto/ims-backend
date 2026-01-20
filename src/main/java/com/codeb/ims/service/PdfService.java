package com.codeb.ims.service;

import com.codeb.ims.entity.Invoice;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

@Service
public class PdfService {

    public ByteArrayInputStream createInvoicePdf(Invoice invoice) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // --- 1. HEADER (Logo & Title) ---
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // --- 2. COMPANY & CLIENT INFO ---
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
            document.add(new Paragraph("Invoice No: " + invoice.getInvoiceNo(), headerFont));
            document.add(new Paragraph("Date: " + invoice.getDateOfService(), headerFont));
            document.add(new Paragraph("Status: " + invoice.getStatus(),
                    invoice.getStatus().equals("PAID") ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.GREEN)
                            : FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.RED)));

            document.add(Chunk.NEWLINE);

            // --- 3. INVOICE TABLE ---
            PdfPTable table = new PdfPTable(4); // 4 Columns
            table.setWidthPercentage(100);

            // Table Header
            addTableHeader(table);

            // Table Data
            addRows(table, invoice);

            document.add(table);
            document.add(Chunk.NEWLINE);

            // --- 4. TOTALS & PAYMENT INFO ---
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Paragraph totalPara = new Paragraph("Total Amount: Rs. " + invoice.getAmountPayable(), totalFont);
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPara);

            Paragraph paidPara = new Paragraph("Amount Paid: Rs. " + invoice.getAmountPaid(), headerFont);
            paidPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(paidPara);

            Paragraph balancePara = new Paragraph("Balance Due: Rs. " + invoice.getBalance(), headerFont);
            balancePara.setAlignment(Element.ALIGN_RIGHT);
            document.add(balancePara);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("------------------------------------------------"));

            // --- 5. PAYMENT AUDIT TRAIL (New!) ---
            if (invoice.getTransactionId() != null) {
                document.add(new Paragraph("Payment Details:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                document.add(new Paragraph("Transaction ID: " + invoice.getTransactionId()));
                document.add(new Paragraph("Payment Mode: " + invoice.getPaymentMode()));
                document.add(new Paragraph("Payment Date: " + invoice.getDateOfPayment()));
            }

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Service Details", "Quantity", "Cost/Unit", "Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setPadding(5);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, Invoice invoice) {
        table.addCell(invoice.getServiceDetails());
        table.addCell(String.valueOf(invoice.getQuantity()));
        table.addCell(String.valueOf(invoice.getCostPerQty()));
        table.addCell(String.valueOf(invoice.getAmountPayable()));
    }
}