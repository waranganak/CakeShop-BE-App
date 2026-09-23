package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.PaymentDTO;

public interface PaymentService {
    void updatePaymentStatusByOrderId(Long orderId, String complete);

    PaymentDTO getPaymentByOrderId(Long orderId);
}
