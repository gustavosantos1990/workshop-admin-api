package com.gdas.shopadminapi.services.request;

import com.gdas.shopadminapi.entities.Customer;
import com.gdas.shopadminapi.exceptions.BusinessException;
import com.gdas.shopadminapi.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CustomerBasicService {

    private final CustomerRepository customerRepository;

    public CustomerBasicService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findOptionalById(UUID customerId) {
        return customerRepository.findById(customerId);
    }

    public Optional<Customer> findByExample(Example<Customer> example) {
        return customerRepository.findOne(example);
    }

    public Customer findById(UUID customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND, "customer.id.invalid", customerId));
    }

    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

}
