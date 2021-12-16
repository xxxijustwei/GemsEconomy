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
import net.sakuragame.eternal.gemseconomy.commands.CurrencyCommand;
import net.sakuragame.eternal.gemseconomy.currency.CurrencyManager;
import net.sakuragame.eternal.gemseconomy.data.DataStorage;
import net.sakuragame.eternal.gemseconomy.data.MysqlHandler;
import net.sakuragame.eternal.gemseconomy.data.YamlHandler;
import net.sakuragame.eternal.gemseconomy.file.Configuration;
import net.sakuragame.eternal.gemseconomy.hook.EconomyPlaceholder;
import net.sakuragame.eternal.gemseconomy.listeners.EconomyListener;
import net.sakuragame.eternal.gemseconomy.logging.EconomyLogger;
import net.sakuragame.eternal.gemseconomy.utils.UtilServer;
import net.sakuragame.eternal.gemseconomy.vault.VaultHandler;
import net.sakuragame.eternal.gemseconomy.commands.BalanceMainCommand;
import net.sakuragame.eternal.gemseconomy.commands.EconomyCommand;
import net.sakuragame.eternal.gemseconomy.commands.PayMainCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class GemsEconomy extends JavaPlugin {

    private static GemsEconomy instance;

    private DataStorage dataStorage = null;
    private AccountManager accountManager;
    private CurrencyManager currencyManager;
    private VaultHandler vaultHandler;
    private EconomyLogger economyLogger;
    private UpdateForwarder updateForwarder;

    private boolean debug = false;
    private boolean vault = false;
    private boolean logging = false;

    private boolean disabling = false;

    @Override
    public void onLoad() {
        Configuration configuration = new Configuration(this);
        configuration.loadDefaultConfig();

        setDebug(getConfig().getBoolean("debug"));
        setVault(getConfig().getBoolean("vault"));
        setLogging(getConfig().getBoolean("transaction_log"));
    }

    @Override
    public void onEnable() {
        instance = this;
        accountManager = new AccountManager(this);
        currencyManager = new CurrencyManager(this);
        economyLogger = new EconomyLogger(this);
        updateForwarder = new UpdateForwarder(this);

        initializeDataStore(getConfig().getString("storage"), true);

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

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", updateForwarder);

        if (isLogging()) {
            getEconomyLogger().save();
        }
    }

    @Override
    public void onDisable() {
        disabling = true;

        if (isVault()) getVaultHandler().unhook();

        if (getDataStore() != null) {
            getDataStore().close();
        }
    }

    public void initializeDataStore(String strategy, boolean load) {

        DataStorage.getMethods().add(new YamlHandler(new File(getDataFolder(), "data.yml")));
        DataStorage.getMethods().add(new MysqlHandler());

        if (strategy != null) {
            dataStorage = DataStorage.getMethod(strategy);
        } else {
            UtilServer.consoleLog("§cNo valid storage method provided.");
            UtilServer.consoleLog("§cCheck your files, then try again.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

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

    public EconomyLogger getEconomyLogger() {
        return economyLogger;
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

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
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
