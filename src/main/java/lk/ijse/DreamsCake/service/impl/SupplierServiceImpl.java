package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.SupplierDTO;
import lk.ijse.DreamsCake.entity.Supplier;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.SupplierRepo;
import lk.ijse.DreamsCake.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;

    public SupplierServiceImpl(SupplierRepo supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        log.info("Fetching all suppliers from database");
        List<Supplier> suppliers = supplierRepo.findAll();
        return suppliers.stream().map(sup -> new SupplierDTO(
                sup.getId(),
                sup.getSupplierName(),
                sup.getContactNumber(),
                sup.getAddress()
        )).collect(Collectors.toList());
    }

    @Override
    public void saveSupplier(SupplierDTO supplierDTO) {
        log.info("Saving supplier: {}", supplierDTO.getName());

        if (supplierDTO.getName() == null || supplierDTO.getName().trim().isEmpty()) {
            throw new ApiException(400, "Supplier name cannot be empty");
        }

        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierDTO.getName());
        supplier.setContactNumber(supplierDTO.getContact());
        supplier.setAddress(supplierDTO.getAddress());

        supplierRepo.save(supplier);
    }

    @Override
    public void updateSupplier(SupplierDTO supplierDTO) {
        log.info("Updating supplier with ID: {}", supplierDTO.getId());

        if (supplierDTO.getId() == null) {
            throw new ApiException(400, "Supplier ID cannot be null for update");
        }

        Supplier supplier = supplierRepo.findById(supplierDTO.getId())
                .orElseThrow(() -> new ApiException(404, "Supplier not found with ID: " + supplierDTO.getId()));

        supplier.setSupplierName(supplierDTO.getName());
        supplier.setContactNumber(supplierDTO.getContact());
        supplier.setAddress(supplierDTO.getAddress());

        supplierRepo.save(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier ID: {}", id);

        if (!supplierRepo.existsById(id)) {
            throw new ApiException(404, "Supplier not found with ID: " + id);
        }

        supplierRepo.deleteById(id);
    }

    @Override
    public SupplierDTO searchSupplier(Long id) {
        log.info("Searching supplier by ID: {}", id);
        SupplierDTO supplierDTO = supplierRepo.searchSupplier(id);
        if (supplierDTO == null) {
            throw new ApiException(404, "Supplier not found with ID: " + id);
        }
        return supplierDTO;
    }

    @Override
    public List<SupplierDTO> filterSuppliersByName(String name) {
        log.info("Filtering suppliers by name: {}", name);
        return supplierRepo.filterSuppliersByName(name);
    }

    @Override
    public Long getNextSupplierId() {
        Long maxId = supplierRepo.findMaxId();
        return (maxId == null) ? 1L : maxId + 1;
    }
}