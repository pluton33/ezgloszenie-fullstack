package io.github.pluton33.ezgloszenie;

import io.github.pluton33.ezgloszenie.data.Offense;
import io.github.pluton33.ezgloszenie.data.OffenseEntity;
import io.github.pluton33.ezgloszenie.data.OffensesResponse;
import io.github.pluton33.ezgloszenie.repository.OffensesRepository;
import io.github.pluton33.ezgloszenie.service.OffenseServiceImpl;
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
public class OffenseServiceImplTest {
    @Mock
    private OffensesRepository repository;

    @InjectMocks
    private OffenseServiceImpl service;

    @Test
    public void shouldReturnOffenseList() {
        OffenseEntity entity1 = new OffenseEntity();
        entity1.setId(1);
        entity1.setTitle("title1");

        OffenseEntity entity2 = new OffenseEntity();
        entity2.setId(2);
        entity2.setTitle("title2");
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        OffensesResponse response = service.getOffenses();
        assertNotNull(response);
        assertEquals(2, response.offenses().size());
        assertEquals("title1", response.offenses().get(0).title());
        assertEquals("title2", response.offenses().get(1).title());

        verify(repository, times(1)).findAll();

    }

    @Test
    public void shouldReturnOffenseWhenExists() {
        OffenseEntity entity1 = new OffenseEntity();
        entity1.setId(1);
        entity1.setTitle("title1");

        when(repository.findById(1)).thenReturn(Optional.of(entity1));

        Offense response = service.getOffenseById(1);
        assertNotNull(response);
        assertEquals("title1", response.title());

        verify(repository, times(1)).findById(1);

    }

    @Test
    public void shouldAddOffenseWithCorrectData() {
        Offense offense1 = new Offense(null, "title1", "content1");

        when(repository.save(any(OffenseEntity.class))).thenAnswer(invocation -> {
            OffenseEntity entity = invocation.getArgument(0);
            entity.setId(123); // symulacja, że baza nadała ID 123
            return entity;
        });

        Offense savedOffense = service.addOffense(offense1);

        ArgumentCaptor<OffenseEntity> captor = ArgumentCaptor.forClass(OffenseEntity.class);
        verify(repository).save(captor.capture());

        OffenseEntity capturedInDatabase = captor.getValue();

        assertEquals("title1", capturedInDatabase.getTitle());
        assertEquals("content1", capturedInDatabase.getContent());

        assertNotNull(savedOffense);
        assertEquals("title1", savedOffense.title());
        assertEquals("content1", savedOffense.content());

        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldEditOffenseWithCorrectData() {
        int idFromUrl = 1;
        Offense dto = new Offense(1, "Updated Title", "Updated Content");

        when(repository.existsById(idFromUrl)).thenReturn(true);
        when(repository.save(any(OffenseEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        Offense result = service.editOffense(idFromUrl, dto);

        ArgumentCaptor<OffenseEntity> captor = ArgumentCaptor.forClass(OffenseEntity.class);
        verify(repository).save(captor.capture());

        OffenseEntity captured = captor.getValue();

        assertEquals(idFromUrl, captured.getId());
        assertEquals("Updated Title", captured.getTitle());
        assertEquals("Updated Content", captured.getContent());

        assertNotNull(result);
        assertEquals("Updated Title", result.title());
    }

    @Test
    void shouldThrowExceptionWhenIdInBodyIsNull() {

        int idFromUrl = 1;
        Offense offenseWithNullId = new Offense(null, "title", "content");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.editOffense(idFromUrl, offenseWithNullId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowExceptionWhenIdsDoNotMatch() {

        int idFromUrl = 1;
        Offense offenseWithId99 = new Offense(99, "title", "content");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.editOffense(idFromUrl, offenseWithId99);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verifyNoInteractions(repository);
    }

    @Test
    void shouldDeleteOffenseWhenExists() {
        int idToDelete = 10;

        when(repository.existsById(idToDelete)).thenReturn(true);
        service.deleteOffense(idToDelete);

        verify(repository, times(1)).deleteById(idToDelete);
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistentOffense() {

        int idToDelete = 10;
        when(repository.existsById(idToDelete)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            service.deleteOffense(idToDelete);
        });

        verify(repository, never()).deleteById(anyInt());
    }
}
