/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy.api;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import net.sakuragame.eternal.gemseconomy.storage.Callback;

import java.util.UUID;

public class GemsEconomyAPI {

    public final static GemsEconomy plugin = GemsEconomy.getInstance();
    
    public static Currency getDefaultCurrency() {
        return GemsEconomy.getCurrencyManager().getDefaultCurrency();
    }

    public static void deposit(UUID uuid, double amount){
        Account account = plugin.getAccountManager().getAccount(uuid);
        Currency def = getDefaultCurrency();

        if (account == null) {
            depositOffline(uuid, amount, def);
            return;
        }

        account.deposit(def, amount, null);
    }

    public static void deposit(UUID uuid, double amount, String loggerContent){
        Account account = plugin.getAccountManager().getAccount(uuid);
        Currency def = getDefaultCurrency();

        if (account == null) {
            depositOffline(uuid, amount, def);
            return;
        }

        account.deposit(def, amount, loggerContent);
    }

    public static void deposit(UUID uuid, double amount, EternalCurrency eCurrency) {
        deposit(uuid, amount, eCurrency, null);
    }
    
    public static void deposit(UUID uuid, double amount, EternalCurrency eCurrency, String loggerContent) {
        Currency currency = eCurrency.getCurrency();
        if (currency == null) return;

        deposit(uuid, amount, currency, loggerContent);
    }

    public static void deposit(UUID uuid, double amount, Currency currency) {
        deposit(uuid, amount, currency, null);
    }

    public static void deposit(UUID uuid, double amount, Currency currency, String loggerContent) {
        Account account = plugin.getAccountManager().getAccount(uuid);
        currency = currency == null ? getDefaultCurrency() : currency;

        if (account == null) {
            depositOffline(uuid, amount, currency);
            return;
        }

        account.deposit(currency, amount, loggerContent);
    }

    private static void depositOffline(UUID uuid, double amount, Currency currency) {
        plugin.getDataStore().loadAccount(uuid, account -> {
            if (account == null) return;
            account.deposit(currency, amount);
        });
    }

    public static void withdraw(UUID uuid, double amount){
        Account account = plugin.getAccountManager().getAccount(uuid);
        Currency def = getDefaultCurrency();
        
        if (account == null) {
            withdrawOffline(uuid, amount, def);
            return;
        }

        account.withdraw(def, amount);
    }

    public static void withdraw(UUID uuid, double amount, String loggerContent){
        Account account = plugin.getAccountManager().getAccount(uuid);
        Currency def = getDefaultCurrency();

        if (account == null) {
            withdrawOffline(uuid, amount, def);
            return;
        }

        account.withdraw(def, amount, loggerContent);
    }

    public static void withdraw(UUID uuid, double amount, EternalCurrency eCurrency) {
        withdraw(uuid, amount, eCurrency, null);
    }

    public static void withdraw(UUID uuid, double amount, EternalCurrency eCurrency, String loggerContent) {
        Currency currency = eCurrency.getCurrency();
        if (currency == null) return;

        withdraw(uuid, amount, currency, loggerContent);
    }

    public static void withdraw(UUID uuid, double amount, Currency currency){
        withdraw(uuid, amount, currency, null);
    }

    public static void withdraw(UUID uuid, double amount, Currency currency, String loggerContent){
        Account account = plugin.getAccountManager().getAccount(uuid);
        currency = currency == null ? getDefaultCurrency() : currency;

        if (account == null) {
            withdrawOffline(uuid, amount, currency);
            return;
        }

        account.withdraw(currency, amount, loggerContent);
    }

    private static void withdrawOffline(UUID uuid, double amount, Currency currency) {
        plugin.getDataStore().loadAccount(uuid, account -> {
            if (account == null) return;
            account.withdraw(currency, amount);
        });
    }

    public static double getBalance(UUID uuid){
        Account account = plugin.getAccountManager().getAccount(uuid);
        return account.getBalance(getDefaultCurrency());
    }

    public static double getBalance(UUID uuid, EternalCurrency eCurrency) {
        Currency currency = eCurrency.getCurrency();
        if (currency == null) return 0;

        return getBalance(uuid, currency);
    }

    public static double getBalance(UUID uuid, Currency currency) {
        Account account = plugin.getAccountManager().getAccount(uuid);
        currency = currency == null ? getDefaultCurrency() : currency;

        if (account == null) return 0;

        return account.getBalance(currency);
    }

    public static double getOfflineBalance(UUID uuid, Currency currency) {
        Account account = plugin.getAccountManager().getAccount(uuid, true);
        currency = currency == null ? getDefaultCurrency() : currency;

        if (account == null) return 0;

        return account.getBalance(currency);
    }

    public static Currency getCurrency(String identifier){
        if(GemsEconomy.getCurrencyManager().getCurrency(identifier) != null){
            return GemsEconomy.getCurrencyManager().getCurrency(identifier);
        }
        return null;
    }

}
