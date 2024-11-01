package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class VendorService {
    public static final Logger logger = Logger
            .getLogger(VendorService.class.getName());
    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }
    public Vendor getVendorById(UUID vendorId){
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isEmpty()) {
            String messageException = String.format("The Vendor '%s', was not found", vendorId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalVendor.get();
    }
}
