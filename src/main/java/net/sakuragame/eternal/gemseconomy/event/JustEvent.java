package net.sakuragame.eternal.gemseconomy.event;

import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class JustEvent extends Event implements Cancellable {

    private final Account account;
    private final Currency currency;
    private boolean cancel;
    private static final HandlerList handlerList = new HandlerList();

    public JustEvent(Account account, Currency currency) {
        this.account = account;
        this.currency = currency;
    }

    public Account getAccount() {
        return account;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
