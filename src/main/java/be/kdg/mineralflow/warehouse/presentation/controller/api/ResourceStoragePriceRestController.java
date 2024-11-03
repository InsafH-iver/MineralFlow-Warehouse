package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.service.resource.ResourceStoragePriceService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.ResourceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.ResourceMapper;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/resourceStoragePrice")
public class ResourceStoragePriceRestController {

    private final ResourceStoragePriceService resourceStoragePriceService;
    private final ResourceMapper resourceMapper;

    public ResourceStoragePriceRestController(ResourceStoragePriceService resourceStoragePriceService, ResourceMapper resourceMapper) {
        this.resourceStoragePriceService = resourceStoragePriceService;
        this.resourceMapper = resourceMapper;
    }

    @PutMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResourceDto> putResourceStoragePrice(@PathVariable UUID resourceId, @RequestParam @Positive double newStoragePrice){
        Resource resource = resourceStoragePriceService.changeStoragePriceOfResource(resourceId,newStoragePrice);
        return new ResponseEntity<>(resourceMapper.toDto(resource),HttpStatus.CREATED);
    }
}
