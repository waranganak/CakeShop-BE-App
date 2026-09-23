package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE d.customerOrder.id = :orderId")
    Optional<Delivery> findByCustomerOrderId(Long orderId);

    List<Delivery> findByRider_Id(Long riderId);

}