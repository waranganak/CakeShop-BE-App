package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.CustomerOrderDTO;
import lk.ijse.DreamsCake.entity.Customer;
import lk.ijse.DreamsCake.entity.CustomerOrder;
import lk.ijse.DreamsCake.exception.CustomerException;
import lk.ijse.DreamsCake.repository.CustomerOrderRepo;
import lk.ijse.DreamsCake.repository.CustomerRepo;
import lk.ijse.DreamsCake.service.AuditLogService;
import lk.ijse.DreamsCake.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CustomerOrderServiceImpl implements OrderService {

    private final CustomerOrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final AuditLogService auditLogService;

    public OrderServiceImpl(CustomerOrderRepo orderRepo, CustomerRepo customerRepo, AuditLogService auditLogService) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.auditLogService = auditLogService;
    }

    @Override
    public void placeOrder(CustomerOrderDTO orderDTO) {
        log.info("Placing order for customer ID: {}", orderDTO.getCustomerId());

        Customer customer = customerRepo.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new CustomerException(404, "Customer not found"));

        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(LocalDateTime.now());
        order.setCakeType(orderDTO.getCakeType());
        order.setQuantity(orderDTO.getQuantity());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setStatus("PENDING");
        order.setCustomer(customer);

        orderRepo.save(order);
        auditLogService.saveLog("Placed new order for cake: " + orderDTO.getCakeType(), customer.getId());
    }

    @Override
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        log.info("Fetching orders for customer ID: {}", customerId);
        return orderRepo.getOrdersByCustomerId(customerId);
    }
}