package com.udacity.pricing.domain.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Getter
@Setter
@AllArgsConstructor
public class Price {
    private String currency;
    private BigDecimal price;
    private Long vehicleId;
}
