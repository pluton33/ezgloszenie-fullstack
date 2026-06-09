package io.github.pluton33.ezgloszenie.repository;

import io.github.pluton33.ezgloszenie.data.ReportEntity;
import io.github.pluton33.ezgloszenie.data.ReportStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportsRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByUserEmail(String email);
}
