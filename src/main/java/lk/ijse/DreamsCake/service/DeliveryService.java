package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.DeliveryDTO;
import lk.ijse.DreamsCake.dto.DeliveryDetailsResponseDTO;

import java.util.List;

public interface DeliveryService {
    void assignDelivery(DeliveryDTO dto);

    DeliveryDetailsResponseDTO getOrderByOrderId(Long orderId);

    void updateDeliveryStatus(Long orderId, String status);

    List<DeliveryDetailsResponseDTO> getDeliveriesByRiderId(Long riderId);
}