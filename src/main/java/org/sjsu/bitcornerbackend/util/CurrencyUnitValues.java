package org.sjsu.bitcornerbackend.util;

import java.math.BigDecimal;

public class CurrencyUnitValues {

    private static BigDecimal gbpRate = new BigDecimal(41414.02);
    private static BigDecimal inrRate = new BigDecimal(4278147.27);
    private static BigDecimal eurRate = new BigDecimal(47951.21);
    private static BigDecimal usdRate = new BigDecimal(50000.00);

    public static BigDecimal getGbpRate() {
        return gbpRate;
    }

    public static void setGbpRate(BigDecimal gbpRate) {
        CurrencyUnitValues.gbpRate = gbpRate;
    }

    public static BigDecimal getInrRate() {
        return inrRate;
    }

    public static void setInrRate(BigDecimal inrRate) {
        CurrencyUnitValues.inrRate = inrRate;
    }

    public static BigDecimal getEurRate() {
        return eurRate;
    }

    public static void setEurRate(BigDecimal eurRate) {
        CurrencyUnitValues.eurRate = eurRate;
    }

    public static BigDecimal getUsdRate() {
        return usdRate;
    }

    public static void setUsdRate(BigDecimal usdRate) {
        System.out.println("Setting USD rate");
        CurrencyUnitValues.usdRate = usdRate;
    }

    public static BigDecimal getUnitValue(Currency currency, BigDecimal unit) {
        switch (currency) {
            case GBP:
                return CurrencyUnitValues.gbpRate.multiply(unit);
            case INR:
                return CurrencyUnitValues.inrRate.multiply(unit);
            case EUR:
                return CurrencyUnitValues.eurRate.multiply(unit);
            case USD:
                return CurrencyUnitValues.usdRate.multiply(unit);
            default:
                return new BigDecimal(0);
        }
    }
}