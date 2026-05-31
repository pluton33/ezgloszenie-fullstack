package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.*;
import io.github.pluton33.ezgloszenie.repository.Report_status_historyRepository;
import io.github.pluton33.ezgloszenie.repository.ReportsRepository;
import io.github.pluton33.ezgloszenie.repository.StatusRepository;
import io.github.pluton33.ezgloszenie.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportsRepository reportsRepository;
    private final UsersRepository usersRepository;
    private final ReportMapper reportMapper;
    private final StatusRepository statusRepository;
    private final Report_status_historyRepository reportStatusHistoryRepository;

    public ReportServiceImpl(ReportsRepository reportsRepository, UsersRepository usersRepository, ReportMapper reportMapper,
                             StatusRepository statusRepository, Report_status_historyRepository reportStatusHistoryRepository) {
        this.reportsRepository = reportsRepository;
        this.usersRepository = usersRepository;
        this.reportMapper = reportMapper;
        this.statusRepository = statusRepository;
        this.reportStatusHistoryRepository = reportStatusHistoryRepository;
    }

    @Override
    public ReportsResponse getReports() {
        int count=0;
        List<ReportEntity> reportEntities = reportsRepository.findAll();
        List<Report> reports = reportEntities.stream()
                .map(entity -> reportMapper.toDto(entity))
                .toList();
        List<Report> reportStatus = new ArrayList<>();
        for(Report report : reports) {
            Report_status_historyEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                    findByReportId(report.id()).getFirst();
            StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
                    orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
            reportStatus.add(new Report(report.id(), report.title(), statusEntity.getName(), report.status(), report.user()));
        }
        return new ReportsResponse(reportStatus);
    }

    @Override
    public Report getReportById(long id) {
         Report report = reportsRepository
                .findById(id)
                .map(entity -> reportMapper.toDto(entity))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak zgłoszenia"
                ));
        Report_status_historyEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                findByReportId(report.id()).getFirst();
        StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
        return  new Report(report.id(), report.title(), statusEntity.getName(), report.status(), report.user());
    }

    @Override
    public Report addReport(Report report, String email) {
        UserEntity loggedUser = usersRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        ReportEntity reportEntity = reportMapper.toEntity(report);
        reportEntity.setId(null);
        reportEntity.setUser(loggedUser);
        reportEntity.setCreated_date(LocalDateTime.now());
        ReportEntity savedEntity = reportsRepository.save(reportEntity);
        StatusEntity statusEntity = new StatusEntity(report.status());
        StatusEntity savedStatus = statusRepository.save(statusEntity);
        Report_status_historyEntity reportStatusHistoryEntity = new Report_status_historyEntity(savedEntity, statusEntity);
        reportStatusHistoryRepository.save(reportStatusHistoryEntity);

        return reportMapper.toDto(savedEntity);
    }

    @Override
    public Report editReport(long id, Report report, String email) {
        if (report.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required");
        }

        if (id != report.id()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID mismatch");
        }
        //Fetchuje po id użytkownika i sprawdza czy podany email zgadza się z tym w reporcie sprzed edycji
        ReportEntity oldReportEntity = reportsRepository.findById(id).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        if (!oldReportEntity.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No editing privileges for this report");
        }
        ReportEntity reportEntity = reportMapper.toEntity(report); //do korekty żeby nie tworzyć nowego obiektu reportEntity
        reportEntity.setId(id);
        reportEntity.setUser(oldReportEntity.getUser());
        ReportEntity editedEntity = reportsRepository.save(reportEntity);
        Report_status_historyEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                findByReportId(editedEntity.getId()).getFirst();
        StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
            orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
        statusEntity.setName(report.status());
        statusRepository.save(statusEntity);
        Report report1 = reportMapper.toDto(editedEntity);
        return new Report(report1.id(), report1.title(), report1.status(), report.description(), report1.user());
    }

    @Override
    public void deleteReport(long id, String email) {
        if (!reportsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found");
        }

        ReportEntity oldReportEntity = reportsRepository.findById(id).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        if (!oldReportEntity.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No editing privileges for this report");
        }
        reportsRepository.deleteById(id);
        List<Report_status_historyEntity> reportStatusHistoryEntityList = reportStatusHistoryRepository.
                findByReport_Id(id);
        for(Report_status_historyEntity reportStatusHistoryEntity : reportStatusHistoryEntityList) {
            StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
                    orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
            statusRepository.delete(statusEntity);
        }
        for(Report_status_historyEntity reportStatusHistoryEntity : reportStatusHistoryEntityList) {
            reportStatusHistoryRepository.delete(reportStatusHistoryEntity);
        }
    }
}
