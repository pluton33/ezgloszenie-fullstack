package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportEntity;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;
import io.github.pluton33.ezgloszenie.repository.ReportsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportsRepository reportsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ReportsResponse getReports() {
        List<ReportEntity> reportEntities = reportsRepository.findAll();
        List<Report> reports = reportEntities.stream()
                .map(entity -> Report.fromEntity(entity))
                .toList();
        return new ReportsResponse(reports);
    }

    @Override
    public Report getReportById(int id) {
        return reportsRepository
                .findById(id)
                .map(entity -> Report.fromEntity(entity))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak zgłoszenia"
                ));
    }

    @Override
    public Report addReport(Report report) {
        ReportEntity reportEntity = report.toEntity();
        reportEntity.setId(null);
        ReportEntity savedEntity = reportsRepository.save(reportEntity);

        return new Report(
                savedEntity.getId(),
                savedEntity.getTitle(),
                savedEntity.getContent()
        );
    }

    @Override
    public Report editReport(int id, Report report) {
        if(report.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required");
        }

        if(id != report.id()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID mismatch");
        }

        if(!reportsRepository.existsById(report.id())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found");
        }
        ReportEntity reportEntity = report.toEntity();
        reportEntity.setId(id);
        ReportEntity editedEntity = reportsRepository.save(reportEntity);

        return new Report(
                editedEntity.getId(),
                editedEntity.getTitle(),
                editedEntity.getContent()
        );
    }

    @Override
    public void deleteReport(int id) {
        if(!reportsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found");
        }
        reportsRepository.deleteById(id);
    }
}
