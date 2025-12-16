package com.example.reporte.service;

import com.example.reporte.model.MonthlyReport;
import com.example.reporte.model.Transaction;
import com.example.reporte.repository.MonthlyReportRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final MonthlyReportRepository monthlyReportRepository;

    @Transactional
    public byte[] generateAndSaveReport(String userId, int year, int month, double incomeVal, double expensesVal, Double budgetVal, List<Transaction> transactions) throws Exception {
        MonthlyReport report = monthlyReportRepository.findByUserIdAndYearAndMonth(userId, year, month)
                .orElse(MonthlyReport.builder()
                        .userId(userId)
                        .year(year)
                        .month(month)
                        .build());

        report.setTotalIncome(incomeVal);
        report.setTotalExpenses(expensesVal);
        report.setBudgetAmount(budgetVal);
        
        if (transactions != null && !transactions.isEmpty()) {
            report.getTransactions().clear();
            for (Transaction tx : transactions) {
                report.addTransaction(tx);
            }
        }

        monthlyReportRepository.save(report);

        return buildPdf(report);
    }

    @Transactional(readOnly = true)
    public byte[] getReportPdf(String userId, int year, int month) throws Exception {
        MonthlyReport report = monthlyReportRepository.findByUserIdAndYearAndMonth(userId, year, month)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado para " + month + "/" + year));
        return buildPdf(report);
    }

    private byte[] buildPdf(MonthlyReport report) throws Exception {
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
            cs.newLine();

            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.showText("Mes: " + String.format("%02d", report.getMonth()) + "/" + report.getYear());
            cs.newLine();
            cs.newLine();

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));
            String income = nf.format(report.getTotalIncome());
            String expenses = nf.format(report.getTotalExpenses());
            double balanceVal = report.getTotalIncome() - report.getTotalExpenses();
            String balance = nf.format(balanceVal);
            String budget = report.getBudgetAmount() != null ? nf.format(report.getBudgetAmount()) : "No establecido";

            cs.showText("Ingresos Totales: " + income);
            cs.newLine();
            cs.showText("Gastos Totales: " + expenses);
            cs.newLine();
            cs.showText("Balance: " + balance);
            cs.newLine();
            cs.showText("Presupuesto: " + budget);
            cs.newLine();
            cs.newLine();
            
            cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
            cs.showText("Detalle de Transacciones");
            cs.newLine();
            cs.setFont(PDType1Font.HELVETICA, 10);
            
            if (report.getTransactions() != null) {
                for (Transaction tx : report.getTransactions()) {
                    String type = tx.isIncome() ? "(+)" : "(-)";
                    String line = String.format("%s %s: %s - %s", type, tx.getTitle(), nf.format(tx.getAmount()), tx.getCategory());
                    cs.showText(line);
                    cs.newLine();
                }
            }

            cs.endText();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out);
        doc.close();
        return out.toByteArray();
    }
    
    public byte[] buildMonthlyReport(String userId, int year, int month, double incomeVal, double expensesVal, Double budgetVal) throws Exception {
        return generateAndSaveReport(userId, year, month, incomeVal, expensesVal, budgetVal, null);
    }
}
