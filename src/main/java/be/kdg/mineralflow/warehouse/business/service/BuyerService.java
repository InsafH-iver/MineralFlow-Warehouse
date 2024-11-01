package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Buyer;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class BuyerService {
    public static final Logger logger = Logger
            .getLogger(BuyerService.class.getName());
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public Buyer getBuyerById(UUID buyerId){
        Optional<Buyer> optionalBuyer = buyerRepository.findById(buyerId);
        if (optionalBuyer.isEmpty()) {
            String messageException = String.format("The Buyer '%s', was not found", buyerId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalBuyer.get();
    }
}
