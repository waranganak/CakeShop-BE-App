package lk.ijse.DreamsCake.controller;

import jakarta.validation.Valid;
import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.DeliveryDTO;
import lk.ijse.DreamsCake.dto.DeliveryDetailsResponseDTO;
import lk.ijse.DreamsCake.service.DeliveryService;
import lk.ijse.DreamsCake.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/v1/delivery")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final PaymentService paymentService;

    @PostMapping(value = "/assign", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse assignDelivery(@Valid @RequestBody DeliveryDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, null, errorMsg);
        }

        deliveryService.assignDelivery(dto);
        System.out.println("Delivery assigned successfully!");
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDeliveryByOrder(@PathVariable Long orderId) {
        DeliveryDetailsResponseDTO dto = deliveryService.getOrderByOrderId(orderId);
        return new CommonResponse(OPERATION_SUCCESS, dto, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/details/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getOrderDetailsForRider(@PathVariable Long orderId) {
        System.out.println("RIDER PORTAL REQUESTED ORDER ID: " + orderId);

        DeliveryDetailsResponseDTO dto = deliveryService.getOrderByOrderId(orderId);
        System.out.println(" FOUND DELIVERY DTO RESULT: " + dto);

        return new CommonResponse(OPERATION_SUCCESS, dto, SUCCESS_MESSAGE);
    }
    @GetMapping(value = "/rider/list/{riderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDeliveriesByRider(@PathVariable Long riderId) {
        System.out.println("FETCHING DELIVERIES FOR RIDER ID: " + riderId);
        List<DeliveryDetailsResponseDTO> list = deliveryService.getDeliveriesByRiderId(riderId);
        return new CommonResponse(OPERATION_SUCCESS, list, SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/complete/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse markAsDelivered(@PathVariable Long orderId) {
        DeliveryDetailsResponseDTO existingOrder = deliveryService.getOrderByOrderId(orderId);

        if (existingOrder == null) {
            return new CommonResponse(400, null, "Order not found!");
        }

        if ("DELIVERED".equals(existingOrder.getDeliveryStatus())) {
            return new CommonResponse(400, null, " This order has already been delivered!");
        }
        deliveryService.updateDeliveryStatus(orderId, "DELIVERED");

        try {
            paymentService.updatePaymentStatusByOrderId(orderId, "PAID");
        } catch (Exception e) {
            System.out.println("Error updating payment status: " + e.getMessage());
        }

        return new CommonResponse(OPERATION_SUCCESS, "Order marked as delivered and payment completed successfully!");
    }


}