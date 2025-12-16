package com.example.reporte.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "monthly_reports", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "report_year", "report_month"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "report_year", nullable = false)
    private int year;

    @Column(name = "report_month", nullable = false)
    private int month;

    private double totalIncome;
    private double totalExpenses;
    private Double budgetAmount;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "monthlyReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setMonthlyReport(this);
    }
}
