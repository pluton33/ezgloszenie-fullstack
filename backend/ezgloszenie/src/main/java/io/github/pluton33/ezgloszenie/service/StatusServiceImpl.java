package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.*;
import io.github.pluton33.ezgloszenie.repository.ReportStatusHistoryRepository;
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
    private final ReportStatusHistoryRepository reportStatusHistoryRepository;
    private final StatusRepository statusRepository;
    private final ReportsRepository reportsRepository;

    public StatusServiceImpl(ReportStatusHistoryRepository reportStatusHistoryRepository,
                             StatusRepository statusRepository, ReportsRepository reportsRepository) {
        this.reportStatusHistoryRepository = reportStatusHistoryRepository;
        this.statusRepository = statusRepository;
        this.reportsRepository = reportsRepository;
    }

    @Override
    public StatusResponse getStatusReport(Long reportId) {
        List<ReportStatusHistoryEntity> reportStatusHistoryEntityList = reportStatusHistoryRepository.
                findByReportId(reportId);
        List<Status> statuses = new ArrayList<>();
        for(ReportStatusHistoryEntity reportStatusHistory: reportStatusHistoryEntityList) {
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
        ReportStatusHistoryEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                findByReportIdAndValidToIsNull(report_id).getFirst();
        reportStatusHistoryEntity.setValid_to(LocalDateTime.now());
        StatusEntity statusEntity = new StatusEntity(name);
        StatusEntity newStatus = statusRepository.save(statusEntity);
        reportStatusHistoryRepository.save(new ReportStatusHistoryEntity(reportEntity, newStatus));
    }
}
