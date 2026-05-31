package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.*;
import io.github.pluton33.ezgloszenie.repository.Report_status_historyRepository;
import io.github.pluton33.ezgloszenie.repository.ReportsRepository;
import io.github.pluton33.ezgloszenie.repository.StatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {
    private final Report_status_historyRepository reportStatusHistoryRepository;
    private final StatusRepository statusRepository;
    private final ReportsRepository reportsRepository;

    public StatusServiceImpl(Report_status_historyRepository reportStatusHistoryRepository,
                             StatusRepository statusRepository, ReportsRepository reportsRepository) {
        this.reportStatusHistoryRepository = reportStatusHistoryRepository;
        this.statusRepository = statusRepository;
        this.reportsRepository = reportsRepository;
    }

    @Override
    public StatusResponse getStatusReport(Long id) {
        List<Report_status_historyEntity> reportStatusHistoryEntityList = reportStatusHistoryRepository.
                findByReport_Id(id);
        List<Status> statuses = new ArrayList<>();
        for(Report_status_historyEntity reportStatusHistory: reportStatusHistoryEntityList) {
            StatusEntity statusEntity = statusRepository.findById(reportStatusHistory.getStatus().getId()).
                    orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
            Status status = new Status(statusEntity.getId(), statusEntity.getName(),reportStatusHistory.getValid_from(),
                    reportStatusHistory.getValid_to());
            statuses.add(status);
        }
        return new StatusResponse(statuses);
    }

    @Override
    public void updateStatus(Long report_id, String name) {
        ReportEntity reportEntity = reportsRepository.findById(report_id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
        Report_status_historyEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                findByReportId(report_id).getFirst();
        reportStatusHistoryEntity.setValid_to(LocalDateTime.now());
        StatusEntity statusEntity = new StatusEntity(name);
        StatusEntity newStatus = statusRepository.save(statusEntity);
        reportStatusHistoryRepository.save(new Report_status_historyEntity(reportEntity, newStatus));
    }
}
