package be.kdg.mineralflow.warehouse.business.util;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Component
public class UnitConverter {
    public double convertToTonnes(double quantity, @NotBlank String uom) {
        return switch (uom.toLowerCase()) {
            case "t" ->
                    quantity;
            case "mt" ->// Megatonnes
                    quantity * 1_000_000;
            case "kt" -> // Kilotonnes
                    quantity * 1000;
            case "kg" -> // Kilograms
                    quantity / 1000;
            case "g" -> // Grams
                    quantity / 1_000_000;
            default -> // Assume quantity is already in tonnes if UOM is unknown
                    throw new IllegalArgumentException("unknown unit of measurement");
        };
    }
}
