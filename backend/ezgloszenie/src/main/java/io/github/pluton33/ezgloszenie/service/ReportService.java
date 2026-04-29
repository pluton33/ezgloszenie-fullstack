package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;

public interface ReportService {
    ReportsResponse getReports();
    Report getReportById(int id);
    Report addReport(Report report, String email);
    Report editReport(int id, Report report, String email);
    void deleteReport(int id, String email);
}
