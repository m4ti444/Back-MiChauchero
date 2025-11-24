package com.example.reporte.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PdfService {
    public byte[] buildMonthlyReport(int year, int month, double incomeVal, double expensesVal, Double budgetVal) throws Exception {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
            cs.setLeading(16f);
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 20);
            cs.newLineAtOffset(50, page.getMediaBox().getHeight() - 80);
            cs.showText("Reporte Financiero Mensual");
            cs.newLine();

            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLine();
            cs.showText("Mes: " + String.format("%02d", month) + "/" + year);
            cs.newLine();

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));
            String income = nf.format(incomeVal);
            String expenses = nf.format(expensesVal);
            double balanceVal = incomeVal - expensesVal;
            String balance = nf.format(balanceVal);
            String budget = budgetVal != null ? nf.format(budgetVal) : "No establecido";

            cs.showText("Ingresos: " + income);
            cs.newLine();
            cs.showText("Gastos: " + expenses);
            cs.newLine();
            cs.showText("Balance: " + balance);
            cs.newLine();
            cs.showText("Presupuesto: " + budget);
            cs.endText();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out);
        doc.close();
        return out.toByteArray();
    }
}
