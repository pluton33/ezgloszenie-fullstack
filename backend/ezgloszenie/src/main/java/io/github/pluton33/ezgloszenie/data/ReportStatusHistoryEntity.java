package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "ReportsStatusHistory")
public class ReportStatusHistoryEntity {
    public ReportStatusHistoryEntity() {}
    public ReportStatusHistoryEntity(ReportEntity report, StatusEntity status) {
        this.report = report;
        this.status = status;
        validFrom = LocalDateTime.now();
        validTo = null;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Nullable Long id;
    @ManyToOne
    @JoinColumn(name = "reportId", nullable = false)
    private ReportEntity report;
    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private StatusEntity status;
    private LocalDateTime validFrom;
    private @Nullable LocalDateTime validTo;

    @Nullable
    public long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public ReportEntity getReport() {
        return report;
    }

    public void setReport(ReportEntity report) {
        this.report = report;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public LocalDateTime getValid_from() {
        return validFrom;
    }

    public void setValid_from(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    @Nullable
    public LocalDateTime getValid_to() {
        return validTo;
    }

    public void setValid_to(@Nullable LocalDateTime validTo) {
        this.validTo = validTo;
    }


}
