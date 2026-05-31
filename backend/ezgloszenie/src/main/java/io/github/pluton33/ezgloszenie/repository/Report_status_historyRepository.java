package io.github.pluton33.ezgloszenie.repository;

import io.github.pluton33.ezgloszenie.data.Report_status_historyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Report_status_historyRepository extends JpaRepository<Report_status_historyEntity, Long> {
    @Query("SELECT rsh FROM ReportsStatusHistory rsh WHERE rsh.report.id = :id AND rsh.valid_to is NULL")
    List<Report_status_historyEntity> findByReportId(@Param("id") Long id);

    List<Report_status_historyEntity> findByReport_Id(Long report_id);
}
