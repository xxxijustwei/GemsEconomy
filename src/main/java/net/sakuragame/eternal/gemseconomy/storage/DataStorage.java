/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy.storage;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class DataStorage {

    public final GemsEconomy plugin = GemsEconomy.getInstance();

    private final String name;

    public DataStorage(String name) {
        this.name = name;
    }

    public abstract void initialize();

    public abstract Map<Integer, Double> getAllBalance(EternalCurrency currency);

    public abstract void loadCurrencies();

    public abstract void updateCurrencyLocally(Currency currency);

    public abstract void saveCurrency(Currency currency);

    public abstract void deleteCurrency(Currency currency);

    public abstract Account loadAccount(String name);

    public abstract Account loadAccount(UUID uuid);

    public abstract void loadAccount(UUID uuid, Callback<Account> callback);

    public abstract void loadAccount(String name, Callback<Account> callback);

    public abstract void saveAccount(Account account);

    public abstract void deleteAccount(Account account);

    public abstract void updateBalance(UUID uuid, Currency currency, double amount);

    public abstract void createAccount(Account account);

    public abstract void addAccountCurrencies(UUID uuid, String name, List<Currency> currencies);

    public abstract void insertLogger(UUID uuid, Currency currency, double amount, String content);

    public abstract void clearLogger(int day);

    public String getName() {
        return this.name;
    }
}

