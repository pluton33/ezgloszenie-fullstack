package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.*;
import io.github.pluton33.ezgloszenie.repository.ReportsRepository;
import io.github.pluton33.ezgloszenie.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportsRepository reportsRepository;
    private final UsersRepository usersRepository;
    private final ReportMapper reportMapper;

    public ReportServiceImpl(ReportsRepository reportsRepository, UsersRepository usersRepository, ReportMapper reportMapper) {
        this.reportsRepository = reportsRepository;
        this.usersRepository = usersRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    public ReportsResponse getReports() {
        List<ReportEntity> reportEntities = reportsRepository.findAll();
        List<Report> reports = reportEntities.stream()
                .map(entity -> reportMapper.toDto(entity))
                .toList();
        return new ReportsResponse(reports);
    }

    @Override
    public Report getReportById(long id) {
        return reportsRepository
                .findById(id)
                .map(entity -> reportMapper.toDto(entity))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak zgłoszenia"
                ));
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

        return reportMapper.toDto(editedEntity);
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
    }
}
