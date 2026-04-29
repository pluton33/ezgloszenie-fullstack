package io.github.pluton33.ezgloszenie;

import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportEntity;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;
import io.github.pluton33.ezgloszenie.repository.ReportsRepository;
import io.github.pluton33.ezgloszenie.service.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {
    @Mock
    private ReportsRepository repository;

    @InjectMocks
    private ReportServiceImpl service;

    @Test
    public void shouldReturnReportList() {
        ReportEntity entity1 = new ReportEntity();
        entity1.setId(1);
        entity1.setTitle("title1");

        ReportEntity entity2 = new ReportEntity();
        entity2.setId(2);
        entity2.setTitle("title2");
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        ReportsResponse response = service.getReports();
        assertNotNull(response);
        assertEquals(2, response.reports().size());
        assertEquals("title1", response.reports().get(0).title());
        assertEquals("title2", response.reports().get(1).title());

        verify(repository, times(1)).findAll();

    }

    @Test
    public void shouldReturnReportWhenExists() {
        ReportEntity entity1 = new ReportEntity();
        entity1.setId(1);
        entity1.setTitle("title1");

        when(repository.findById(1)).thenReturn(Optional.of(entity1));

        Report response = service.getReportById(1);
        assertNotNull(response);
        assertEquals("title1", response.title());

        verify(repository, times(1)).findById(1);

    }

    @Test
    public void shouldAddReportWithCorrectData() {
        Report report1 = new Report(null, "title1", "content1");

        when(repository.save(any(ReportEntity.class))).thenAnswer(invocation -> {
            ReportEntity entity = invocation.getArgument(0);
            entity.setId(123); // symulacja, że baza nadała ID 123
            return entity;
        });

        Report savedReport = service.addReport(report1);

        ArgumentCaptor<ReportEntity> captor = ArgumentCaptor.forClass(ReportEntity.class);
        verify(repository).save(captor.capture());

        ReportEntity capturedInDatabase = captor.getValue();

        assertEquals("title1", capturedInDatabase.getTitle());
        assertEquals("content1", capturedInDatabase.getDescription());

        assertNotNull(savedReport);
        assertEquals("title1", savedReport.title());
        assertEquals("content1", savedReport.description());

        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldEditReportWithCorrectData() {
        long idFromUrl = 1;
        Report dto = new Report(1, "Updated Title", "Updated Content");

        when(repository.existsById(idFromUrl)).thenReturn(true);
        when(repository.save(any(ReportEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        Report result = service.editReport(idFromUrl, dto);

        ArgumentCaptor<ReportEntity> captor = ArgumentCaptor.forClass(ReportEntity.class);
        verify(repository).save(captor.capture());

        ReportEntity captured = captor.getValue();

        assertEquals(idFromUrl, captured.getId());
        assertEquals("Updated Title", captured.getTitle());
        assertEquals("Updated Content", captured.getDescription());

        assertNotNull(result);
        assertEquals("Updated Title", result.title());
    }

    @Test
    void shouldThrowExceptionWhenIdInBodyIsNull() {

        long idFromUrl = 1;
        Report reportWithNullId = new Report(null, "title", "description");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.editReport(idFromUrl, reportWithNullId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowExceptionWhenIdsDoNotMatch() {

        long idFromUrl = 1;
        Report reportWithId99 = new Report(99, "title", "description");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.editReport(idFromUrl, reportWithId99);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verifyNoInteractions(repository);
    }

    @Test
    void shouldDeleteReportWhenExists() {
        long idToDelete = 10;

        when(repository.existsById(idToDelete)).thenReturn(true);
        service.deleteReport(idToDelete);

        verify(repository, times(1)).deleteById(idToDelete);
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistentReport() {

        long idToDelete = 10;
        when(repository.existsById(idToDelete)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            service.deleteReport(idToDelete);
        });

        verify(repository, never()).deleteById(anyInt());
    }
}
