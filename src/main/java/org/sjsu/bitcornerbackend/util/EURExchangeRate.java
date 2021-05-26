package org.sjsu.bitcornerbackend.util;

import java.math.BigDecimal;

public class EURExchangeRate {
    private static BigDecimal gbpRate = new BigDecimal(0.87);
    private static BigDecimal usdRate = new BigDecimal(1.23);
    private static BigDecimal inrRate = new BigDecimal(89.17);

    public static BigDecimal getEURUnitValue(Currency currency, BigDecimal unit) {
        switch (currency) {
            case GBP:
                return gbpRate.multiply(unit);
            case USD:
                return usdRate.multiply(unit);
            case INR:
                return inrRate.multiply(unit);
            default:
                return new BigDecimal(0);
        }
    }
}