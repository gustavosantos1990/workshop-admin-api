package com.gdas.shopadminapi.services.request;

import com.gdas.shopadminapi.entities.Customer;
import com.gdas.shopadminapi.entities.Request;
import com.gdas.shopadminapi.enums.RequestStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Transactional
public class RequestCreatorService {
    
    private final RequestBasicService requestBasicService;
    private final CustomerBasicService customerBasicService;

    public RequestCreatorService(RequestBasicService requestSimpleService, CustomerBasicService customerBasicService) {
        this.requestBasicService = requestSimpleService;
        this.customerBasicService = customerBasicService;
    }
    
    public Request create(Request request) {
        Customer customer = loadSaveCustomer(request.getCustomer());
        request.setCustomer(customer);
        request.setStatus(RequestStatus.ESTIMATE);
        return requestBasicService.save(request);
    }

    private Customer loadSaveCustomer(Customer customer) {
        Optional<Customer> optionalCustomer = Optional.empty();

        if(customer.getId() != null) {
            optionalCustomer = customerBasicService.findOptionalById(customer.getId());
        }

        if (optionalCustomer.isEmpty()) {
            Example<Customer> example = exampleFromPhone(customer.getPhone());
            optionalCustomer = customerBasicService.findByExample(example);
        }

        if (optionalCustomer.isEmpty()) {
            return customerBasicService.save(customer);
        }

        Customer existingCustomer = optionalCustomer.get();
        copyProperties(customer, existingCustomer, "id", "createdAt");

        return customerBasicService.save(existingCustomer);
    }

    private Example<Customer> exampleFromPhone(String phone) {
        Customer customer = new Customer();
        customer.setPhone(phone);
        return Example.of(customer);
    }

}
