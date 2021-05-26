package org.sjsu.bitcornerbackend.util;

import java.math.BigDecimal;

public class GBPExchangeRate {
    private static BigDecimal inrRate = new BigDecimal(103.02);
    private static BigDecimal usdRate = new BigDecimal(1.42);
    private static BigDecimal eurRate = new BigDecimal(1.16);

    public static BigDecimal getGBPUnitValue(Currency currency, BigDecimal unit) {
        switch (currency) {
            case INR:
                return inrRate.multiply(unit);
            case USD:
                return usdRate.multiply(unit);
            case EUR:
                return eurRate.multiply(unit);
            default:
                return new BigDecimal(0);
        }
    }
}
