package com.enigma.livecode_wms.service.impl;

import com.enigma.livecode_wms.dto.request.CustomerRequest;
import com.enigma.livecode_wms.dto.response.CustomerResponse;
import com.enigma.livecode_wms.entity.Customer;
import com.enigma.livecode_wms.repository.CustomerRepository;
import com.enigma.livecode_wms.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerResponse> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .customerName(customer.getName())
                        .email(customer.getEmail())
                        .phone(customer.getMobilePhone())
                        .address(customer.getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse create(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .email(customerRequest.getEmail())
                .mobilePhone(customerRequest.getMobilePhone())
                .address(customerRequest.getAddress())
                .build();
        customerRepository.save(customer);
        return CustomerResponse.builder()
                .customerName(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getMobilePhone())
                .address(customer.getAddress())
                .build();
    }

    @Override
    public CustomerResponse createNewCustomer(Customer request) {
        Customer customer = customerRepository.saveAndFlush(request);
        return CustomerResponse.builder()
                .id(customer.getId())
                .customerName(customer.getName())
                .phone(customer.getMobilePhone())
                .build();
    }


    @Override
    public CustomerResponse getById(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            return CustomerResponse.builder()
                    .id(customer.getId())
                    .customerName(customer.getName())
                    .email(customer.getEmail())
                    .address(customer.getAddress())
                    .phone(customer.getMobilePhone())
                    .build();
        }
        return null;
    }

    @Override
    public CustomerResponse update(CustomerRequest customerRequest) {

        Optional<Customer> optionalCustomer = customerRepository.findById(customerRequest.getId());

        if (optionalCustomer.isPresent()) {
            Customer customer = Customer.builder()
                    .id(customerRequest.getId())
                    .name(customerRequest.getName())
                    .address(customerRequest.getAddress())
                    .mobilePhone(customerRequest.getMobilePhone())
                    .email(customerRequest.getEmail())
                    .build();
            customerRepository.save(customer);
            return CustomerResponse.builder()
                    .id(customer.getId())
                    .customerName(customer.getName())
                    .address(customer.getAddress())
                    .phone(customer.getMobilePhone())
                    .email(customer.getEmail())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public void delete(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customerRepository.deleteById(id);
            System.out.println("delete succed");
        } else {
            System.out.println("id not found");
        }
    }
}
