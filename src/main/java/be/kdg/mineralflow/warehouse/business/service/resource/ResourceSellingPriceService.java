package be.kdg.mineralflow.warehouse.business.service.resource;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResourceSellingPriceService {


    private final ResourceRepository resourceRepository;
    private final ResourceService resourceService;

    public ResourceSellingPriceService(ResourceRepository resourceRepository, ResourceService resourceService) {
        this.resourceRepository = resourceRepository;
        this.resourceService = resourceService;
    }

    public Resource changeSellingPriceOfResource(UUID resourceId, double newSellingPrice) {
        Resource resource = resourceService.getResource(resourceId);
        resource.setSellingPricePerTon(newSellingPrice);
        return resourceRepository.save(resource);
    }
}
