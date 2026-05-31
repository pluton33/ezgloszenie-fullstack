package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "ReportsStatusHistory")
public class Report_status_historyEntity {
    public Report_status_historyEntity() {}
    public Report_status_historyEntity(ReportEntity report, StatusEntity status) {
        this.report = report;
        this.status = status;
        valid_from = LocalDateTime.now();
        valid_to = null;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Nullable Long id;
    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private ReportEntity report;
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;
    private LocalDateTime valid_from;
    private @Nullable LocalDateTime valid_to;

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
        return valid_from;
    }

    public void setValid_from(LocalDateTime valid_from) {
        this.valid_from = valid_from;
    }

    @Nullable
    public LocalDateTime getValid_to() {
        return valid_to;
    }

    public void setValid_to(@Nullable LocalDateTime valid_to) {
        this.valid_to = valid_to;
    }


}
