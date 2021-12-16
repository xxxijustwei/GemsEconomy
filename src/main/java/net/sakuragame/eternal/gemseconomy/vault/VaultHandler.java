/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy.vault;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.utils.UtilServer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHandler {

    private GEVaultHook economy = null;
    private final GemsEconomy plugin;

    public VaultHandler(GemsEconomy plugin){
        this.plugin = plugin;
    }

    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new GEVaultHook();
            }

            if(GemsEconomy.getCurrencyManager().getDefaultCurrency() == null){
                UtilServer.consoleLog("No Default currency found. Vault linking disabled!");
                return;
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.economy, plugin, ServicePriority.Highest);

            UtilServer.consoleLog("Vault link enabled.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if(this.economy != null){
            sm.unregister(Economy.class, this.economy);
            this.economy = null;
        }
    }

}
