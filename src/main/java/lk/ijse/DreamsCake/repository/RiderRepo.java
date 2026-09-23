package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepo extends JpaRepository<Rider, Long> {

    @Query("SELECT MAX(r.id) FROM Rider r")
    Long findMaxId();

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByVehicleNumber(String vehicleNumber);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    boolean existsByVehicleNumberAndIdNot(String vehicleNumber, Long id);

    Optional<Rider> findByEmail(String usernameOrEmail);
}

