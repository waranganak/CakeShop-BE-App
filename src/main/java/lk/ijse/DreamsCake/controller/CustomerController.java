package lk.ijse.DreamsCake.controller;

import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.CustomerDTO;
import lk.ijse.DreamsCake.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping(value = "v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);

    }
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }
    @GetMapping(value = "/search/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerDTO> filterCustomersByName(@PathVariable String name) {
        return customerService.filterCustomersByName(name);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDTO searchCustomer(@PathVariable Long id) {
        return customerService.searchCustomer(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}