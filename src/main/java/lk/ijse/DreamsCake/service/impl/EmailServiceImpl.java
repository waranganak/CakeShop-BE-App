package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.OrderDetailDTO;
import lk.ijse.DreamsCake.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmationEmail(String toEmail, String orderId, double totalAmount, List<OrderDetailDTO> orderDetails) {
        try {
            StringBuilder itemsText = new StringBuilder();
            if (orderDetails != null) {
                for (OrderDetailDTO orderDetailDTO : orderDetails) {
                    itemsText.append("- ")
                            .append(orderDetailDTO.getProductName())
                            .append(" | Qty: ")
                            .append(orderDetailDTO.getQuantity())
                            .append("\n");
                }
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order Confirmation & Invoice - Dream's Cake 🎂");
            message.setText("Dear Customer,\n\n" +
                    "Thank you for your order with Dream's Cake!\n\n" +
                    "Order ID: " + orderId + "\n\n" +
                    "--- Ordered Items ---\n" +
                    itemsText.toString() +
                    "---------------------\n" +
                    "Total Amount: Rs. " + totalAmount + "\n\n" +
                    "Your order has been placed successfully and is being processed.\n\n" +
                    "Best Regards,\n" +
                    "Dream's Cake Team");

            mailSender.send(message);
            System.out.println("Order confirmation email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}