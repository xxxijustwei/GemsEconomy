package net.sakuragame.eternal.gemseconomy.currency;


import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;

import java.util.UUID;

public enum EternalCurrency {

    Money("money", "Γ", "金币", true),
    Coins("coins", "☪", "点劵", false),
    Points("points", "㊉", "神石", false),
    Fish("fish", "ω", "钓鱼点数", false);

    private final String identifier;
    private final String symbol;
    private final String display;
    private final boolean isDecimals;

    EternalCurrency(String identifier, String symbol, String display, boolean isDecimals) {
        this.identifier = identifier;
        this.symbol = symbol;
        this.display = display;
        this.isDecimals = isDecimals;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplay() {
        return display;
    }

    public boolean isDecimals() {
        return isDecimals;
    }

    public Currency getCurrency() {
        return GemsEconomy.getCurrencyManager().getCurrency(getIdentifier());
    }

    public void create() {
        Currency currency = GemsEconomyAPI.getCurrency(getIdentifier());
        if (currency != null) return;

        currency = new Currency(UUID.randomUUID(), identifier, display);
        currency.setSymbol(getSymbol());
        currency.setDecimalSupported(isDecimals);

        GemsEconomy.getCurrencyManager().createNewCurrency(currency, this == Money);
    }
}
