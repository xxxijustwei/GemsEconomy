package net.sakuragame.eternal.gemseconomy.currency;

import com.google.common.collect.Lists;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;

import java.util.List;
import java.util.UUID;

public class CurrencyManager {

    private final GemsEconomy plugin;

    public CurrencyManager(GemsEconomy plugin) {
        this.plugin = plugin;
    }

    private final List<Currency> currencies = Lists.newArrayList();

    public void initDefaultCurrency() {
        for (EternalCurrency currency : EternalCurrency.values()) {
            currency.create();
        }
    }

    public boolean currencyExist(String identifier) {
        for(Currency currency : currencies) {
            if(currency.getIdentifier().equals(identifier)){
                return true;
            }
        }
        return false;
    }

    public Currency getCurrency(String identifier) {
        for(Currency currency : currencies) {
            if(currency.getIdentifier().equals(identifier)){
                return currency;
            }
        }
        return null;
    }

    public Currency getCurrency(UUID uuid) {
        for (Currency currency : getCurrencies()) {
            if (!currency.getUUID().equals(uuid)) continue;
            return currency;
        }
        return null;
    }

    public Currency getDefaultCurrency() {
        for (Currency currency : currencies) {
            if (!currency.isDefaultCurrency()) continue;
            return currency;
        }
        return null;
    }

    public void createNewCurrency(String identifier, String displayName){
        createNewCurrency(identifier, displayName, false);
    }

    public void createNewCurrency(String identifier, String displayName, boolean def){
        if(currencyExist(identifier)) {
            return;
        }

        Currency currency = new Currency(UUID.randomUUID(), identifier, displayName);
        if(currencies.size() == 0 || def) {
            currency.setDefaultCurrency(true);
        }

        add(currency);

        plugin.getDataStore().saveCurrency(currency);
    }

    public void deleteCurrency(Currency currency) {
        plugin.getDataStore().deleteCurrency(currency);
    }

    public void add(Currency currency) {
        if(currencies.contains(currency))return;

        currencies.add(currency);
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
