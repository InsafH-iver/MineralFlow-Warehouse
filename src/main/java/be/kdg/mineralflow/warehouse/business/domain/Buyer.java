package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String address;

    protected Buyer() {
    }

    public Buyer(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
