package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.AuditLogDTO;

import java.util.List;

public interface AuditLogService {
    void saveLog(String action, Long userId);

    List<AuditLogDTO> getAllLogs();
}
