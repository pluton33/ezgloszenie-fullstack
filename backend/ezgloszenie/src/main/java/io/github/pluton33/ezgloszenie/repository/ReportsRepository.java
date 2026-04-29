package io.github.pluton33.ezgloszenie.repository;

import io.github.pluton33.ezgloszenie.data.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportsRepository extends JpaRepository<ReportEntity, Long> {
}
