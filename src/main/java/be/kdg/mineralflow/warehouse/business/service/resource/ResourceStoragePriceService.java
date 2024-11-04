package be.kdg.mineralflow.warehouse.business.service.resource;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResourceStoragePriceService {


    private final ResourceRepository resourceRepository;
    private final ResourceService resourceService;

    public ResourceStoragePriceService(ResourceRepository resourceRepository, ResourceService resourceService) {
        this.resourceRepository = resourceRepository;
        this.resourceService = resourceService;
    }

    public Resource changeStoragePriceOfResource(UUID resourceId, double newStoragePrice) {
        Resource resource = resourceService.getResource(resourceId);
        resource.setStoragePricePerTonPerDay(newStoragePrice);
        return resourceRepository.save(resource);
    }
}
