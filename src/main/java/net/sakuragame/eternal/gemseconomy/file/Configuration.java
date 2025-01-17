/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy.file;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class Configuration {

    private final GemsEconomy plugin;

    public Configuration(GemsEconomy plugin) {
        this.plugin = plugin;
    }

    public void loadDefaultConfig() {

        FileConfiguration config = plugin.getConfig();

        config.options().header(plugin.getDescription().getName()
                + "\n"
                + "Version: " + plugin.getDescription().getVersion()
                + "\nGemsEconomy Main Configuration file."
                + "\n"
                + "Developer(s): " + plugin.getDescription().getAuthors()
                + "\n\n"
                + "You have three valid storage methods, yaml, mysql or sqlite. If you choose mysql you would have to enter the database credentials down below."
                + "\n"
                + "All messages below are configurable, I hope you use them because it took 1 hour to make all of them into the plugin and configurable.");

        String path = "Messages.";

        config.addDefault("debug", false);
        config.addDefault("vault", true);

        config.addDefault(path + "prefix", "&2&lGemsEconomy> ");
        config.addDefault(path + "nopermission", "&7You don't have permission to do this.");
        config.addDefault(path + "noconsole", "&7Console cannot do this.");
        config.addDefault(path + "invalidamount", "&7Not a valid amount.");
        config.addDefault(path + "pay_yourself", "&7You can't pay yourself.");
        config.addDefault(path + "player_is_null", "&7The specified player does not exist.");
        config.addDefault(path + "noDefaultCurrency", "&7No default currency.");
        config.addDefault(path + "currencyExists", "&7Currency already exists.");
        config.addDefault(path + "accountMissing", "&7Your account is missing. Please relog the server.");
        config.addDefault(path + "cannotReceiveMoney", "&a{player}&7 can't receive money.");
        config.addDefault(path + "insufficientFunds", "&7You don't have enough {currencycolor}{currency}&7!");
        config.addDefault(path + "targetInsufficientFunds", "&e{target} &7don't have enough {currencycolor}{currency}&7!");
        config.addDefault(path + "paid", "&7You were paid {currencycolor}{amount} &7from &a{player}&7.");
        config.addDefault(path + "payer", "&7You paid {currencycolor}{amount} &7to &a{player}&7.");
        config.addDefault(path + "payNoPermission", "&7You don't have permission to pay {currencycolor}{currency}&7.");
        config.addDefault(path + "currencyNotPayable", "{currencycolor}{currency} &7is not payable.");
        config.addDefault(path + "add", "&7You gave &a{player}&7: {currencycolor}{amount}. ");
        config.addDefault(path + "take", "&7You took {currencycolor}{amount} &7from &a{player}&7.");
        config.addDefault(path + "set", "&7You set &a{player}&7's balance to {currencycolor}{amount}&7.");

        config.addDefault(path + "balance.current", "&a{player}&7's balance is: {currencycolor}{balance}");
        config.addDefault(path + "balance.multiple", "&a{player}&7's balances:");
        config.addDefault(path + "balance.list", "&a&l>> {currencycolor}{format}");
        config.addDefault(path + "balance.none", "&7No balances to show for &c{player}&7.");

        config.addDefault(path + "help.eco_command", Arrays.asList(
                "{prefix}&e&lEconomy Help",
                "&2&l>> &a/eco give <user> <amount> [currency] &8- &7Give a player an amount of a currency.",
                "&2&l>> &a/eco take <user> <amount> [currency] &8- &7Take an amount of a currency from a player.",
                "&2&l>> &a/eco set <user> <amount> [currency] &8- &7Set a players amount of a currency."));

        config.addDefault(path + "usage.pay_command", "&2&l>> &a/pay <user> <amount> [currency] &8- &7Pay the specified user the specified amount.");
        config.addDefault(path + "usage.give_command", "&2&l>> &a/eco give <user> <amount> [currency] &8- &7Give a player an amount of a currency.");
        config.addDefault(path + "usage.take_command", "&2&l>> &a/eco take <user> <amount> [currency] &8- &7Take an amount of a currency from a player.");
        config.addDefault(path + "usage.set_command", "&2&l>> &a/eco set <user> <amount> [currency] &8- &7Set a players amount of a currency.");

        config.addDefault(path + "help.currency_command", Arrays.asList("{prefix}&e&lCurrency Help",
                "&2&l>> &a/currency create <identifier> <displayname> &8- &7Create a currency.",
                "&2&l>> &a/currency delete <identifier> &8- &7Delete a currency.",
                "&2&l>> &a/currency view <identifier> &8- &7View information about a currency.",
                "&2&l>> &a/currency list &8- &7List of currencies.",
                "&2&l>> &a/currency symbol <identifier> <char|remove> &8- &7Select a symbol for a currency or remove it.",
                "&2&l>> &a/currency color <identifier> <color> &8- &7Select a color for a currency.",
                "&2&l>> &a/currency colorlist &8- &7List of Colors.",
                "&2&l>> &a/currency decimals <identifier> &8- &7Enable decimals for a currency.",
                "&2&l>> &a/currency payable <identifier> &8- &7Set a currency payable or not.",
                "&2&l>> &a/currency default <identifier> &8- &7Set a currency as default.",
                "&2&l>> &a/currency startbal <identifier> <amount> &8- &7Set the starting balance for a currency."
        ));

        config.addDefault(path + "usage.currency_create", "&2&l>> &a/currency create <identifier> <displayname> &8- &7Create a currency.");
        config.addDefault(path + "usage.currency_delete", "&2&l>> &a/currency delete <identifier> &8- &7Delete a currency.");
        config.addDefault(path + "usage.currency_view", "&2&l>> &a/currency view <identifier> &8- &7View information about a currency.");
        config.addDefault(path + "usage.currency_symbol", "&2&l>> &a/currency symbol <identifier> <char|remove> &8- &7Select a symbol for a currency or remove it.");
        config.addDefault(path + "usage.currency_color", "&2&l>> &a/currency color <identifier> <color> &8- &7Select a color for a currency.");
        config.addDefault(path + "usage.currency_payable", "&2&l>> &a/currency payable <identifier> &8- &7Set a currency payable or not.");
        config.addDefault(path + "usage.currency_default", "&2&l>> &a/currency default <identifier> &8- &7Set a currency as default.");
        config.addDefault(path + "usage.currency_decimals", "&2&l>> &a/currency decimals <identifier> &8- &7Enable decimals for a currency.");
        config.addDefault(path + "usage.currency_startbal", "&2&l>> &a/currency startbal <identifier> <amount> &8- &7Set the starting balance for a currency.");

        config.options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
    }

}
