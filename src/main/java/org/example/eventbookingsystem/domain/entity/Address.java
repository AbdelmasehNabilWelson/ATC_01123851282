package org.example.eventbookingsystem.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String Country;
    private String City;
    private String Street;
    private String postcode;
}
