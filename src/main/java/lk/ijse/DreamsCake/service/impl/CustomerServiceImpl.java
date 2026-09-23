package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.CustomerDTO;
import lk.ijse.DreamsCake.dto.SignupDTO;
import lk.ijse.DreamsCake.entity.Customer;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.CustomerRepo;
import lk.ijse.DreamsCake.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public void saveCustomer(SignupDTO signupDTO) {
        log.info("Saving customer: {}", signupDTO.getName());

        if (signupDTO.getName() == null || signupDTO.getName().trim().isEmpty()) {
            throw new ApiException(400, "Customer name cannot be empty");
        }
        if (signupDTO.getPhone() == null || signupDTO.getPhone().trim().isEmpty()) {
            throw new ApiException(400, "Customer phone number cannot be empty");
        }
        if (signupDTO.getEmail() == null || signupDTO.getEmail().trim().isEmpty()) {
            throw new ApiException(400, "Customer email cannot be empty");
        }

        if (customerRepo.existsByPhone(signupDTO.getPhone())) {
            throw new ApiException(400, "A customer with this phone number already exists!");
        }
        if (customerRepo.existsByEmail(signupDTO.getEmail())) {
            throw new ApiException(400, "A customer with this email address already exists!");
        }

        Customer customer = new Customer();
        customer.setName(signupDTO.getName());
        customer.setEmail(signupDTO.getEmail());
        customer.setPhone(signupDTO.getPhone());
        customer.setAddress(signupDTO.getAddress());
        customerRepo.save(customer);

    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer with ID: {}", customerDTO.getId());

        if (customerDTO.getId() == null) {
            throw new ApiException(400, "Customer ID cannot be null for update");
        }

        Customer customer = customerRepo.findById(customerDTO.getId())
                .orElseThrow(() -> new ApiException(404, "Customer not found with ID: " + customerDTO.getId()));

        if (customerRepo.existsByPhoneAndIdNot(customerDTO.getPhone(), customerDTO.getId())) {
            throw new ApiException(400, "Another customer is already using this phone number!");
        }
        if (customerRepo.existsByEmailAndIdNot(customerDTO.getEmail(), customerDTO.getId())) {
            throw new ApiException(400, "Another customer is already using this email address!");
        }

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());

        customerRepo.save(customer);

    }

    @Override
    public CustomerDTO searchCustomer(Long id) {
        log.info("Searching customer by ID: {}", id);
        CustomerDTO customerDTO = customerRepo.searchCustomer(id);
        if (customerDTO == null) {
            throw new ApiException(404, "Customer not found with ID: " + id);
        }
        return customerDTO;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers from database");

        List<Customer> customers = customerRepo.findAll();
        return customers.stream().map(cust -> new CustomerDTO(
                cust.getId(),
                cust.getName(),
                cust.getEmail(),
                cust.getPhone(),
                cust.getAddress()
        )).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> filterCustomersByName(String name) {
        log.info("Filtering customers by name: {}", name);
        return customerRepo.filterCustomersByName(name);
    }

    @Override
    public Long getCustomerIdByUsername(String username) {
        Customer customer = customerRepo.findByName(username).orElse(null);

        if (customer != null) {
            return customer.getId();
        }
        return null;
    }
}