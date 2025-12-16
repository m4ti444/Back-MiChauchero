package com.example.reporte.controller;

import com.example.reporte.model.Transaction;
import com.example.reporte.service.PdfService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/reporte")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReportePdfController {
    
    private final PdfService pdfService;

    @PostMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generar(@RequestBody ReportRequest request) throws Exception {
        byte[] pdf = pdfService.generateAndSaveReport(
                request.getUserId(),
                request.getYear(), 
                request.getMonth(), 
                request.getIncome(), 
                request.getExpenses(), 
                request.getBudgetAmount(),
                request.getTransactions()
        );
        
        YearMonth ym = YearMonth.of(request.getYear(), request.getMonth());
        String filename = "reporte-" + ym + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    @GetMapping(value = "/{year}/{month}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> obtenerHistorico(@PathVariable int year, @PathVariable int month, @RequestParam String userId) throws Exception {
        byte[] pdf = pdfService.getReportPdf(userId, year, month);
        YearMonth ym = YearMonth.of(year, month);
        String filename = "reporte-" + ym + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @Data
    public static class ReportRequest {
        private String userId;
        private int year;
        private int month;
        private double income;
        private double expenses;
        private Double budgetAmount;
        private List<Transaction> transactions;
    }
}
