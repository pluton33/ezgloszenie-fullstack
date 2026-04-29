package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;

public interface ReportService {
    ReportsResponse getReports();
    Report getReportById(long id);
    Report addReport(Report report, String email);
    Report editReport(long id, Report report, String email);
    void deleteReport(long id, String email);
}
