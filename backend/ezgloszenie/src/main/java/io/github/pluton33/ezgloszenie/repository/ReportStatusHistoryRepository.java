package io.github.pluton33.ezgloszenie.repository;

import io.github.pluton33.ezgloszenie.data.ReportStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportStatusHistoryRepository extends JpaRepository<ReportStatusHistoryEntity, Long> {
    @Query("SELECT rsh FROM ReportsStatusHistory rsh WHERE rsh.report.id = :id AND rsh.validTo is NULL")
    List<ReportStatusHistoryEntity> findByReportIdAndValidToIsNull(@Param("id") Long id);

    List<ReportStatusHistoryEntity> findByReportId(Long report_id);
}
