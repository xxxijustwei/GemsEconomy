/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy.account;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.storage.Callback;
import net.sakuragame.eternal.gemseconomy.utils.SchedulerUtils;
import net.sakuragame.eternal.gemseconomy.utils.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountManager {

    private final GemsEconomy plugin;
    private final List<Account> accounts;

    public AccountManager(GemsEconomy plugin) {
        this.plugin = plugin;
        this.accounts = new ArrayList<>();
    }
    
    public void createAccount(String nickname) {
        SchedulerUtils.runAsync(() -> {
            Account account = getAccount(nickname);

            if (account == null) {
                account = new Account(UUID.randomUUID(), nickname);
                add(account);
                account.setCanReceiveCurrency(true);

                plugin.getDataStore().saveAccount(account);
                UtilServer.consoleLog("New Account created for: " + account.getDisplayName());
            }
        });
    }

    public Account getAccount(Player player) {
        return getAccount(player.getUniqueId());
    }

    public Account getAccount(String name) {
        for (Account account : this.accounts) {
            if (account.getNickname() == null || !account.getNickname().equalsIgnoreCase(name)) continue;
            return account;
        }
        return plugin.getDataStore().loadAccount(name);
    }

    public Account getAccount(UUID uuid) {
        return getAccount(uuid, false);
    }

    public Account getAccount(UUID uuid, boolean Offline) {
        for (Account account : this.accounts) {
            if (!account.getUUID().equals(uuid)) continue;
            return account;
        }

        if (Offline) {
            if (Bukkit.isPrimaryThread()) {
                UtilServer.consoleLog("If the Offline parameter of getAccount is true, execute it in asynchronous mode!");
                return null;
            }
            return plugin.getDataStore().loadAccount(uuid);
        }

        return null;
    }

    public void removeAccount(UUID uuid){
        for(int i = 0; i < this.accounts.size(); i++){
            Account a = getAccounts().get(i);
            if(a.getUUID().equals(uuid)){
                accounts.remove(i);
                break;
            }
        }
    }

    public void add(Account account) {
        if(this.accounts.contains(account))return;

        this.accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addEconomyLogger(UUID uuid, Currency currency, double amount, String content) {
        SchedulerUtils.runAsync(() -> plugin.getDataStore().insertLogger(uuid, currency, amount, content));
    }
}

