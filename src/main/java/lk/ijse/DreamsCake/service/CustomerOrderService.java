package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.CustomerOrderDTO;
import java.util.List;

public interface CustomerOrderService {
    void placeOrder(CustomerOrderDTO orderDTO);
    List<CustomerOrderDTO> getOrdersByCustomer(Long customerId);
}