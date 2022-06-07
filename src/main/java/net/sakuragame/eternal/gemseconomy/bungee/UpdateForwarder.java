package net.sakuragame.eternal.gemseconomy.bungee;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.utils.SchedulerUtils;
import net.sakuragame.eternal.gemseconomy.utils.UtilServer;
import net.sakuragame.serversystems.manage.api.redis.RedisMessageListener;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;

import java.util.UUID;

public class UpdateForwarder extends RedisMessageListener {

    private final GemsEconomy plugin;
    private final String CURRENCY_UPDATE_CHANNEL = "economy_currency_update";

    public UpdateForwarder(GemsEconomy plugin) {
        this.plugin = plugin;
        ClientManagerAPI.getRedisManager().subscribe(plugin.getName());
        ClientManagerAPI.getRedisManager().registerListener(this);
        UtilServer.consoleLog("Redis channel registered successfully!");
    }

    @Override
    public void onMessage(String serviceName, String sourceServer, String channel, String[] messages) {
        if (sourceServer.equals(ClientManagerAPI.getServerID())) return;
        if (!serviceName.equals(plugin.getName())) return;

        if (channel.equals(CURRENCY_UPDATE_CHANNEL)) {
            UUID cid = UUID.fromString(messages[0]);
            Currency currency = GemsEconomy.getCurrencyManager().getCurrency(cid);
            SchedulerUtils.runAsync(() -> plugin.getDataStore().updateCurrencyLocally(currency));
            if(GemsEconomy.getInstance().isDebug()){
                UtilServer.consoleLog(channel + " - Currency " + currency.getIdentifier() + " updated.");
            }
        }
    }

    public void updateCurrency(UUID cid) {
        ClientManagerAPI.getRedisManager().publishAsync(plugin.getName(), CURRENCY_UPDATE_CHANNEL, cid.toString());
    }

}
