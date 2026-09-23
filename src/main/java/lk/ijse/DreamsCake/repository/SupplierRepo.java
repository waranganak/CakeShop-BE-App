package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.dto.SupplierDTO;
import lk.ijse.DreamsCake.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier, Long> {

    @Query("SELECT new lk.ijse.DreamsCake.dto.SupplierDTO(s.id, s.supplierName, s.contactNumber, s.address) FROM Supplier s WHERE s.id = :id")
    SupplierDTO searchSupplier(@Param("id") Long id);

    @Query("SELECT new lk.ijse.DreamsCake.dto.SupplierDTO(s.id, s.supplierName, s.contactNumber, s.address) FROM Supplier s WHERE s.supplierName LIKE %:name%")
    List<SupplierDTO> filterSuppliersByName(@Param("name") String name);

    @Query("SELECT MAX(s.id) FROM Supplier s")
    Long findMaxId();

    Optional<Supplier> findBySupplierName(String supplierName);
}