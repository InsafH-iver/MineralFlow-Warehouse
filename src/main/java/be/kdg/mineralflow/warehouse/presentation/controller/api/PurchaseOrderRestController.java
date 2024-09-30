package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.presentation.controller.dto.PlaceHolderDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/purchaseOrders")
public class PurchaseOrderRestController {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public List<PlaceHolderDto> createPurchaseOrders(@RequestBody @Valid List<PlaceHolderDto> placeHolderDto) {
//        return new GrondstofDto(grondstofService.addGrondstof(grondstofje.toSource()));
        return new ArrayList<PlaceHolderDto>();
    }
}
