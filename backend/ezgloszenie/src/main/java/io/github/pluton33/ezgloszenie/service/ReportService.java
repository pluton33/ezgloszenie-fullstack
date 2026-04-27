package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;

public interface ReportService {
    ReportsResponse getReports();
    Report getReportById(int id);
    Report addReport(Report report);
    Report editReport(int id, Report report);
    void deleteReport(int id);
}
