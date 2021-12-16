package me.xanium.gemseconomy.currency;


import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.api.GemsEconomyAPI;

public enum EternalCurrency {

    Money("money", "金币"),
    Coins("coins", "点劵"),
    Points("points", "神石");

    private final String identifier;
    private final String display;

    EternalCurrency(String identifier, String display) {
        this.identifier = identifier;
        this.display = display;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDisplay() {
        return display;
    }

    public Currency getCurrency() {
        return GemsEconomy.getCurrencyManager().getCurrency(getIdentifier());
    }

    public void create() {
        Currency currency = GemsEconomyAPI.getCurrency(getIdentifier());
        if (currency != null) return;

        GemsEconomy.getCurrencyManager().createNewCurrency(getIdentifier(), getDisplay(), this == Money);
    }
}
