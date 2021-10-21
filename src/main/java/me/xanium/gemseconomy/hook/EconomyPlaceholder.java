package me.xanium.gemseconomy.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.api.GemsEconomyAPI;
import me.xanium.gemseconomy.currency.Currency;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EconomyPlaceholder extends PlaceholderExpansion {

    private GemsEconomyAPI api = new GemsEconomyAPI();

    @Override
    public @NotNull String getIdentifier() {
        return "economy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "justwei";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        if (params.startsWith("balance:")) {
            String ident = params.split(":", 2)[1];
            Currency currency = GemsEconomy.getInstance().getCurrencyManager().getCurrency(ident);
            if (currency == null) return "";

            return currency.format(api.getBalance(player.getUniqueId(), currency));
        }

        if (params.equalsIgnoreCase("balance")) {
            Currency currency = GemsEconomy.getInstance().getCurrencyManager().getDefaultCurrency();
            return currency.format(api.getBalance(player.getUniqueId()));
        }

        return "";
    }
}
