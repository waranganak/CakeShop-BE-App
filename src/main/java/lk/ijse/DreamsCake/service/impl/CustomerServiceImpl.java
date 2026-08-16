package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.CustomerDTO;
import lk.ijse.DreamsCake.entity.Customer;
import lk.ijse.DreamsCake.repository.CustomerRepo;
import lk.ijse.DreamsCake.service.AuditLogService;
import lk.ijse.DreamsCake.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final AuditLogService auditLogService; // Audit log service එක Inject කරන්න

    public CustomerServiceImpl(CustomerRepo customerRepo, AuditLogService auditLogService) {
        this.customerRepo = customerRepo;
        this.auditLogService = auditLogService;
    }

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving customer: {}", customerDTO.getName());
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customerRepo.save(customer);

        // Audit log එක පටිගත කිරීම
        auditLogService.saveLog("Created customer: " + customerDTO.getName(), 1L);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer ID: {}", customerDTO.getId());
        Optional<Customer> optionalCustomer = customerRepo.findById(customerDTO.getId());
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setName(customerDTO.getName());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhone(customerDTO.getPhone());
            customer.setAddress(customerDTO.getAddress());
            customerRepo.save(customer);

            auditLogService.saveLog("Updated customer: " + customerDTO.getName(), 1L);
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer ID: {}", id);
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            auditLogService.saveLog("Deleted customer ID: " + id, 1L);
        }
    }

    @Override
    public CustomerDTO searchCustomer(Long id) {
        return customerRepo.findById(id).map(c -> new CustomerDTO(
                c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()
        )).orElse(null);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepo.findAll().stream().map(c -> new CustomerDTO(
                c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()
        )).collect(Collectors.toList());
    }

    public List<CustomerDTO> filterCustomersByName(String name) {
        log.info("Filtering customers by name: {}", name);
        return customerRepo.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .map(c -> new CustomerDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()))
                .collect(Collectors.toList());
    }
}