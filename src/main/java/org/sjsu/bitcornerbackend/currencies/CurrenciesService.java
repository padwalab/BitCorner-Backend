package org.sjsu.bitcornerbackend.currencies;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CurrenciesService implements ICurrencyService {
    @Autowired
    private CurrenciesRepository currenciesRepository;

    @Override
    public Set<Currencies> createDefaultCurrencies() {
        Set<Currencies> currencySet = new HashSet<>();

        Currencies BTC = new CurrenciesBuilder().setAmount(new BigDecimal(0)).setHold(new BigDecimal(0))
                .setCurrency(Currency.BTC).build();
        BTC = currenciesRepository.save(BTC);
        currencySet.add(BTC);

        Currencies Usd = new CurrenciesBuilder().setAmount(new BigDecimal(0)).setHold(new BigDecimal(0))
                .setCurrency(Currency.USD).build();
        Usd = currenciesRepository.save(Usd);
        currencySet.add(Usd);

        Currencies Inr = new CurrenciesBuilder().setAmount(new BigDecimal(0)).setHold(new BigDecimal(0))
                .setCurrency(Currency.INR).build();
        Inr = currenciesRepository.save(Inr);
        currencySet.add(Inr);

        Currencies Gbp = new CurrenciesBuilder().setAmount(new BigDecimal(0)).setHold(new BigDecimal(0))
                .setCurrency(Currency.GBP).build();
        Gbp = currenciesRepository.save(Gbp);
        currencySet.add(Gbp);

        return currencySet;
    }

}
