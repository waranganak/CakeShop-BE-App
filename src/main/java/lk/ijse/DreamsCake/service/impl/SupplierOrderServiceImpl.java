package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.SupplierOrderDTO;
import lk.ijse.DreamsCake.dto.SupplierOrderDetailDTO;
import lk.ijse.DreamsCake.entity.Ingredient;
import lk.ijse.DreamsCake.entity.Supplier;
import lk.ijse.DreamsCake.entity.SupplierOrder;
import lk.ijse.DreamsCake.entity.SupplierOrderDetail;
import lk.ijse.DreamsCake.enums.SupplierOrderStatus;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.IngredientRepo;
import lk.ijse.DreamsCake.repository.SupplierOrderRepo;
import lk.ijse.DreamsCake.repository.SupplierRepo;
import lk.ijse.DreamsCake.service.SupplierOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SupplierOrderServiceImpl implements SupplierOrderService {

    private final SupplierOrderRepo supplierOrderRepo;
    private final SupplierRepo supplierRepo;
    private final IngredientRepo ingredientRepo;

    @Override
    public void saveOrder(SupplierOrderDTO dto) {
        log.info("Saving supplier order for Supplier ID: {}", dto.getSupplierId());

        if (dto.getSupplierId() == null) {
            throw new ApiException(400, "Supplier ID cannot be null");
        }

        Supplier supplier = supplierRepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new ApiException(404, "Supplier not found with ID: " + dto.getSupplierId()));

        SupplierOrder supplierOrder = new SupplierOrder();
        supplierOrder.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDate.now());

        try {
            supplierOrder.setStatus(dto.getStatus() != null ? SupplierOrderStatus.valueOf(dto.getStatus()) : SupplierOrderStatus.PENDING);
        } catch (IllegalArgumentException e) {
            log.error("Invalid supplier order status value: {}", dto.getStatus());
            throw new ApiException(400, "Invalid supplier order status: " + dto.getStatus());
        }

        supplierOrder.setSupplier(supplier);

        List<SupplierOrderDetail> detailsList = new ArrayList<>();
        if (dto.getOrderDetails() != null && !dto.getOrderDetails().isEmpty()) {
            for (SupplierOrderDetailDTO detailDTO : dto.getOrderDetails()) {
                if (detailDTO.getIngredientId() == null) {
                    throw new ApiException(400, "Ingredient ID cannot be null in order details");
                }

                Ingredient ingredient = ingredientRepo.findById(detailDTO.getIngredientId())
                        .orElseThrow(() -> new ApiException(404, "Ingredient not found with ID: " + detailDTO.getIngredientId()));

                SupplierOrderDetail detail = new SupplierOrderDetail();
                detail.setQuantity(detailDTO.getQuantity());
                detail.setIngredient(ingredient);
                detail.setSupplierOrder(supplierOrder);

                detailsList.add(detail);
            }
        } else {
            throw new ApiException(400, "Supplier order must contain at least one order detail");
        }

        supplierOrder.setSupplierOrderDetails(detailsList);

        supplierOrderRepo.save(supplierOrder);
        log.info("Supplier order successfully saved for Supplier ID: {}", dto.getSupplierId());
    }
}