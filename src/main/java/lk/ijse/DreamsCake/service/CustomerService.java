package lk.ijse.DreamsCake.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lk.ijse.DreamsCake.dto.CustomerDTO;
import lk.ijse.DreamsCake.dto.SignupDTO;

import java.util.List;
public interface CustomerService {
    void saveCustomer(SignupDTO signupDTO);
    void updateCustomer(CustomerDTO customerDTO);
    CustomerDTO searchCustomer(Long id);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO> filterCustomersByName(String name);

    Long getCustomerIdByUsername(String username);
}