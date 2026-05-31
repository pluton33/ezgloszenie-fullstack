package io.github.pluton33.ezgloszenie.repository;

import io.github.pluton33.ezgloszenie.data.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
}

