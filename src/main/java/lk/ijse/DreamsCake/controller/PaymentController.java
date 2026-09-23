package lk.ijse.DreamsCake.controller;

import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.PaymentDTO;
import lk.ijse.DreamsCake.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(value = "/order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderId(orderId);
        return new CommonResponse(OPERATION_SUCCESS, paymentDTO, SUCCESS_MESSAGE);
    }
}