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

import java.util.UUID;

public class GemsEconomyAPI {

    public final static GemsEconomy plugin = GemsEconomy.getInstance();

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    public static void deposit(UUID uuid, double amount){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        acc.deposit(GemsEconomy.getCurrencyManager().getDefaultCurrency(), amount);
    }


    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency, if the currency is null, the default will be used.
     * @param eCurrency - A eternal-land currency.
     */
    public static void deposit(UUID uuid, double amount, EternalCurrency eCurrency) {
        Currency currency = eCurrency.getCurrency();
        if (currency == null) return;

        deposit(uuid, amount, currency);
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of a currency, if the currency is null, the default will be used.
     * @param currency - A specified currency.
     */
    public static void deposit(UUID uuid, double amount, Currency currency){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        if(currency != null) {
            acc.deposit(currency, amount);
        }else{
            acc.deposit(GemsEconomy.getCurrencyManager().getDefaultCurrency(), amount);
        }
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the default currency.
     */
    public static void withdraw(UUID uuid, double amount){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        acc.withdraw(GemsEconomy.getCurrencyManager().getDefaultCurrency(), amount);
    }

    public static void withdraw(UUID uuid, double amount, EternalCurrency eCurrency) {
        Currency currency = eCurrency.getCurrency();
        if (currency == null) return;

        withdraw(uuid, amount, currency);
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param amount - An amount of the currency.
     * @param currency - The currency you withdraw from.
     */
    public static void withdraw(UUID uuid, double amount, Currency currency){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        if(currency != null) {
            acc.withdraw(currency, amount);
        }else{
            acc.withdraw(GemsEconomy.getCurrencyManager().getDefaultCurrency(), amount);
        }
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @return - The default currency balance of the user.
     */
    public static double getBalance(UUID uuid){
        Account acc = plugin.getAccountManager().getAccount(uuid);
        return acc.getBalance(GemsEconomy.getCurrencyManager().getDefaultCurrency());
    }

    public static double getBalance(UUID uuid, EternalCurrency eCurrency) {
        Currency currency = eCurrency.getCurrency();
        if (currency == null) return 0;

        return getBalance(uuid, currency);
    }

    /**
     *
     * @param uuid - The users unique ID.
     * @param currency - An amount of the default currency.
     * @return - The balance of the specified currency.
     */
    public static double getBalance(UUID uuid, Currency currency) {
        Account acc = plugin.getAccountManager().getAccount(uuid);
        if (currency != null) {
            return acc.getBalance(currency);
        }else{
            return acc.getBalance(GemsEconomy.getCurrencyManager().getDefaultCurrency());
        }
    }

    /**
     *
     * @param identifier - Currency identifier.
     * @return - Currency Object.
     */
    public static Currency getCurrency(String identifier){
        if(GemsEconomy.getCurrencyManager().getCurrency(identifier) != null){
            return GemsEconomy.getCurrencyManager().getCurrency(identifier);
        }
        return null;
    }

}
