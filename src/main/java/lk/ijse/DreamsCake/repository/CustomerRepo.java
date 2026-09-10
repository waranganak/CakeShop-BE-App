package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.dto.CustomerDTO;
import lk.ijse.DreamsCake.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT new lk.ijse.DreamsCake.dto.CustomerDTO(c.id, c.name, c.email, c.phone, c.address) " +
            "FROM Customer c " +
            "WHERE (?1 IS NULL OR c.name LIKE %?1%)")
    List<CustomerDTO> filterCustomersByName(String name);

    @Query(value = "SELECT new lk.ijse.DreamsCake.dto.CustomerDTO(c.id, c.name, c.email, c.phone, c.address) " +
            "FROM Customer c " +
            "WHERE c.id = ?1")
    CustomerDTO searchCustomer(Long id);

    Optional<Customer> findByName(String name);
}