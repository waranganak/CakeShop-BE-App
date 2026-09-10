package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.entity.SupplierOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierOrderRepo extends JpaRepository<SupplierOrder, Long> {
}