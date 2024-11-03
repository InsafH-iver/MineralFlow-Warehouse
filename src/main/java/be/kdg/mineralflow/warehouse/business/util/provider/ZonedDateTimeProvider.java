package be.kdg.mineralflow.warehouse.business.util.provider;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class ZonedDateTimeProvider {
    public ZonedDateTime now() {
        return ZonedDateTime.now();
    }
}
