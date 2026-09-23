package lk.ijse.DreamsCake.controller;

import jakarta.validation.Valid;
import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.SupplierDTO;
import lk.ijse.DreamsCake.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/v1/supplier")
@CrossOrigin
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping(value = "/next-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getNextSupplierId() {
        Long nextId = supplierService.getNextSupplierId();
        return new CommonResponse(OPERATION_SUCCESS, nextId, SUCCESS_MESSAGE);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        return new CommonResponse(OPERATION_SUCCESS, suppliers, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchSupplier(@PathVariable Long id) {
        SupplierDTO supplier = supplierService.searchSupplier(id);
        return new CommonResponse(OPERATION_SUCCESS, supplier, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterSuppliersByName(@RequestParam String name) {
        List<SupplierDTO> suppliers = supplierService.filterSuppliersByName(name);
        return new CommonResponse(OPERATION_SUCCESS, suppliers, SUCCESS_MESSAGE);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveSupplier(@Valid @RequestBody SupplierDTO supplierDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, null, errorMsg);
        }

        supplierService.saveSupplier(supplierDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateSupplier(@RequestBody SupplierDTO supplierDTO) {
        supplierService.updateSupplier(supplierDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }
}