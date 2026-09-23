package lk.ijse.DreamsCake.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/v1/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askChatbot(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message") != null ? request.get("message").toLowerCase() : "";
        String reply;

        if (userMessage.contains("hi") || userMessage.contains("hello") || userMessage.contains("hey") || userMessage.contains("morning")) {
            reply = "Hello! Welcome to Dream's Cake. Feel free to browse our menu and place your order.";
        } else if (userMessage.contains("help") || userMessage.contains("support")) {
            reply = "I'm here to help! You can ask me about our delivery charges, opening hours, payment methods, or check your orders in your dashboard.";

        } else if (userMessage.contains("charge") || userMessage.contains("fee") || userMessage.contains("cost")) {
            reply = "Our standard delivery charge is Rs. 400.00.";
        } else if (userMessage.contains("how long") || userMessage.contains("time") || userMessage.contains("deliver")) {
            reply = "Orders are usually delivered within 2 hours of placement!";
        } else if (userMessage.contains("track") || userMessage.contains("delivery status")) {
            reply = "You can easily track your live delivery progress by going to the 'Delivery Process' section in your customer dashboard.";
        } else if (userMessage.contains("history") || userMessage.contains("past orders") || userMessage.contains("my orders")) {
            reply = "You can check the status of your current and past orders under the 'My Orders' section in your dashboard.";

        } else if (userMessage.contains("payment") || userMessage.contains("pay") || userMessage.contains("methods")) {
            reply = "We accept Cash on Delivery (COD) as well as Credit and Debit Card payments.";
        } else if (userMessage.contains("cod") || userMessage.contains("cash on delivery")) {
            reply = "Yes! We fully support Cash on Delivery (COD) for all cake orders.";
        } else if (userMessage.contains("card") || userMessage.contains("credit") || userMessage.contains("debit")) {
            reply = "Yes, you can securely pay using your credit or debit card during checkout.";

        } else if (userMessage.contains("hours") || userMessage.contains("open") || userMessage.contains("time")) {
            reply = "We are open every day from 8:00 AM to 10:00 PM!";
        } else if (userMessage.contains("weekend") || userMessage.contains("sunday") || userMessage.contains("saturday")) {
            reply = "Yes, we are open on weekends too from 8:00 AM to 10:00 PM!";
        } else if (userMessage.contains("hotline") || userMessage.contains("contact") || userMessage.contains("phone")) {
            reply = "You can reach our customer support hotline at +94 91 2233445 or email support@dreamscake.lk.";

        } else {
            reply = "I'm not quite sure about that. Please check your dashboard or contact our support hotline for further assistance!";
        }
        
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
