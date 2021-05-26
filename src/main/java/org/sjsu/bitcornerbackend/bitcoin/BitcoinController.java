package org.sjsu.bitcornerbackend.bitcoin;

import java.math.BigDecimal;

import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.CurrencyUnitValues;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/btc")
public class BitcoinController {
    // private BigDecimal i = new BigDecimal(1);

    @GetMapping("/{currency}")
    public BigDecimal BTCRate(@PathVariable(value = "currency") Currency currency) {
        // this.i = this.i.add(new BigDecimal((int) ((Math.random() * (10 - -10)) +
        // -10)));
        return CurrencyUnitValues.getUnitValue(currency, new BigDecimal(1));
    }

    @GetMapping("/{currency}/{units}")
    public BigDecimal BTCRateForUnits(@PathVariable(value = "currency") Currency currency,
            @PathVariable(value = "units") BigDecimal units) {
        return CurrencyUnitValues.getUnitValue(currency, units);
    }
}
