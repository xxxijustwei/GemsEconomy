/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy;

import net.sakuragame.eternal.gemseconomy.account.AccountManager;
import net.sakuragame.eternal.gemseconomy.bungee.UpdateForwarder;
import net.sakuragame.eternal.gemseconomy.commands.BalanceMainCommand;
import net.sakuragame.eternal.gemseconomy.commands.CurrencyCommand;
import net.sakuragame.eternal.gemseconomy.commands.EconomyCommand;
import net.sakuragame.eternal.gemseconomy.commands.PayMainCommand;
import net.sakuragame.eternal.gemseconomy.currency.CurrencyManager;
import net.sakuragame.eternal.gemseconomy.file.Configuration;
import net.sakuragame.eternal.gemseconomy.hook.EconomyPlaceholder;
import net.sakuragame.eternal.gemseconomy.listeners.EconomyListener;
import net.sakuragame.eternal.gemseconomy.storage.DataStorage;
import net.sakuragame.eternal.gemseconomy.storage.MysqlHandler;
import net.sakuragame.eternal.gemseconomy.utils.UtilServer;
import net.sakuragame.eternal.gemseconomy.vault.VaultHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class GemsEconomy extends JavaPlugin {

    private static GemsEconomy instance;

    private DataStorage dataStorage = null;
    private AccountManager accountManager;
    private CurrencyManager currencyManager;
    private VaultHandler vaultHandler;
    private UpdateForwarder updateForwarder;

    private boolean debug = false;
    private boolean vault = false;

    private boolean disabling = false;

    @Override
    public void onLoad() {
        Configuration configuration = new Configuration(this);
        configuration.loadDefaultConfig();

        setDebug(getConfig().getBoolean("debug"));
        setVault(getConfig().getBoolean("vault"));
    }

    @Override
    public void onEnable() {
        instance = this;
        accountManager = new AccountManager(this);
        currencyManager = new CurrencyManager(this);
        updateForwarder = new UpdateForwarder(this);

        initializeDataStore(true);

        getServer().getPluginManager().registerEvents(new EconomyListener(), this);
        getCommand("balance").setExecutor(new BalanceMainCommand());
        getCommand("economy").setExecutor(new EconomyCommand());
        getCommand("pay").setExecutor(new PayMainCommand());
        getCommand("currency").setExecutor(new CurrencyCommand());

        new EconomyPlaceholder().register();

        if (isVault()) {
            vaultHandler = new VaultHandler(this);
            vaultHandler.hook();
        } else {
            UtilServer.consoleLog("Vault link is disabled.");
        }
    }

    @Override
    public void onDisable() {
        disabling = true;

        if (isVault()) getVaultHandler().unhook();
    }

    public void initializeDataStore(boolean load) {
        dataStorage = new MysqlHandler();

        try {
            UtilServer.consoleLog("Initializing data store \"" + getDataStore().getName() + "\"...");
            getDataStore().initialize();

            if (load) {
                UtilServer.consoleLog("Loading currencies...");
                getDataStore().loadCurrencies();
                currencyManager.initDefaultCurrency();
                UtilServer.consoleLog("Loaded " + getCurrencyManager().getCurrencies().size() + " currencies!");
            }
        } catch (Throwable e) {
            UtilServer.consoleLog("§cCannot load initial data from DataStore.");
            UtilServer.consoleLog("§cCheck your files, then try again.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public DataStorage getDataStore() {
        return dataStorage;
    }

    public static GemsEconomy getInstance() {
        return instance;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public boolean isDebug() {
        return debug;
    }

    private void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isVault() {
        return vault;
    }

    private void setVault(boolean vault) {
        this.vault = vault;
    }

    public boolean isDisabling() {
        return disabling;
    }

    public UpdateForwarder getUpdateForwarder() {
        return updateForwarder;
    }

    public static CurrencyManager getCurrencyManager() {
        return instance.currencyManager;
    }
}
