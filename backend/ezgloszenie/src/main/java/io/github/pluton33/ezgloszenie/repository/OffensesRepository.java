package io.github.pluton33.ezgloszenie.repository;

import io.github.pluton33.ezgloszenie.data.OffenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffensesRepository extends JpaRepository<OffenseEntity, Integer> {
}
