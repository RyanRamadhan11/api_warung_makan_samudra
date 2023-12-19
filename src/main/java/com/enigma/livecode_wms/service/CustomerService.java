package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.dto.request.CustomerRequest;
import com.enigma.livecode_wms.dto.response.CustomerResponse;
import com.enigma.livecode_wms.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest customerRequest);

    CustomerResponse createNewCustomer(Customer request);

    CustomerResponse update(CustomerRequest customerRequest);

    void delete(String id);

    CustomerResponse getById(String id);

    List<CustomerResponse> getAll();
}
