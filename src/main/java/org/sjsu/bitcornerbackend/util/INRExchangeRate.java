package org.sjsu.bitcornerbackend.util;

import java.math.BigDecimal;

public class INRExchangeRate {
    private static BigDecimal gbpRate = new BigDecimal(0.0097);
    private static BigDecimal usdRate = new BigDecimal(0.014);
    private static BigDecimal eurRate = new BigDecimal(0.011);

    public static BigDecimal getINRUnitValue(Currency currency, BigDecimal unit) {
        switch (currency) {
            case GBP:
                return gbpRate.multiply(unit);
            case USD:
                return usdRate.multiply(unit);
            case EUR:
                return eurRate.multiply(unit);
            default:
                return new BigDecimal(0);
        }
    }
}
