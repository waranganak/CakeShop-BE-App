package lk.ijse.DreamsCake.controller;

import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.AuditLogDTO;
import lk.ijse.DreamsCake.service.AuditLogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping(value = "v1/logs")
@CrossOrigin
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveLog(@RequestBody AuditLogDTO auditLogDTO) {
        auditLogService.saveLog(auditLogDTO.getAction(), auditLogDTO.getUserId());
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllLogs() {
        List<AuditLogDTO> logs = auditLogService.getAllLogs();
        return new CommonResponse(OPERATION_SUCCESS, logs, SUCCESS_MESSAGE);
    }
}