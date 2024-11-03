package be.kdg.mineralflow.warehouse.business.domain;

import be.kdg.mineralflow.warehouse.exception.IncorrectDomainException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.logging.Logger;

@Entity
public class StockPortion {
    public static final Logger logger = Logger
            .getLogger(StockPortion.class.getName());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private ZonedDateTime arrivalTime;
    private double amountInTon;
    private double amountLeftInTon;
    private double storageCostPerTonPerDay;

    protected StockPortion() {
    }

    public StockPortion(double amountInTon, ZonedDateTime deliveryTime, double storageCostPerTonPerDay) {
        this.amountInTon = amountInTon;
        this.amountLeftInTon = amountInTon;
        this.arrivalTime = deliveryTime;
        this.storageCostPerTonPerDay = storageCostPerTonPerDay;
    }

    public double getAmountInTon() {
        return amountInTon;
    }

    public double getAmountLeftInTon() {
        return amountLeftInTon;
    }

    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }
    public long getDaysBetween(LocalDateTime dateTime) {
        return ChronoUnit.DAYS.between(LocalDateTime.from(arrivalTime),dateTime);
    }

    public double getStorageCostPerTonPerDay() {
        return storageCostPerTonPerDay;
    }

    public boolean isPortionEmpty() {
        return amountLeftInTon == 0;
    }

    public double takeAmountInTon(double amountToTakeOutOfStock) {
        checkValidityOfValue(amountToTakeOutOfStock);

        if (amountToTakeOutOfStock > this.amountLeftInTon) { // when there is more to take than there is in this stock portion
            double amountTakenOutOfStock = this.amountLeftInTon;
            this.amountLeftInTon = 0;
            return amountTakenOutOfStock;
        }
        { // when there is less or the same as what's left in this stock portion
            this.amountLeftInTon -= amountToTakeOutOfStock;
            return amountToTakeOutOfStock;
        }
    }

    private void checkValidityOfValue(double amountToTakeOutOfStock) {
        if (amountToTakeOutOfStock < 0) {
            String messageException = String.format("The provided amount to be taken out of Stock in ton (%f) is invalid," +
                    " the value must be a positive number", amountToTakeOutOfStock);
            logger.severe(messageException);
            throw new IncorrectDomainException(messageException);
        }
    }
}
