package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.service.resource.ResourceSellingPriceService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.ResourceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.ResourceMapper;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/resourceSellingPrice")
public class ResourceSellingPriceRestController {


    private final ResourceSellingPriceService resourceSellingPriceService;
    private final ResourceMapper resourceMapper;

    public ResourceSellingPriceRestController(ResourceSellingPriceService resourceSellingPriceService, ResourceMapper resourceMapper) {
        this.resourceSellingPriceService = resourceSellingPriceService;
        this.resourceMapper = resourceMapper;
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<ResourceDto> putResourceSellingPrice(@PathVariable UUID resourceId, @RequestParam @Positive double newSellingPrice){
        Resource resource = resourceSellingPriceService.changeSellingPriceOfResource(resourceId,newSellingPrice);
        return new ResponseEntity<>(resourceMapper.toDto(resource),HttpStatus.CREATED);
    }
}
