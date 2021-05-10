package org.sjsu.bitcornerbackend.util;

import java.math.BigDecimal;

public class CurrencyUnitValues {

    private static BigDecimal gbpRate = new BigDecimal(41414.02);
    private static BigDecimal inrRate = new BigDecimal(4278147.27);
    private static BigDecimal eurRate = new BigDecimal(47951.21);
    private static BigDecimal usdRate = new BigDecimal(58283.40);

    public static BigDecimal getUnitValue(Currency currency, int unit) {
        switch (currency) {
            case GBP:
                return gbpRate.multiply(new BigDecimal(unit));
            case INR:
                return inrRate.multiply(new BigDecimal(unit));
            case EUR:
                return eurRate.multiply(new BigDecimal(unit));
            case USD:
                return usdRate.multiply(new BigDecimal(unit));
            default:
                return new BigDecimal(0);
        }
    }
}
