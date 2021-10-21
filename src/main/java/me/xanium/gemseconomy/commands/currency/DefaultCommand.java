package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.PermissionType;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class DefaultCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "default";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 2) {
            commandSender.sendMessage(Message.getCurrencyUsage_Default());
            return;
        }

        String s = strings[1];
        Currency currency = plugin.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            commandSender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        Currency current = plugin.getCurrencyManager().getDefaultCurrency();
        if (current != null) {
            current.setDefaultCurrency(false);
            plugin.getDataStore().saveCurrency(current);
        }

        currency.setDefaultCurrency(true);
        commandSender.sendMessage(Message.getPrefix() + "§7Set default currency to §f" + currency.getIdentifier());
        plugin.getDataStore().saveCurrency(currency);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return PermissionType.ADMIN.name();
    }
}
