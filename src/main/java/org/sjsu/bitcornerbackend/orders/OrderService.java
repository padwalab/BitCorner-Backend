package org.sjsu.bitcornerbackend.orders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.bankAccount.BankAccountRepository;
import org.sjsu.bitcornerbackend.currencies.Currencies;
import org.sjsu.bitcornerbackend.currencies.CurrenciesRepository;
import org.sjsu.bitcornerbackend.exceptions.orderExceptions.OrderDoesNotExist;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserRepository;
import org.sjsu.bitcornerbackend.user.UserService;
import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.CurrencyUnitValues;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.sjsu.bitcornerbackend.util.OrderVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrenciesRepository currenciesRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Orders createOrder(OrdersBuilder ordersBuilder) {
        Orders order = ordersBuilder.build();
        Orders orderResult = ordersRepository.save(order);
        return orderResult;
    }

    @Override
    public List<Orders> all() {
        List<Orders> orders = ordersRepository.findAll();
        return orders;
    }

    @Override
    public List<Orders> allBuys(Long id) throws UserNotFoundException {
        // User user = userService.findById(id);
        List<Orders> userOrders = ordersRepository.findByUserAndTypeOrderByCreatedDateDesc(id, OrderType.BUY);
        return userOrders;
    }

    @Override
    public List<Orders> findByStatus(OrderStatus status) {
        return ordersRepository.findByStatus(status);
    }

    // public void exec() {
    // new Thread(() -> {
    // System.out.println(ordersRepository.findAll());
    // }).start();
    // }
    public Orders transfer(Orders buyer, Orders seller) throws UserNotFoundException {
        if (buyer.getUnits().compareTo(seller.getUnits()) > 0) {
            BigDecimal basePrice;
            if (seller.getLimitamt().compareTo(buyer.getLimitamt()) < 0) {
                basePrice = seller.getLimitamt();
            } else {
                basePrice = buyer.getLimitamt();
            }
            // set the LTP of BTC here!!
            switch (buyer.getCurrency()) {
                case USD:
                    System.out.println("Setting the usd rate: " + basePrice.toString());
                    CurrencyUnitValues.setUsdRate(basePrice);
                    break;
                case GBP:
                    CurrencyUnitValues.setGbpRate(basePrice);
                    break;
                case EUR:
                    CurrencyUnitValues.setEurRate(basePrice);
                    break;
                case INR:
                    CurrencyUnitValues.setInrRate(basePrice);
                    break;
            }

            basePrice = basePrice.multiply(seller.getUnits());
            User buyerProfile = userService.findById(buyer.getUser());
            User sellerProfile = userService.findById(seller.getUser());
            BankAccount buyerBankAccount = buyerProfile.getBankAccount();
            BankAccount sellerBankAccount = sellerProfile.getBankAccount();
            Set<Currencies> buyerCurrencies = buyerBankAccount.getCurrencies();
            for (Currencies curr : buyerCurrencies) {
                if (curr.getCurrency() == buyer.getCurrency()) {
                    BigDecimal balance = curr.getAmount().subtract(basePrice);
                    curr.setAmount(balance);
                    curr.setHold(curr.getHold().subtract(basePrice));
                    currenciesRepository.save(curr);
                } else if (curr.getCurrency() == Currency.BTC) {
                    curr.setAmount(curr.getAmount().add(seller.getUnits()));
                    currenciesRepository.save(curr);
                }
            }
            buyerBankAccount.setCurrencies(buyerCurrencies);
            bankAccountRepository.save(buyerBankAccount);
            buyerProfile.setBankAccount(buyerBankAccount);
            userRepository.save(buyerProfile);

            Set<Currencies> sellerCurrencies = sellerBankAccount.getCurrencies();
            for (Currencies curr : sellerCurrencies) {
                if (curr.getCurrency() == buyer.getCurrency()) {
                    // here you'll have to take percentage from seller
                    BigDecimal balance = curr.getAmount().add(basePrice);
                    curr.setAmount(balance);
                    currenciesRepository.save(curr);
                } else if (curr.getCurrency() == Currency.BTC) {
                    curr.setAmount(curr.getAmount().subtract(seller.getUnits()));
                    curr.setHold(curr.getHold().subtract(seller.getUnits()));
                    currenciesRepository.save(curr);
                }
            }
            sellerBankAccount.setCurrencies(sellerCurrencies);
            bankAccountRepository.save(sellerBankAccount);
            sellerProfile.setBankAccount(sellerBankAccount);
            userRepository.save(sellerProfile);
            seller.setStatus(OrderStatus.EXECUTED);
            ordersRepository.save(seller);

            buyer.setUnits(buyer.getUnits().subtract(seller.getUnits()));
            return buyer;

        } else if (buyer.getUnits().compareTo(seller.getUnits()) < 0) {
            BigDecimal basePrice;
            if (seller.getLimitamt().compareTo(buyer.getLimitamt()) < 0) {
                basePrice = seller.getLimitamt();
            } else {
                basePrice = buyer.getLimitamt();
            }
            switch (buyer.getCurrency()) {
                case USD:
                    System.out.println("Setting the usd rate: " + basePrice.toString());
                    CurrencyUnitValues.setUsdRate(basePrice);
                    break;
                case GBP:
                    CurrencyUnitValues.setGbpRate(basePrice);
                    break;
                case EUR:
                    CurrencyUnitValues.setEurRate(basePrice);
                    break;
                case INR:
                    CurrencyUnitValues.setInrRate(basePrice);
                    break;
            }
            basePrice = basePrice.multiply(buyer.getUnits());
            User buyerProfile = userService.findById(buyer.getUser());
            User sellerProfile = userService.findById(seller.getUser());
            BankAccount buyerBankAccount = buyerProfile.getBankAccount();
            BankAccount sellerBankAccount = sellerProfile.getBankAccount();
            Set<Currencies> buyerCurrencies = buyerBankAccount.getCurrencies();
            for (Currencies curr : buyerCurrencies) {
                if (curr.getCurrency() == buyer.getCurrency()) {
                    BigDecimal balance = curr.getAmount().subtract(basePrice);
                    curr.setAmount(balance);
                    curr.setHold(curr.getHold().subtract(basePrice));
                    currenciesRepository.save(curr);
                } else if (curr.getCurrency() == Currency.BTC) {
                    curr.setAmount(curr.getAmount().add(buyer.getUnits()));
                    currenciesRepository.save(curr);
                }
            }
            buyerBankAccount.setCurrencies(buyerCurrencies);
            bankAccountRepository.save(buyerBankAccount);
            buyerProfile.setBankAccount(buyerBankAccount);
            userRepository.save(buyerProfile);

            Set<Currencies> sellerCurrencies = sellerBankAccount.getCurrencies();
            for (Currencies curr : sellerCurrencies) {
                if (curr.getCurrency() == buyer.getCurrency()) {
                    // here you'll have to take percentage from seller
                    BigDecimal balance = curr.getAmount().add(basePrice);
                    curr.setAmount(balance);
                    currenciesRepository.save(curr);
                } else if (curr.getCurrency() == Currency.BTC) {
                    curr.setAmount(curr.getAmount().subtract(buyer.getUnits()));
                    curr.setHold(curr.getHold().subtract(buyer.getUnits()));
                    currenciesRepository.save(curr);
                }
            }
            sellerBankAccount.setCurrencies(sellerCurrencies);
            bankAccountRepository.save(sellerBankAccount);
            sellerProfile.setBankAccount(sellerBankAccount);
            userRepository.save(sellerProfile);
            buyer.setStatus(OrderStatus.EXECUTED);
            ordersRepository.save(buyer);

            seller.setUnits(seller.getUnits().subtract(buyer.getUnits()));
            return seller;
        } else {
            BigDecimal basePrice;
            if (seller.getLimitamt().compareTo(buyer.getLimitamt()) < 0) {
                basePrice = seller.getLimitamt();
            } else {
                basePrice = buyer.getLimitamt();
            }
            switch (buyer.getCurrency()) {
                case USD:
                    System.out.println("Setting the usd rate: " + basePrice.toString());
                    CurrencyUnitValues.setUsdRate(basePrice);
                    break;
                case GBP:
                    CurrencyUnitValues.setGbpRate(basePrice);
                    break;
                case EUR:
                    CurrencyUnitValues.setEurRate(basePrice);
                    break;
                case INR:
                    CurrencyUnitValues.setInrRate(basePrice);
                    break;
            }
            basePrice = basePrice.multiply(seller.getUnits());
            User buyerProfile = userService.findById(buyer.getUser());
            User sellerProfile = userService.findById(seller.getUser());
            BankAccount buyerBankAccount = buyerProfile.getBankAccount();
            BankAccount sellerBankAccount = sellerProfile.getBankAccount();
            Set<Currencies> buyerCurrencies = buyerBankAccount.getCurrencies();
            for (Currencies curr : buyerCurrencies) {
                if (curr.getCurrency() == buyer.getCurrency()) {
                    BigDecimal balance = curr.getAmount().subtract(basePrice);
                    curr.setAmount(balance);
                    curr.setHold(curr.getHold().subtract(basePrice));
                    currenciesRepository.save(curr);
                } else if (curr.getCurrency() == Currency.BTC) {
                    curr.setAmount(curr.getAmount().add(seller.getUnits()));
                    currenciesRepository.save(curr);
                }
            }
            buyerBankAccount.setCurrencies(buyerCurrencies);
            bankAccountRepository.save(buyerBankAccount);
            buyerProfile.setBankAccount(buyerBankAccount);
            userRepository.save(buyerProfile);

            Set<Currencies> sellerCurrencies = sellerBankAccount.getCurrencies();
            for (Currencies curr : sellerCurrencies) {
                if (curr.getCurrency() == buyer.getCurrency()) {
                    // here you'll have to take percentage from seller
                    BigDecimal balance = curr.getAmount().add(basePrice);
                    curr.setAmount(balance);
                    currenciesRepository.save(curr);
                } else if (curr.getCurrency() == Currency.BTC) {
                    curr.setAmount(curr.getAmount().subtract(seller.getUnits()));
                    curr.setHold(curr.getHold().subtract(seller.getUnits()));
                    currenciesRepository.save(curr);
                }
            }
            sellerBankAccount.setCurrencies(sellerCurrencies);
            bankAccountRepository.save(sellerBankAccount);
            sellerProfile.setBankAccount(sellerBankAccount);
            userRepository.save(sellerProfile);
            seller.setStatus(OrderStatus.EXECUTED);
            ordersRepository.save(seller);
            buyer.setStatus(OrderStatus.EXECUTED);
            ordersRepository.save(buyer);

            return null;
        }
    }

    public void trasactLimit(List<Orders> bOrders, List<Orders> sOrders, Currency currency)
            throws UserNotFoundException {

        Collections.sort(bOrders, (a, b) -> a.getLimitamt().compareTo(b.getLimitamt()));
        System.out.print("sorted buy order - > " + bOrders.toString());
        Collections.sort(sOrders, (a, b) -> a.getLimitamt().compareTo(b.getLimitamt()));
        System.out.print("sorted sell order - > " + sOrders.toString());
        while (true) {
            if (bOrders.isEmpty() && sOrders.isEmpty()) {
                break;
            }
            Orders orderRemainder = transfer(bOrders.remove(0), sOrders.remove(0));
            if (orderRemainder == null) {
                continue;
            } else {
                if (orderRemainder.getType() == OrderType.BUY) {
                    bOrders.add(0, orderRemainder);
                } else {
                    sOrders.add(0, orderRemainder);
                }
            }
        }
        // List<Integer> bOrderIdSorted = new ArrayList<>();
    }

    public void transact(List<Orders> bOrders, List<Orders> sOrders, Currency currency) throws UserNotFoundException {
        BigDecimal btcRate = CurrencyUnitValues.getUnitValue(currency, new BigDecimal(1));
        System.out.println("transact\nborders: " + bOrders.toString() + "\nsellorders: " + sOrders.toString());

        for (Orders order : bOrders) {
            System.out.println("handing buy orders");
            User currUser = userService.findById(order.getUser());
            BigDecimal currUserDeduction = btcRate.multiply(order.getUnits());
            System.out.println(currUserDeduction);
            BankAccount currUserBankAccount = currUser.getBankAccount();
            Set<Currencies> userCurrencies = currUserBankAccount.getCurrencies();
            for (Currencies currencies : userCurrencies) {
                if (currencies.getCurrency() == currency) {
                    BigDecimal number = currencies.getAmount().subtract(currUserDeduction);
                    currencies.setAmount(number);
                    BigDecimal number2 = currencies.getHold().subtract(currUserDeduction);
                    currencies.setHold(number2);
                } else if (currencies.getCurrency() == Currency.BTC) {
                    BigDecimal number3 = currencies.getAmount().add(order.getUnits());
                    currencies.setAmount(number3);
                }
                currenciesRepository.save(currencies);
            }
            currUserBankAccount.setCurrencies(userCurrencies);
            order.setStatus(OrderStatus.EXECUTED);
            ordersRepository.save(order);
            Set<Orders> userOrders = currUserBankAccount.getOrders();
            for (Orders userOrder : userOrders) {
                if (userOrder.getId() == order.getId()) {
                    userOrder = order;
                    break;
                }
            }
            currUserBankAccount.setOrders(userOrders);
            bankAccountRepository.save(currUserBankAccount);

            currUser.setBankAccount(currUserBankAccount);
            userRepository.save(currUser);

        }
        for (Orders order : sOrders) {
            System.out.println("handing sell orders");
            User currUser = userService.findById(order.getUser());
            BigDecimal currUserAddition = btcRate.multiply(order.getUnits());
            BankAccount currUserBankAccount = currUser.getBankAccount();
            Set<Currencies> userCurrencies = currUserBankAccount.getCurrencies();
            for (Currencies currencies : userCurrencies) {
                if (currencies.getCurrency() == currency) {
                    BigDecimal number = currencies.getAmount().add(currUserAddition);
                    currencies.setAmount(number);
                } else if (currencies.getCurrency() == Currency.BTC) {
                    BigDecimal number2 = currencies.getAmount().subtract(order.getUnits());
                    currencies.setAmount(number2);
                    BigDecimal number3 = currencies.getHold().subtract(currUserAddition);
                    currencies.setHold(number3);
                }
                currenciesRepository.save(currencies);
            }
            currUserBankAccount.setCurrencies(userCurrencies);
            order.setStatus(OrderStatus.EXECUTED);
            ordersRepository.save(order);
            Set<Orders> userOrders = currUserBankAccount.getOrders();
            for (Orders userOrder : userOrders) {
                if (userOrder.getId() == order.getId()) {
                    userOrder = order;
                    break;
                }
            }
            currUserBankAccount.setOrders(userOrders);
            bankAccountRepository.save(currUserBankAccount);

            currUser.setBankAccount(currUserBankAccount);
            userRepository.save(currUser);
        }

    }

    public void execLimitOrders(Orders order) throws UserNotFoundException {
        List<Orders> executableOrders = new ArrayList<>();
        List<Orders> borders = ordersRepository.findByTypeAndStatusAndCurrencyOrderByLimitamtDescCreatedDateAsc(
                OrderType.BUY, OrderStatus.PENDING, order.getCurrency());
        System.out.println(borders);
        List<Orders> sorders = ordersRepository.findByTypeAndStatusAndCurrencyOrderByLimitamtAscCreatedDateAsc(
                OrderType.SELL, OrderStatus.PENDING, order.getCurrency());
        System.out.println(sorders);
        // findByStatusAndCurrency

        switch (order.getType()) {
            case BUY:
                BigDecimal minSellOrders = new BigDecimal(1000000000);
                if (sorders.size() > 0) {
                    for (Orders sorder : sorders) {
                        if (sorder.getLimitamt() == null) {
                            executableOrders.add(sorder);
                        } else {
                            if (sorder.getLimitamt().compareTo(borders.get(0).getLimitamt()) <= 0) {
                                executableOrders.add(sorder);
                                if (sorder.getLimitamt().compareTo(minSellOrders) < 0) {
                                    minSellOrders = sorder.getLimitamt();
                                }
                            }
                        }
                    }
                    for (Orders border : borders) {
                        if (border.getLimitamt().compareTo(minSellOrders) >= 0) {
                            executableOrders.add(border);
                        }
                    }
                }
                break;
            case SELL:
                BigDecimal maxBuyOrders = new BigDecimal(0);
                if (borders.size() > 0) {
                    for (Orders border : borders) {
                        if (border.getLimitamt().compareTo(sorders.get(0).getLimitamt()) >= 0) {
                            executableOrders.add(border);
                            if (border.getLimitamt().compareTo(maxBuyOrders) > 0) {
                                maxBuyOrders = border.getLimitamt();
                            }
                        }
                    }
                    for (Orders sorder : sorders) {
                        if (sorder.getLimitamt().compareTo(maxBuyOrders) <= 0) {
                            executableOrders.add(sorder);
                        }
                    }
                }
                break;
        }

        // executableOrders.addAll(borders);
        // executableOrders.addAll(sorders);

        execBuy(order.getCurrency(), executableOrders, OrderVariant.LIMIT);

    }

    public void execMarketOrders(Currency currency) throws UserNotFoundException {
        List<Orders> executableOrders = ordersRepository
                .findByStatusAndCurrencyOrderByCreatedDateAsc(OrderStatus.PENDING, currency);

        execBuy(currency, executableOrders, OrderVariant.MARKET);
    }

    public void execBuy(Currency currency, List<Orders> executableOrders, OrderVariant variant)
            throws UserNotFoundException {
        System.out.println("int the thread\nexecutable orders: " + executableOrders.toString());
        LinkedHashMap<Integer, BigDecimal> buyOrdersCumulative = new LinkedHashMap<Integer, BigDecimal>();
        LinkedHashMap<Integer, BigDecimal> sellOrdersCumulative = new LinkedHashMap<Integer, BigDecimal>();
        List<Orders> buyOrders = new ArrayList<>();
        List<Orders> sellOrders = new ArrayList<>();
        int i = 0;
        int j = 0;
        BigDecimal buyRunningTotal = new BigDecimal(0);
        BigDecimal sellRunningTotal = new BigDecimal(0);
        for (Orders order : executableOrders) {
            System.out.println(order);
            if (order.getType() == OrderType.BUY) {
                buyOrders.add(order);
                buyRunningTotal = buyRunningTotal.add(order.getUnits());
                buyOrdersCumulative.put(i++, buyRunningTotal);
                for (Map.Entry<Integer, BigDecimal> currentSellOrder : sellOrdersCumulative.entrySet()) {
                    System.out.println("Buy running total: " + buyRunningTotal.toString());
                    if (currentSellOrder.getValue().compareTo(buyRunningTotal) > 0) {
                        System.out.println("let check this");
                        break;
                    } else if (currentSellOrder.getValue().compareTo(buyRunningTotal) == 0) {
                        switch (variant) {
                            case MARKET:
                                transact(buyOrders, sellOrders.subList(0, currentSellOrder.getKey() + 1), currency);
                                break;
                            case LIMIT:
                                // transact(buyOrders, sellOrders.subList(0, currentSellOrder.getKey() + 1),
                                // currency);
                                trasactLimit(buyOrders, sellOrders.subList(0, currentSellOrder.getKey() + 1), currency);
                                break;
                        }

                        return;
                    }
                }
            } else if (order.getType() == OrderType.SELL) {
                sellOrders.add(order);
                sellRunningTotal = sellRunningTotal.add(order.getUnits());
                sellOrdersCumulative.put(j++, sellRunningTotal);
                for (Map.Entry<Integer, BigDecimal> currentBuyOrder : buyOrdersCumulative.entrySet()) {
                    System.out.println("Sell running total: " + sellRunningTotal.toString());
                    if (currentBuyOrder.getValue().compareTo(sellRunningTotal) > 0) {
                        System.out.println("let check this");
                        break;
                    } else if (currentBuyOrder.getValue().compareTo(sellRunningTotal) == 0) {
                        switch (variant) {
                            case MARKET:
                                transact(buyOrders.subList(0, currentBuyOrder.getKey() + 1), sellOrders, currency);
                                break;
                            case LIMIT:
                                // transact(buyOrders.subList(0, currentBuyOrder.getKey() + 1), sellOrders,
                                // currency);
                                trasactLimit(buyOrders.subList(0, currentBuyOrder.getKey() + 1), sellOrders, currency);
                                break;
                        }
                        return;
                    }
                }

            }
        }
    }

    @Override
    public List<Orders> findByStatus(OrderStatus status, OrderType type) {
        List<Orders> bidOrders = ordersRepository.findByStatusAndTypeOrderByCreatedDateAsc(status, type);
        return bidOrders;
    }

    @Override
    public List<Orders> findByStatus(OrderType type, Currency currency) {
        List<Orders> Orders = ordersRepository.findByTypeAndStatusAndCurrencyOrderByLimitamtDescCreatedDateAsc(type,
                OrderStatus.PENDING, currency);
        return Orders;
    }

    @Override
    public Set<Orders> all(Long id) throws UserNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Orders> allSells(Long id) throws UserNotFoundException {
        // TODO Auto-generated method stub
        List<Orders> userOrders = ordersRepository.findByUserAndTypeOrderByCreatedDateDesc(id, OrderType.SELL);
        return userOrders;
    }

    @Override
    public Orders findById(Long orderId) throws OrderDoesNotExist {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new OrderDoesNotExist("No such Orders"));
        return order;
    }

}
