package net.sakuragame.eternal.gemseconomy.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EconomyPlaceholder extends PlaceholderExpansion {

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
            Currency currency = GemsEconomy.getCurrencyManager().getCurrency(ident);
            if (currency == null) return "";

            return currency.format(GemsEconomyAPI.getBalance(player.getUniqueId(), currency));
        }

        if (params.equalsIgnoreCase("balance")) {
            Currency currency = GemsEconomy.getCurrencyManager().getDefaultCurrency();
            return currency.format(GemsEconomyAPI.getBalance(player.getUniqueId()));
        }

        return "";
    }
}
