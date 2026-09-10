package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.dto.CustomerOrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import lk.ijse.DreamsCake.entity.CustomerOrder;

import java.util.List;

@Repository
public interface CustomerOrderRepo extends JpaRepository<CustomerOrder, Long> {

    @Query("SELECT new lk.ijse.DreamsCake.dto.CustomerOrderDTO(" +
            "o.id, o.orderDate, o.totalAmount, o.status, o.customer.id) " +
            "FROM CustomerOrder o WHERE o.customer.id = ?1")
    List<CustomerOrderDTO> getOrdersByCustomerId(Long customerId);
}