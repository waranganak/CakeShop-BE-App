package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.PaymentDTO;
import lk.ijse.DreamsCake.entity.Payment;
import lk.ijse.DreamsCake.enums.PaymentStatus;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.PaymentRepo;
import lk.ijse.DreamsCake.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepository;

    @Override
    public void updatePaymentStatusByOrderId(Long orderId, String status) {
        log.info("Updating payment status to {} for Order ID: {}", status, orderId);

        Payment payment = paymentRepository.findByCustomerOrderId(orderId);

        if (payment == null) {
            log.warn("Payment not found for Order ID: {}", orderId);
            throw new ApiException(404, "Payment not found for Order ID: " + orderId);
        }

        try {
            payment.setPaymentStatus(PaymentStatus.valueOf(status));
            paymentRepository.save(payment);
            log.info("Payment status successfully updated to {} for Order ID: {}", status, orderId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment status value: {}", status);
            throw new ApiException(400, "Invalid payment status: " + status);
        }
    }

    @Override
    public PaymentDTO getPaymentByOrderId(Long orderId) {
        log.info("Fetching payment details for Order ID: {}", orderId);

        Payment payment = paymentRepository.findByCustomerOrderId(orderId);

        if (payment == null) {
            log.warn("Payment not found for Order ID: {}", orderId);
            throw new ApiException(404, "Payment not found for Order ID: " + orderId);
        }

        Long cOrderId = (payment.getCustomerOrder() != null) ? payment.getCustomerOrder().getId() : orderId;

        return new PaymentDTO(
                payment.getId(),
                null,
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                cOrderId
        );
    }
}