package com.gdas.shopadminapi.services.request;

import com.gdas.shopadminapi.entities.FinancialEvent;
import com.gdas.shopadminapi.entities.Request;
import com.gdas.shopadminapi.enums.RequestStatus;
import com.gdas.shopadminapi.exceptions.BusinessException;
import com.gdas.shopadminapi.services.financial.FinancialEventBasicService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.gdas.shopadminapi.enums.Event.*;
import static com.gdas.shopadminapi.enums.RequestStatus.*;
import static com.gdas.shopadminapi.utils.DateTimeUtils.currentLocalDateTime;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Service
@Transactional
public class RequestStatusUpdaterService {

    Logger logger = LoggerFactory.getLogger(RequestStatusUpdaterService.class);

    private static final Map<RequestStatus, List<RequestStatus>> UPDATE_OPERATIONS = Map.of(
            ESTIMATE, List.of(ACTIVE, CANCELED),
            ACTIVE, List.of(DELIVERED, CANCELED),
            DELIVERED, List.of(UNDER_BILLING),
            UNDER_BILLING, List.of(UNDER_BILLING, BILLING_REIMBURSEMENTS),
            BILLING_REIMBURSEMENTS, List.of(BILLING_REIMBURSEMENTS, UNDER_BILLING, BILLING_RESULTS),
            BILLING_RESULTS, List.of(BILLING_RESULTS, UNDER_BILLING, BILLING_REIMBURSEMENTS, BILLED),
            BILLED, List.of(DONE)
    );

    private static final BiConsumer<Request, Request> CURRENT_STATUS_CONSTRAINT = (existingRequest, request) -> {
        if (DONE.equals(existingRequest.getStatus()) || CANCELED.equals(existingRequest.getStatus())) {
            throw new BusinessException(PRECONDITION_FAILED, "request.status.non-updatable", existingRequest.getStatus());
        }
    };

    private static final BiConsumer<Request, Request> NEXT_STATUS_CONSTRAINT = (existingRequest, request) -> {
        if(!UPDATE_OPERATIONS.get(existingRequest.getStatus()).contains(request.getStatus())) {
            throw new BusinessException(PRECONDITION_FAILED, "request.status.update.condition",
                    existingRequest.getStatus().getLabel(), request.getStatus().getLabel(),
                    UPDATE_OPERATIONS.get(existingRequest.getStatus()).stream().map(RequestStatus::getLabel).collect(Collectors.toList()));
        }
    };

    private static final BiConsumer<Request, Request> PRODUCTS_AMOUNT_CONSTRAINT = (existingRequest, request) -> {
        if (existingRequest.getRequestProducts() == null || existingRequest.getRequestProducts().isEmpty()) {
            throw new BusinessException(PRECONDITION_FAILED, "request.products.quantity");
        }
    };

    private static final List<BiConsumer<Request, Request>> BASIC_CONSTRAINTS =
            List.of(CURRENT_STATUS_CONSTRAINT, NEXT_STATUS_CONSTRAINT);
    
    private static final Map<RequestStatus, List<BiConsumer<Request, Request>>> STATUS_CONSTRAINTS = Map.of(
            ESTIMATE, List.of(CURRENT_STATUS_CONSTRAINT, NEXT_STATUS_CONSTRAINT, PRODUCTS_AMOUNT_CONSTRAINT),
            ACTIVE, BASIC_CONSTRAINTS,
            DELIVERED, BASIC_CONSTRAINTS,
            UNDER_BILLING, BASIC_CONSTRAINTS,
            BILLING_REIMBURSEMENTS, BASIC_CONSTRAINTS,
            BILLING_RESULTS, BASIC_CONSTRAINTS,
            BILLED, BASIC_CONSTRAINTS
    );

    private final RequestBasicService requestService;
    private final FinancialEventBasicService financialService;

    public RequestStatusUpdaterService(RequestBasicService requestService, FinancialEventBasicService financialService) {
        this.requestService = requestService;
        this.financialService = financialService;
    }

    public Request updateStatus(@NotNull Request request) {
        try {
            Request existingRequest = requestService.findById(request.getId());

            STATUS_CONSTRAINTS
                    .getOrDefault(request.getStatus(), emptyList())
                    .forEach(constraint -> constraint.accept(existingRequest, request));

            switch (existingRequest.getStatus()) {
                case ESTIMATE, ACTIVE, DELIVERED, BILLED -> {}
                case UNDER_BILLING -> updateFromUnderBilling(existingRequest, request);
                case BILLING_REIMBURSEMENTS -> updateFromBillingReimbursements(existingRequest, request);
                case BILLING_RESULTS -> updateFromBillingResults(existingRequest, request);
                default -> throw new BusinessException(NOT_IMPLEMENTED, "operation.not-implemented");
            }

            existingRequest.setStatus(request.getStatus());

            if(CANCELED.equals(request.getStatus()))
                existingRequest.setCanceledAt(currentLocalDateTime());

            return requestService.save(existingRequest);
        } catch (Throwable t) {
            logger.error("error on request status update: {}", request);
            logger.error(t.getMessage(), t);
            throw t;
        }
    }

    private void updateFromUnderBilling(Request existingRequest, Request request) {
        if (UNDER_BILLING.equals(request.getStatus())) {
            resetEventsToUnderBillingStatus(request);
        }
    }

    private void updateFromBillingReimbursements(Request existingRequest, Request request) {
        if (UNDER_BILLING.equals(request.getStatus())) {
            resetEventsToUnderBillingStatus(request);
        }

        if (BILLING_REIMBURSEMENTS.equals(request.getStatus())) {
            resetEventsToBillingReimbursementsStatus(request);
        }
    }

    private void updateFromBillingResults(Request existingRequest, Request request) {
        if (UNDER_BILLING.equals(request.getStatus())) {
            resetEventsToUnderBillingStatus(request);
        }

        if (BILLING_REIMBURSEMENTS.equals(request.getStatus())) {
            resetEventsToBillingReimbursementsStatus(request);
        }

        if (BILLING_RESULTS.equals(request.getStatus())) {
            resetEventsToBillingResultsStatus(request);
        }
    }

    private void resetEventsToBillingResultsStatus(Request request) {
        List<FinancialEvent> events = financialService.findByRequest(request.getId());
        events.removeIf(e -> List.of(
                ADVANCE_OR_PAYMENT_OF_REQUEST,
                PICKUP_OR_DELIVERY_OF_REQUEST,
                PRODUCTION_COST,
                EXTRA_EXPENSES_OF_REQUEST,
                DELIVERY_REIMBURSEMENT,
                COST_REIMBURSEMENT,
                EXTRA_EXPENSES_REIMBURSEMENT).contains(e.getEvent()));
        financialService.deleteMany(events);
    }

    private void resetEventsToUnderBillingStatus(Request request) {
        List<FinancialEvent> events = financialService.findByRequest(request.getId());
        events.removeIf(e -> List.of(
                ADVANCE_OR_PAYMENT_OF_REQUEST,
                PICKUP_OR_DELIVERY_OF_REQUEST).contains(e.getEvent()));
        financialService.deleteMany(events);
    }

    private void resetEventsToBillingReimbursementsStatus(Request request) {
        List<FinancialEvent> events = financialService.findByRequest(request.getId());
        events.removeIf(e -> List.of(
                ADVANCE_OR_PAYMENT_OF_REQUEST,
                PICKUP_OR_DELIVERY_OF_REQUEST,
                PRODUCTION_COST,
                EXTRA_EXPENSES_OF_REQUEST).contains(e.getEvent()));
        financialService.deleteMany(events);
    }

}
