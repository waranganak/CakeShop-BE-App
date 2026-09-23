package lk.ijse.DreamsCake.controller;

import jakarta.validation.Valid;
import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.RiderDTO;
import lk.ijse.DreamsCake.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/v1/riders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RiderController {

    private final RiderService riderService;

    @GetMapping(value = "/next-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getNextRiderId() {
        Long nextId = riderService.getNextRiderId();
        return new CommonResponse(OPERATION_SUCCESS, nextId, SUCCESS_MESSAGE);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllRiders() {
        List<RiderDTO> riders = riderService.getAllRiders();
        return new CommonResponse(OPERATION_SUCCESS, riders, SUCCESS_MESSAGE);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveRider(@Valid @RequestBody RiderDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, null, errorMsg);
        }

        riderService.saveRider(dto);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateRider(@Valid @RequestBody RiderDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, null, errorMsg);
        }

        riderService.updateRider(dto);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteRider(@PathVariable Long id) {
        riderService.deleteRider(id);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }
}