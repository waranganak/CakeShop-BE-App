package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.RiderDTO;
import lk.ijse.DreamsCake.entity.Rider;
import lk.ijse.DreamsCake.entity.User;
import lk.ijse.DreamsCake.enums.RoleType;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.RiderRepo;
import lk.ijse.DreamsCake.repository.UserRepo;
import lk.ijse.DreamsCake.service.EmailService;
import lk.ijse.DreamsCake.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final RiderRepo riderRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<RiderDTO> getAllRiders() {
        log.info("Fetching all riders from database");
        List<Rider> riders = riderRepo.findAll();
        return riders.stream().map(r -> new RiderDTO(
                r.getId(),
                r.getName(),
                r.getEmail(),
                null,
                r.getPhone(),
                r.getVehicleNumber(),
                r.getStatus(),
                r.getRole()
        )).collect(Collectors.toList());
    }

    @Override
    public void saveRider(RiderDTO dto) {
        log.info("Saving rider: {}", dto.getName());

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ApiException(400, "Rider name cannot be empty");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ApiException(400, "Rider email cannot be empty");
        }
        if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
            throw new ApiException(400, "Rider phone number cannot be empty");
        }
        if (dto.getVehicleNumber() == null || dto.getVehicleNumber().trim().isEmpty()) {
            throw new ApiException(400, "Vehicle number cannot be empty");
        }

        if (riderRepo.existsByEmail(dto.getEmail())) {
            throw new ApiException(400, "A rider with this email address already exists!");
        }
        if (riderRepo.existsByPhone(dto.getPhone())) {
            throw new ApiException(400, "A rider with this phone number already exists!");
        }
        if (riderRepo.existsByVehicleNumber(dto.getVehicleNumber())) {
            throw new ApiException(400, "A rider with this vehicle number already exists!");
        }

        String rawPassword = "Cake@" + (int)(Math.random() * 9000 + 1000);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        RoleType assignedRole = dto.getRole() != null ? dto.getRole() : RoleType.RIDER;

        Rider rider = new Rider();
        rider.setName(dto.getName());
        rider.setEmail(dto.getEmail());
        rider.setPassword(encodedPassword);
        rider.setPhone(dto.getPhone());
        rider.setVehicleNumber(dto.getVehicleNumber());
        rider.setStatus("AVAILABLE");
        rider.setRole(assignedRole);
        riderRepo.save(rider);

        User user = new User();
        user.setUserName(dto.getEmail());
        user.setPassword(encodedPassword);
        user.setUserRoles(assignedRole.name());
        userRepo.save(user);

        try {
            emailService.sendRiderCredentials(dto.getEmail(), dto.getName(), rawPassword);
            log.info("Credentials email sent successfully to rider: {}", dto.getEmail());
        } catch (Exception e) {
            log.error("Failed to send credentials email: {}", e.getMessage());
        }
    }

    @Override
    public void updateRider(RiderDTO dto) {
        log.info("Updating rider with ID: {}", dto.getId());

        if (dto.getId() == null) {
            throw new ApiException(400, "Rider ID cannot be null for update");
        }

        Rider rider = riderRepo.findById(dto.getId())
                .orElseThrow(() -> new ApiException(404, "Rider not found with ID: " + dto.getId()));

        String oldEmail = rider.getEmail();

        if (riderRepo.existsByPhoneAndIdNot(dto.getPhone(), dto.getId())) {
            throw new ApiException(400, "Another rider is already using this phone number!");
        }
        if (riderRepo.existsByVehicleNumberAndIdNot(dto.getVehicleNumber(), dto.getId())) {
            throw new ApiException(400, "Another rider is already using this vehicle number!");
        }

        rider.setName(dto.getName());
        rider.setEmail(dto.getEmail());
        rider.setPhone(dto.getPhone());
        rider.setVehicleNumber(dto.getVehicleNumber());

        if (dto.getStatus() != null) {
            rider.setStatus(dto.getStatus());
        }

        if (dto.getRole() != null) {
            rider.setRole(dto.getRole());
            userRepo.findByUserName(oldEmail).ifPresent(user -> {
                user.setUserRoles(dto.getRole().name());
                userRepo.save(user);
            });
        }

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            rider.setPassword(encodedPassword);

            userRepo.findByUserName(oldEmail).ifPresent(user -> {
                user.setPassword(encodedPassword);
                userRepo.save(user);
            });
        }

        if (!oldEmail.equals(dto.getEmail())) {
            userRepo.findByUserName(oldEmail).ifPresent(user -> {
                user.setUserName(dto.getEmail());
                userRepo.save(user);
            });
        }

        riderRepo.save(rider);
    }

    @Override
    public void deleteRider(Long id) {
        log.info("Deleting rider ID: {}", id);

        Rider rider = riderRepo.findById(id)
                .orElseThrow(() -> new ApiException(404, "Rider not found with ID: " + id));

        userRepo.findByUserName(rider.getEmail()).ifPresent(userRepo::delete);

        riderRepo.deleteById(id);
    }

    @Override
    public Long getNextRiderId() {
        Long maxId = riderRepo.findMaxId();
        return (maxId == null) ? 1L : maxId + 1;
    }
}