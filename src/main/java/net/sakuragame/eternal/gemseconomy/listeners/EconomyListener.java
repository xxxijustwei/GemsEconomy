/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy.listeners;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.file.Message;
import net.sakuragame.eternal.gemseconomy.utils.SchedulerUtils;
import net.sakuragame.eternal.gemseconomy.utils.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EconomyListener implements Listener {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;

        SchedulerUtils.runAsync(() -> {
            Account account = plugin.getAccountManager().getAccount(player.getUniqueId(), true);

            if (account == null) {
                plugin.getAccountManager().createAccount(player);
                UtilServer.consoleLog("New Account created for: " + player.getName());
                return;
            }

            plugin.getAccountManager().add(account);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getAccountManager().removeAccount(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        SchedulerUtils.runLater(40L, () -> {
            if (GemsEconomy.getCurrencyManager().getDefaultCurrency() == null && (player.isOp() || player.hasPermission("gemseconomy.command.currency"))) {
                player.sendMessage(Message.getPrefix() + "§cYou have not made a currency yet. Please do so by \"§e/currency§c\".");
            }
        });
    }

}

