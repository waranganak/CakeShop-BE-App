package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.CustomerOrderDTO;
import java.util.List;

public interface OrderService {
    void placeOrder(CustomerOrderDTO orderDTO);
    List<CustomerOrderDTO> getOrdersByCustomer(Long customerId);
}