package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    void saveCustomer(CustomerDTO dto);
    void updateCustomer(CustomerDTO dto);
    void deleteCustomer(Long id);
    CustomerDTO searchCustomer(Long id);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO> filterCustomersByName(String name);
}