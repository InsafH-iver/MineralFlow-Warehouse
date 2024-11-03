package be.kdg.mineralflow.warehouse.business.service.resource;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Resource getResource(UUID resourceId){
        return resourceRepository.getResourceById(resourceId).
                orElseThrow(()-> ExceptionHandlingHelper.logAndThrowNotFound("Resource with id '%s' not found",resourceId));
    }
}
