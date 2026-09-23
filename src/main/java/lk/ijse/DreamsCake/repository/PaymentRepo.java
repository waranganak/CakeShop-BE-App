package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Payment findByCustomerOrderId(Long orderId);
}