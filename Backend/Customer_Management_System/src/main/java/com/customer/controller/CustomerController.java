package com.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.customer.entity.Customer;
import com.customer.entity.Login;
import com.customer.exception.CustomerException;
import com.customer.exception.LoginException;
import com.customer.service.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody Login request) throws LoginException {
        String token = customerService.authenticateUser(request.getLoginId(), request.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
    
	@PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestHeader("Authorization") String token,
                                                 @RequestBody Customer customer) {
		try {
            customerService.createCustomer(customer, token.replace("Bearer ", ""));
            return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
        } catch (CustomerException ex) {
            return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
        }
    }
    
    @GetMapping("/get_customer_list")
    public List<Customer> getCustomerList(@RequestHeader("Authorization") String token) {
        return customerService.getCustomerList(token.replace("Bearer ", ""));
    }
    
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteCustomer(@RequestHeader("Authorization") String token, @PathVariable String uuid) {
        boolean deleted = customerService.deleteCustomer(token.replace("Bearer ", ""), uuid);
        if (deleted) {
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
    @PutMapping("/customers/{uuid}")
    public ResponseEntity<String> updateCustomer(@RequestHeader("Authorization") String token,
                                                 @PathVariable String uuid,
                                                 @RequestBody Customer customer) {
        try {
            customerService.updateCustomer(token.replace("Bearer ", ""), uuid, customer);
            return new ResponseEntity<>("Successfully Updated", HttpStatus.OK);
        } catch (CustomerException ex) {
            return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
        }
    }
}
