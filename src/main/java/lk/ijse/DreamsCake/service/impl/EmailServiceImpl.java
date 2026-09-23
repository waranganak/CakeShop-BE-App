package lk.ijse.DreamsCake.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lk.ijse.DreamsCake.dto.OrderDetailDTO;
import lk.ijse.DreamsCake.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmationEmail(String toEmail, String orderId, double totalAmount, List<OrderDetailDTO> orderDetails) {
        try {
            byte[] qrCodeBytes = generateQRCodeBytes(orderId);

            StringBuilder itemsHtml = new StringBuilder();
            if (orderDetails != null) {
                for (OrderDetailDTO orderDetailDTO : orderDetails) {
                    itemsHtml.append("<li>")
                            .append(orderDetailDTO.getProductName())
                            .append(" (Qty: ")
                            .append(orderDetailDTO.getQuantity())
                            .append(")</li>");
                }
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Order Confirmation & Invoice - Dream's Cake 🎂 (#" + orderId + ")");

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333; max-width: 600px; margin: auto; border: 1px solid #f0f0f0; border-radius: 10px;'>" +
                    "<h2 style='color: #e91e63; text-align: center;'>Thank you for your order, Dream's Cake! 🍰</h2>" +
                    "<p>Dear Customer,</p>" +
                    "<p>Your order has been placed successfully and is being processed.</p>" +
                    "<hr style='border: none; border-top: 1px solid #eee;'/>" +
                    "<p><b>Order ID:</b> #" + orderId + "</p>" +
                    "<h3>Ordered Items:</h3>" +
                    "<ul>" + itemsHtml.toString() + "</ul>" +
                    "<p><b>Total Amount:</b> Rs. " + String.format("%.2f", totalAmount) + "</p>" +
                    "<hr style='border: none; border-top: 1px solid #eee;'/>" +
                    "<div style='text-align: center; margin-top: 20px;'>" +
                    "<p style='color: #666; font-size: 14px;'>Please show this QR code to our delivery rider upon arrival:</p>" +
                    "<img src='cid:qrCode' alt='Order QR Code' style='width: 160px; height: 160px; border: 5px solid #fff; box-shadow: 0 0 10px rgba(0,0,0,0.1);'/>" +
                    "</div>" +
                    "<br><p style='text-align: center; color: #888; font-size: 12px;'>Dream's Cake Management System</p>" +
                    "</div>";

            helper.setText(htmlContent, true);
            helper.addInline("qrCode", new ByteArrayDataSource(qrCodeBytes, "image/png"));

            mailSender.send(message);
            System.out.println("Order confirmation email with inline QR code sent successfully to " + toEmail);

        } catch (MessagingException | WriterException | IOException e) {
            System.err.println("Failed to send email with QR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendRiderCredentials(String toEmail, String riderName, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Welcome to Dream's Cake - Rider Portal Credentials 🏍️");
            message.setText("Hello " + riderName + ",\n\n" +
                    "You have been successfully registered as a Delivery Rider for Dream's Cake.\n" +
                    "You can now login to the Rider Portal using the following credentials:\n\n" +
                    "Email: " + toEmail + "\n" +
                    "Password: " + password + "\n\n" +
                    "Please keep your credentials secure and do not share them with anyone.\n\n" +
                    "Best Regards,\n" +
                    "Dream's Cake Management Team");

            mailSender.send(message);
            System.out.println("Rider credentials email sent successfully to " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send rider credentials email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private byte[] generateQRCodeBytes(String orderId) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(orderId, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}