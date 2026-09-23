package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.RiderDTO;
import java.util.List;

public interface RiderService {
    void saveRider(RiderDTO riderDTO);
    void updateRider(RiderDTO riderDTO);
    void deleteRider(Long id);
    List<RiderDTO> getAllRiders();

    Long getNextRiderId();
}