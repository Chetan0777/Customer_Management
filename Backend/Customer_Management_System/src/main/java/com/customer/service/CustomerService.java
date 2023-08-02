package com.customer.service;


import java.util.List;

import com.customer.entity.Customer;
import com.customer.exception.CustomerException;
import com.customer.exception.LoginException;

public interface CustomerService {

	String authenticateUser(String loginId, String password) throws LoginException;
	void createCustomer(Customer customer, String token) throws CustomerException;
	List<Customer> getCustomerList(String token);
	boolean deleteCustomer(String token, String uuid);
	void updateCustomer(String token, String uuid, Customer customer) throws CustomerException;
}