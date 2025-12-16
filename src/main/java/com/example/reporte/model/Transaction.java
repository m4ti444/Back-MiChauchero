package com.example.reporte.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private double amount;
    private boolean isIncome;
    private String category;
    private Long dateEpoch;
    
    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_report_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MonthlyReport monthlyReport;
}
