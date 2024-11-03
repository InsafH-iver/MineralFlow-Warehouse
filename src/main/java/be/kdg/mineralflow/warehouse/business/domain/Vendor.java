package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String address;

    protected Vendor() {
    }

    public Vendor(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Vendor(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
