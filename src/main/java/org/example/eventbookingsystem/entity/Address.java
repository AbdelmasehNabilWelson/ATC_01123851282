package org.example.eventbookingsystem.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String Country;
    private String City;
    private String Street;
    private Long postcode;
}
