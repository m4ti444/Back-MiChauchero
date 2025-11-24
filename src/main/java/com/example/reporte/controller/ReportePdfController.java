package com.example.reporte.controller;

import com.example.reporte.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/reporte")
@CrossOrigin(origins = "*")
public class ReportePdfController {
    private final PdfService pdfService;

    public ReportePdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generar(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam double income,
            @RequestParam double expenses,
            @RequestParam(required = false) Double budgetAmount
    ) throws Exception {
        byte[] pdf = pdfService.buildMonthlyReport(year, month, income, expenses, budgetAmount);
        YearMonth ym = YearMonth.of(year, month);
        String filename = "reporte-" + ym + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
