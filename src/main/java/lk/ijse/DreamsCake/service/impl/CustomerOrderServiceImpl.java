package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.CustomerOrderDTO;
import lk.ijse.DreamsCake.dto.OrderDetailDTO;
import lk.ijse.DreamsCake.entity.*;
import lk.ijse.DreamsCake.enums.OrderStatus;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.CustomerOrderRepo;
import lk.ijse.DreamsCake.repository.CustomerRepo;
import lk.ijse.DreamsCake.repository.IngredientRepo;
import lk.ijse.DreamsCake.repository.ProductRepo;
import lk.ijse.DreamsCake.service.CustomerOrderService;
import lk.ijse.DreamsCake.service.EmailService; // 👈 1. EmailService එක ඉම්පෝට් කරගන්න
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final CustomerOrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final IngredientRepo ingredientRepo;
    private final EmailService emailService;

    public CustomerOrderServiceImpl(CustomerOrderRepo orderRepo, CustomerRepo customerRepo, ProductRepo productRepo, IngredientRepo ingredientRepo, EmailService emailService) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.ingredientRepo = ingredientRepo;
        this.emailService = emailService;
    }

    @Override
    public void placeOrder(CustomerOrderDTO orderDTO) {
        log.info("Saving order for customer ID: {}", orderDTO.getCustomerId());

        Customer customer = customerRepo.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ApiException("Customer not found with ID: " + orderDTO.getCustomerId()));

        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setCustomer(customer);

        List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();

        if (orderDTO.getOrderDetails() != null) {
            for (OrderDetailDTO dto : orderDTO.getOrderDetails()) {
                Product product = productRepo.findById(dto.getProductId())
                        .orElseThrow(() -> new ApiException("Product not found with ID: " + dto.getProductId()));

                OrderDetail detail = new OrderDetail();
                detail.setCustomerOrder(order);
                detail.setProduct(product);
                detail.setQuantity(dto.getQuantity());
                detail.setSubTotal(dto.getPrice() * dto.getQuantity());

                orderDetails.add(detail);

                orderDetailDTOs.add(new OrderDetailDTO(
                        product.getId(),
                        product.getProductName(),
                        dto.getQuantity(),
                        detail.getSubTotal()
                ));
            }
        }

        order.setOrderDetails(orderDetails);
        CustomerOrder savedOrder = orderRepo.save(order);

        log.info("Order saved successfully to customer_orders table!");

        try {
            String customerEmail = customer.getEmail();
            if (customerEmail != null && !customerEmail.isEmpty()) {
                System.out.println("DEBUG - Order Details List Size: " + orderDetailDTOs.size());
                emailService.sendOrderConfirmationEmail(
                        customerEmail,
                        String.valueOf(savedOrder.getId()),
                        savedOrder.getTotalAmount(),
                        orderDetailDTOs
                );
                log.info("Order confirmation email triggered for: {}", customerEmail);
            }
        } catch (Exception e) {
            log.error("Failed to send order confirmation email: {}", e.getMessage());
        }
    }

    @Override
    public List<CustomerOrderDTO> getOrdersByCustomer(Long customerId) {
        log.info("Fetching orders for customer ID: {}", customerId);
        return orderRepo.getOrdersByCustomerId(customerId);
    }
}