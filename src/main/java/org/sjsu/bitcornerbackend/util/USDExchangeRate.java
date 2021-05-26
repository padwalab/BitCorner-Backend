package org.sjsu.bitcornerbackend.util;

import java.math.BigDecimal;

public class USDExchangeRate {
    private static BigDecimal gbpRate = new BigDecimal(0.71);
    private static BigDecimal inrRate = new BigDecimal(72.78);
    private static BigDecimal eurRate = new BigDecimal(0.82);

    public static BigDecimal getUSDUnitValue(Currency currency, BigDecimal unit) {
        switch (currency) {
            case GBP:
                return gbpRate.multiply(unit);
            case INR:
                return inrRate.multiply(unit);
            case EUR:
                return eurRate.multiply(unit);
            default:
                return new BigDecimal(0);
        }
    }
}
