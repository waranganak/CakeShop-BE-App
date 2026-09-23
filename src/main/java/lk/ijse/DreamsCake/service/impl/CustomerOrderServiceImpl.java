package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.CustomerOrderDTO;
import lk.ijse.DreamsCake.dto.OrderDetailDTO;
import lk.ijse.DreamsCake.entity.*;
import lk.ijse.DreamsCake.enums.OrderStatus;
import lk.ijse.DreamsCake.enums.PaymentMethod;
import lk.ijse.DreamsCake.enums.PaymentStatus;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.*;
import lk.ijse.DreamsCake.service.CustomerOrderService;
import lk.ijse.DreamsCake.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final CustomerOrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final IngredientRepo ingredientRepo;
    private final ProductIngredientRepo productIngredientRepo;
    private final EmailService emailService;

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

        Payment payment = new Payment();
        if (orderDTO.getPaymentMethod() != null && !orderDTO.getPaymentMethod().isEmpty()) {
            payment.setPaymentMethod(PaymentMethod.valueOf(orderDTO.getPaymentMethod()));
        } else {
            payment.setPaymentMethod(PaymentMethod.COD);
        }
        payment.setAmount(orderDTO.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCustomerOrder(order);
        order.setPayment(payment);

        List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();

        if (orderDTO.getOrderDetails() != null) {
            for (OrderDetailDTO dto : orderDTO.getOrderDetails()) {
                Product product = productRepo.findById(dto.getProductId())
                        .orElseThrow(() -> new ApiException("Product not found with ID: " + dto.getProductId()));

                if (product.getQty() < dto.getQuantity()) {
                    throw new ApiException("Insufficient stock for product: " + product.getProductName());
                }

                product.setQty(product.getQty() - dto.getQuantity());
                productRepo.save(product);
                List<ProductIngredient> productIngredients = productIngredientRepo.findByProduct(product);
                for (ProductIngredient pi : productIngredients) {
                    Ingredient ingredient = pi.getIngredient();
                    double requiredQtyPerUnit = pi.getRequiredQuantity();
                    double totalRequiredQty = requiredQtyPerUnit * dto.getQuantity();

                    double availableStock = ingredient.getQuantityInStock();
                    String unit = ingredient.getUnit() != null ? ingredient.getUnit().name() : "";
                    double stockInBaseUnit = availableStock;
                    if ("KG".equals(unit)) {
                        stockInBaseUnit = availableStock * 1000;
                    }

                    if (stockInBaseUnit < totalRequiredQty) {
                        throw new ApiException("Insufficient stock for ingredient: " + ingredient.getIngredientName());
                    }

                    double remainingBaseUnit = stockInBaseUnit - totalRequiredQty;
                    if ("KG".equals(unit)) {
                        ingredient.setQuantityInStock(remainingBaseUnit / 1000.0);
                    } else {
                        ingredient.setQuantityInStock(remainingBaseUnit);
                    }

                    ingredientRepo.save(ingredient);
                }

                OrderDetail detail = new OrderDetail();
                detail.setCustomerOrder(order);
                detail.setProduct(product);
                detail.setQuantity(dto.getQuantity());
                detail.setSubTotal(dto.getPrice() * dto.getQuantity());

                orderDetails.add(detail);

                orderDetailDTOs.add(new OrderDetailDTO(
                        product.getId(),
                        dto.getProductName(),
                        dto.getQuantity(),
                        detail.getSubTotal()
                ));
            }
        }

        order.setOrderDetails(orderDetails);
        CustomerOrder savedOrder = orderRepo.save(order);

        log.info("Order, Payment, and Stock updated successfully!");

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

    @Override
    public List<CustomerOrderDTO> getAllOrders() {
        log.info("Fetching all customer orders for admin...");

        List<CustomerOrder> orders = orderRepo.findAllOrdersWithPayments();
        List<CustomerOrderDTO> orderDTOs = new ArrayList<>();

        for (CustomerOrder order : orders) {
            CustomerOrderDTO dto = new CustomerOrderDTO();
            dto.setId(order.getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());

            if (order.getCustomer() != null) {
                dto.setCustomerId(order.getCustomer().getId());
            }

            List<OrderDetailDTO> detailDTOs = new ArrayList<>();
            if (order.getOrderDetails() != null) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    OrderDetailDTO detailDTO = new OrderDetailDTO();
                    detailDTO.setProductId(detail.getProduct() != null ? detail.getProduct().getId() : null);

                    String prodName = (detail.getProduct() != null && detail.getProduct().getProductName() != null)
                            ? detail.getProduct().getProductName()
                            : "Unknown Product";
                    detailDTO.setProductName(prodName);

                    detailDTO.setQuantity(detail.getQuantity());
                    detailDTO.setPrice(detail.getSubTotal());

                    detailDTOs.add(detailDTO);
                }
            }
            dto.setOrderDetails(detailDTOs);
            orderDTOs.add(dto);
        }

        return orderDTOs;
    }
}