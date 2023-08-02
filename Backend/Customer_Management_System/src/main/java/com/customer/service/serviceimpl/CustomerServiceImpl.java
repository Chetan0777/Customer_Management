package com.customer.service.serviceimpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.customer.entity.Customer;
import com.customer.exception.CustomerException;
import com.customer.exception.LoginException;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;

import io.micrometer.common.util.StringUtils;

@Service
public class CustomerServiceImpl implements CustomerService {

	 private final String AUTH_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
	 private final String CREATE_CUSTOMER_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
	 private final String GET_CUSTOMER_LIST_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
	 private final String DELETE_CUSTOMER_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
	 private final String UPDATE_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
	 
	 @Autowired
	 RestTemplate restTemplate;
	 
	 @Autowired
	 CustomerRepository customerRepository;
	
	 @Override
	 public String authenticateUser(String loginId, String password) throws LoginException {
		 
		 HttpHeaders headers = new HttpHeaders();
	     headers.setContentType(MediaType.APPLICATION_JSON);
	     
	     Map<String, String> requestBody = new HashMap<>();
         requestBody.put("login_id", loginId);
         requestBody.put("password", password);
         
         HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
         
         try {
             ResponseEntity<String> response = restTemplate.postForEntity(AUTH_API_URL, requestEntity, String.class);
             return response.getBody();
         } catch (HttpClientErrorException ex) {
             throw new LoginException(ex.getMessage());
         }

	 }

	@Override
	public void createCustomer(Customer customer, String token) throws CustomerException {
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        if (StringUtils.isBlank(customer.getFirstName()) || StringUtils.isBlank(customer.getLastName())) {
            throw new CustomerException("First Name or Last Name is missing.", HttpStatus.BAD_REQUEST);
        }

        String requestBody = "{" +
                "\"cmd\":\"create\"," +
                "\"first_name\":\"" + customer.getFirstName() + "\"," +
                "\"last_name\":\"" + customer.getLastName() + "\"," +
                "\"street\":\"" + customer.getStreet() + "\"," +
                "\"address\":\"" + customer.getAddress() + "\"," +
                "\"city\":\"" + customer.getCity() + "\"," +
                "\"state\":\"" + customer.getState() + "\"," +
                "\"email\":\"" + customer.getEmail() + "\"," +
                "\"phone\":\"" + customer.getPhone() + "\"" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(CREATE_CUSTOMER_API_URL, entity, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                customerRepository.save(customer);
            } else {
                throw new CustomerException("Failed to create customer. Status: " + response.getStatusCode(),
                        response.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            throw new CustomerException("Failed to create customer. " + ex.getMessage(), ex.getStatusCode());
        }
  

	}

	@Override
	public List<Customer> getCustomerList(String token) {
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Customer[]> response = restTemplate.exchange(GET_CUSTOMER_LIST_API_URL + "?cmd=get_customer_list",
                    HttpMethod.GET, entity, Customer[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return Arrays.asList(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException ex) {
            return Collections.emptyList();
        }
	}

	@Override
	public boolean deleteCustomer(String token, String uuid) {
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(DELETE_CUSTOMER_API_URL + "?cmd=delete&uuid=" + uuid,
                    HttpMethod.POST, entity, String.class);

            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException ex) {
            return false;
        }
        
	}

	@Override
	public void updateCustomer(String token, String uuid, Customer customer) throws CustomerException {
        
		 HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "Bearer " + token);

	        if (StringUtils.isBlank(customer.getFirstName()) || StringUtils.isBlank(customer.getLastName())) {
	            throw new CustomerException("First Name or Last Name is missing.", HttpStatus.BAD_REQUEST);
	        }

	        String requestBody = "{" +
	                "\"cmd\":\"update\"," +
	                "\"uuid\":\"" + uuid + "\"," +
	                "\"first_name\":\"" + customer.getFirstName() + "\"," +
	                "\"last_name\":\"" + customer.getLastName() + "\"," +
	                "\"street\":\"" + customer.getStreet() + "\"," +
	                "\"address\":\"" + customer.getAddress() + "\"," +
	                "\"city\":\"" + customer.getCity() + "\"," +
	                "\"state\":\"" + customer.getState() + "\"," +
	                "\"email\":\"" + customer.getEmail() + "\"," +
	                "\"phone\":\"" + customer.getPhone() + "\"" +
	                "}";

	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

	        try {
	            ResponseEntity<String> response = restTemplate.postForEntity(UPDATE_API_URL, entity, String.class);
	            if (response.getStatusCode() == HttpStatus.OK) {
	                Customer existingCustomer = customerRepository.findById(customer.getId()).orElse(null);
	                if (existingCustomer != null) {
	                    existingCustomer.setFirstName(customer.getFirstName());
	                    existingCustomer.setLastName(customer.getLastName());
	                    existingCustomer.setStreet(customer.getStreet());
	                    existingCustomer.setAddress(customer.getAddress());
	                    existingCustomer.setCity(customer.getCity());
	                    existingCustomer.setState(customer.getState());
	                    existingCustomer.setEmail(customer.getEmail());
	                    existingCustomer.setPhone(customer.getPhone());
	                    customerRepository.save(existingCustomer);
	                }
	            } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
	                throw new CustomerException("UUID not found. Update failed.", HttpStatus.INTERNAL_SERVER_ERROR);
	            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
	                throw new CustomerException("Body is empty. Update failed.", HttpStatus.BAD_REQUEST);
	            } else {
	                throw new CustomerException("Failed to update customer. Status: " + response.getStatusCode(),
	                        response.getStatusCode());
	            }
	        } catch (HttpClientErrorException ex) {
	            throw new CustomerException("Failed to update customer. " + ex.getMessage(), ex.getStatusCode());
	        }
	}

}
