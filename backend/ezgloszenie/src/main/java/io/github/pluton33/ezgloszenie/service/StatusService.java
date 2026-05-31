package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.StatusResponse;

public interface StatusService {
    StatusResponse getStatusReport(Long reportId);
    void updateStatus(Long reportId, String name);
}
