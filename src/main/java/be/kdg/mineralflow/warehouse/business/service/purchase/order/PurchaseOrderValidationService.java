package be.kdg.mineralflow.warehouse.business.service.purchase.order;

import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.OrderLineDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PartyDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrderDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PurchaseOrderValidationService {
    private final Validator validator;

    public PurchaseOrderValidationService(Validator validator) {
        this.validator = validator;
    }

    public void validate(PurchaseOrderDto purchaseOrderDto) {
        Set<ConstraintViolation<PurchaseOrderDto>> violations = validator.validate(purchaseOrderDto);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:");
            for (ConstraintViolation<PurchaseOrderDto> violation : violations) {
                errorMessage.append("\n")
                        .append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage());
            }
            throw new IllegalArgumentException(errorMessage.toString());
        }
        validateNestedObjects(purchaseOrderDto);
    }

    private void validateNestedObjects(PurchaseOrderDto purchaseOrderDto) {
        if (purchaseOrderDto.customerParty() != null) {
            Set<ConstraintViolation<@NotNull PartyDto>> customerViolations = validator.validate(purchaseOrderDto.customerParty());
            if (!customerViolations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation errors in CustomerParty:");
                for (ConstraintViolation<@NotNull PartyDto> violation : customerViolations) {
                    errorMessage.append("\n")
                            .append(violation.getPropertyPath())
                            .append(": ")
                            .append(violation.getMessage());
                }
                throw new IllegalArgumentException(errorMessage.toString());
            }
        }
        if (purchaseOrderDto.sellerParty() != null) {
            Set<ConstraintViolation<@NotNull PartyDto>> sellerViolations = validator.validate(purchaseOrderDto.sellerParty());
            if (!sellerViolations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation errors in SellerParty:");
                for (ConstraintViolation<@NotNull PartyDto> violation : sellerViolations) {
                    errorMessage.append("\n")
                            .append(violation.getPropertyPath())
                            .append(": ")
                            .append(violation.getMessage());
                }
                throw new IllegalArgumentException(errorMessage.toString());
            }
        }

        if (purchaseOrderDto.orderLines() != null) {
            for (OrderLineDto orderLine : purchaseOrderDto.orderLines()) {
                Set<ConstraintViolation<OrderLineDto>> orderLineViolations = validator.validate(orderLine);
                if (!orderLineViolations.isEmpty()) {
                    StringBuilder errorMessage = new StringBuilder("Validation errors in OrderLine:");
                    for (ConstraintViolation<OrderLineDto> violation : orderLineViolations) {
                        errorMessage.append("\n")
                                .append(violation.getPropertyPath())
                                .append(": ")
                                .append(violation.getMessage());
                    }
                    throw new IllegalArgumentException(errorMessage.toString());
                }
            }
        }
    }
}
