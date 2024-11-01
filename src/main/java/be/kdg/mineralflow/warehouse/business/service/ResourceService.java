package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class ResourceService {
    public static final Logger logger = Logger
            .getLogger(ResourceService.class.getName());
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Resource getResourceByName(String resourceName){
        Optional<Resource> optionalResource = resourceRepository.getResourceByName(resourceName);
        if (optionalResource.isEmpty()) {
            String messageException = String.format("The resource '%s', was not found", resourceName);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalResource.get();
    }
}
