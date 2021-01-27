package com.udacity.vehicles.client.prices;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Getter
@Setter
public class Price {
    private String currency;
    private BigDecimal price;
    private Long vehicleId;
}
