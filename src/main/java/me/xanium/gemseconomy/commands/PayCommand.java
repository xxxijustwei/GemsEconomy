/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.event.GemsPayEvent;
import me.xanium.gemseconomy.file.Message;
import me.xanium.gemseconomy.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s13542415, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.getNoConsole());
            return true;
        }
        SchedulerUtils.runAsync(() -> {
            if (!sender.hasPermission("gemseconomy.command.pay")) {
                sender.sendMessage(Message.getNoPerms());
                return;
            }
            if (args.length < 2) {
                sender.sendMessage(Message.getPayUsage());
                return;
            }
            if (plugin.getCurrencyManager().getDefaultCurrency() == null) {
                sender.sendMessage(Message.getNoDefaultCurrency());
                return;
            }

            Currency currency = plugin.getCurrencyManager().getDefaultCurrency();
            if (args.length == 3) {
                currency = plugin.getCurrencyManager().getCurrency(args[2]);
            }
            if (currency != null) {
                double amount;

                if (!currency.isPayable()) {
                    sender.sendMessage(Message.getCurrencyNotPayable().replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getIdentifier()));
                    return;
                }
                if (!sender.hasPermission("gemseconomy.command.pay." + currency.getIdentifier().toLowerCase()) && !sender.hasPermission("gemseconomy.command.pay." + currency.getIdentifier().toLowerCase())) {
                    sender.sendMessage(Message.getPayNoPerms().replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getIdentifier()));
                    return;
                }
                if (currency.isDecimalSupported()) {
                    try {
                        amount = Double.parseDouble(args[1]);
                        if (amount <= 0.0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(Message.getUnvalidAmount());
                        return;
                    }
                } else {
                    try {
                        amount = Integer.parseInt(args[1]);
                        if (amount <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(Message.getUnvalidAmount());
                        return;
                    }
                }
                Account account = plugin.getAccountManager().getAccount((Player) sender);
                if (account != null) {
                    Account target = plugin.getAccountManager().getAccount(args[0]);
                    if (target != null) {
                        if (!target.getUuid().equals(account.getUuid())) {
                            if (target.canReceiveCurrency()) {
                                if (account.hasEnough(currency, amount)) {
                                    GemsPayEvent event = new GemsPayEvent(currency, account, target, amount);
                                    SchedulerUtils.run(() -> Bukkit.getPluginManager().callEvent(event));
                                    if (event.isCancelled()) return;

                                    double accBal = account.getBalance(currency) - amount;
                                    double tarBal = target.getBalance(currency) + amount;
                                    account.modifyBalance(currency, accBal, true);
                                    target.modifyBalance(currency, tarBal, true);
                                    GemsEconomy.getInstance().getEconomyLogger().log("[PAYMENT] " + account.getDisplayName() + " (New bal: " + currency.format(accBal) + ") -> paid " + target.getDisplayName() + " (New bal: " + currency.format(tarBal) + ") - An amount of " + currency.format(amount));

                                    if (Bukkit.getPlayer(target.getUuid()) != null) {
                                        Bukkit.getPlayer(target.getUuid()).sendMessage(Message.getPaidMessage().replace("{currencycolor}", currency.getColor() + "").replace("{amount}", currency.format(amount)).replace("{player}", sender.getName()));
                                    }
                                    sender.sendMessage(Message.getPayerMessage().replace("{currencycolor}", currency.getColor() + "").replace("{amount}", currency.format(amount)).replace("{player}", target.getDisplayName()));
                                } else {
                                    sender.sendMessage(Message.getInsufficientFunds().replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getIdentifier()));
                                }
                            } else {
                                sender.sendMessage(Message.getCannotReceive().replace("{player}", target.getDisplayName()));
                            }
                        } else {
                            sender.sendMessage(Message.getPayYourself());
                        }
                    } else {
                        sender.sendMessage(Message.getPlayerDoesNotExist());
                    }
                } else {
                    sender.sendMessage(Message.getAccountMissing());
                }
            } else {
                sender.sendMessage(Message.getUnknownCurrency());
            }
        });
        return true;
    }

}

