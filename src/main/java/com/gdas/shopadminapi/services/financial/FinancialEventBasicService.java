package com.gdas.shopadminapi.services.financial;

import com.gdas.shopadminapi.entities.FinancialEvent;
import com.gdas.shopadminapi.repositories.FinancialEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialEventBasicService {

    private final FinancialEventRepository financialEventRepository;

    public FinancialEventBasicService(FinancialEventRepository financialEventRepository) {
        this.financialEventRepository = financialEventRepository;
    }

    public List<FinancialEvent> findByRequest(Long requestId) {
        return financialEventRepository.findByRequestId(requestId);
    }

    public void deleteMany(List<FinancialEvent> events) {
        financialEventRepository.deleteAll(events);
    }
}
