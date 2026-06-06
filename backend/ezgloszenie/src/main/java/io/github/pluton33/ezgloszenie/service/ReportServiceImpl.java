package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.*;
import io.github.pluton33.ezgloszenie.repository.ReportStatusHistoryRepository;
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
    private final ReportStatusHistoryRepository reportStatusHistoryRepository;

    public ReportServiceImpl(ReportsRepository reportsRepository, UsersRepository usersRepository, ReportMapper reportMapper,
                             StatusRepository statusRepository, ReportStatusHistoryRepository reportStatusHistoryRepository) {
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
            ReportStatusHistoryEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                    findByReportIdAndValidToIsNull(report.id()).getFirst();
            StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
                    orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
            reportStatus.add(new Report(report.id(), report.title(), report.description(), statusEntity.getName(),report.category(), report.user(),report.created_date()));
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
        ReportStatusHistoryEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                findByReportIdAndValidToIsNull(report.id()).getFirst();
        StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
        return  new Report(report.id(), report.title(), report.description(), statusEntity.getName(),report.category(), report.user(),report.created_date());
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
        ReportStatusHistoryEntity reportStatusHistoryEntity = new ReportStatusHistoryEntity(savedEntity, savedStatus);
        reportStatusHistoryRepository.save(reportStatusHistoryEntity);

        Report report1 = reportMapper.toDto(savedEntity);
        return new Report(report1.id(), report1.title(), report1.description(), report.status(),report1.category(),report1.user(),report1.created_date());
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
        ReportStatusHistoryEntity reportStatusHistoryEntity = reportStatusHistoryRepository.
                findByReportIdAndValidToIsNull(editedEntity.getId()).getFirst();
        StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
            orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
        statusEntity.setName(report.status());
        statusRepository.save(statusEntity);
        Report report1 = reportMapper.toDto(editedEntity);
        return new Report(report1.id(), report1.title(), report1.description(), report.status(),report1.category(),report1.user(),report1.created_date());
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
        List<ReportStatusHistoryEntity> reportStatusHistoryEntityList = reportStatusHistoryRepository.
                findByReportId(id);
        List<StatusEntity> statusEntities = new ArrayList<>();
        for(ReportStatusHistoryEntity reportStatusHistoryEntity : reportStatusHistoryEntityList) {
            StatusEntity statusEntity = statusRepository.findById(reportStatusHistoryEntity.getStatus().getId()).
                    orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report does not have status"));
            statusEntities.add(statusEntity);
        }
        for(ReportStatusHistoryEntity reportStatusHistoryEntity : reportStatusHistoryEntityList) {
            reportStatusHistoryRepository.delete(reportStatusHistoryEntity);
        }
        for(StatusEntity statusEntity : statusEntities) {
            statusRepository.delete(statusEntity);
        }
        reportsRepository.deleteById(id);
    }
}
