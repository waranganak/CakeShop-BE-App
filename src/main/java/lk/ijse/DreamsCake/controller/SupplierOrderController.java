package lk.ijse.DreamsCake.controller;

import lk.ijse.DreamsCake.dto.SupplierOrderDTO;
import lk.ijse.DreamsCake.service.SupplierOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/supplier-orders")
@CrossOrigin(origins = "*")
public class SupplierOrderController {

    @Autowired
    private SupplierOrderService supplierOrderService;

    @PostMapping
    public ResponseEntity<String> saveOrder(@RequestBody SupplierOrderDTO dto) {
        try {
            supplierOrderService.saveOrder(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Supplier order and details saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}