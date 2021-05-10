package org.sjsu.bitcornerbackend.util;

import java.math.BigDecimal;

public class BTCvalue {
    private BigDecimal btcRate;

    public BTCvalue() {
        this.btcRate = new BigDecimal(100);
    }

    public BigDecimal getBtcRate() {
        return btcRate;
    }

    public void setBtcRate(BigDecimal btcRate) {
        this.btcRate = btcRate;
    }

}
