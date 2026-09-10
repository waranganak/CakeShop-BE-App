package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.OrderDetailDTO;
import java.util.List;

public interface EmailService {
    void sendOrderConfirmationEmail(String toEmail, String orderId, double totalAmount, List<OrderDetailDTO> orderDetails);
}