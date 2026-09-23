package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.SupplierDTO;
import java.util.List;

public interface SupplierService {
    List<SupplierDTO> getAllSuppliers();
    void saveSupplier(SupplierDTO supplierDTO);
    void updateSupplier(SupplierDTO supplierDTO);
    void deleteSupplier(Long id);
    SupplierDTO searchSupplier(Long id);
    List<SupplierDTO> filterSuppliersByName(String name);
    Long getNextSupplierId();
}