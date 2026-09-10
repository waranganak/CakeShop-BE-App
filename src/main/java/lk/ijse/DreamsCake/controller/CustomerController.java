package lk.ijse.DreamsCake.controller;

import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.CustomerDTO;
import lk.ijse.DreamsCake.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@CrossOrigin

@RestController
@RequestMapping(value = "v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/search/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterCustomersByName(@PathVariable String name) {
        List<CustomerDTO> customers = customerService.filterCustomersByName(name);
        return new CommonResponse(OPERATION_SUCCESS, customers, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchCustomer(@PathVariable Long id) {
        CustomerDTO customer = customerService.searchCustomer(id);
        return new CommonResponse(OPERATION_SUCCESS, customer, SUCCESS_MESSAGE);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return new CommonResponse(OPERATION_SUCCESS, customers, SUCCESS_MESSAGE);
    }
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerDTO);
        return new CommonResponse(OPERATION_SUCCESS, "Profile Updated Successfully!", SUCCESS_MESSAGE);
    }

}