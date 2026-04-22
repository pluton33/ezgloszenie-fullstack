package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.Offense;
import io.github.pluton33.ezgloszenie.data.OffenseEntity;
import io.github.pluton33.ezgloszenie.data.OffensesResponse;
import io.github.pluton33.ezgloszenie.repository.OffensesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OffenseServiceImpl implements OffenseService {
    @Autowired
    private OffensesRepository offensesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OffensesResponse getOffenses() {
        List<OffenseEntity> offenseEntities = offensesRepository.findAll();
        List<Offense> offenses = offenseEntities.stream()
                .map(entity -> Offense.fromEntity(entity))
                .toList();
        return new OffensesResponse(offenses);
    }

    @Override
    public Offense getOffenseById(int id) {
        return offensesRepository
                .findById(id)
                .map(entity -> Offense.fromEntity(entity))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak zgłoszenia"
                ));
    }

    @Override
    public Offense addOffense(Offense offense) {
        OffenseEntity offenseEntity = offense.toEntity();
        offenseEntity.setId(null);
        OffenseEntity savedEntity = offensesRepository.save(offenseEntity);

        return new Offense(
                savedEntity.getId(),
                savedEntity.getTitle(),
                savedEntity.getContent()
        );
    }

    @Override
    public Offense editOffense(Offense offense) {
        if(offense.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required");
        }

        if(!offensesRepository.existsById(offense.id())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offense not found");
        }
        OffenseEntity offenseEntity = offense.toEntity();
        OffenseEntity editedEntity = offensesRepository.save(offenseEntity);

        return new Offense(
                editedEntity.getId(),
                editedEntity.getTitle(),
                editedEntity.getContent()
        );
    }

    @Override
    public void deleteOffense(Integer id) {
        if(!offensesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offense not found");
        }
        offensesRepository.deleteById(id);
    }
}
