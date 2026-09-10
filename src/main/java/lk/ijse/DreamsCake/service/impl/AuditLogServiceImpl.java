package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.AuditLogDTO;
import lk.ijse.DreamsCake.entity.AuditLog;
import lk.ijse.DreamsCake.entity.User;
import lk.ijse.DreamsCake.exception.ResourceNotFoundException;
import lk.ijse.DreamsCake.repository.AuditLogRepo;
import lk.ijse.DreamsCake.repository.UserRepo;
import lk.ijse.DreamsCake.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepo auditLogRepo;
    private final UserRepo userRepo;

    public AuditLogServiceImpl(AuditLogRepo auditLogRepo, UserRepo userRepo) {
        this.auditLogRepo = auditLogRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void saveLog(String action, Long userId) {
        try {
            User user = null;
            if (userId != null) {
                user = userRepo.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            }

            AuditLog auditLog = new AuditLog();
            auditLog.setAction(action);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLog.setUser(user);

            auditLogRepo.save(auditLog);
        } catch (ResourceNotFoundException e) {
            log.warn("Warning while saving audit log: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }
    }

    @Override
    public List<AuditLogDTO> getAllLogs() {
        log.info("Fetching all audit logs for UI");
        List<AuditLog> logs = auditLogRepo.findAll();
        return logs.stream().map(logEntity -> {
            Long userId = (logEntity.getUser() != null) ? logEntity.getUser().getUserId() : null;
            String username = (logEntity.getUser() != null) ? logEntity.getUser().getUserName() : "Admin";

            return new AuditLogDTO(
                    logEntity.getId(),
                    logEntity.getAction(),
                    logEntity.getTimestamp(),
                    userId,
                    username
            );
        }).collect(Collectors.toList());
    }
}