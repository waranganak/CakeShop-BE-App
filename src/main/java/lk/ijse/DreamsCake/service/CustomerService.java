package lk.ijse.DreamsCake.service;

import jakarta.validation.Valid;
import lk.ijse.DreamsCake.dto.CustomerDTO;
import lk.ijse.DreamsCake.dto.SignupDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO searchCustomer(Long id);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO> filterCustomersByName(String name);


    void saveCustomer(@Valid SignupDTO signupDTO);

    void updateCustomer(CustomerDTO customerDTO);
}