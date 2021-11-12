package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class SymbolCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "symbol";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 3) {
            commandSender.sendMessage(Message.getCurrencyUsage_Symbol());
            return;
        }

        String s = strings[1];
        String symbol = strings[2];

        Currency currency = plugin.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            commandSender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        if (symbol.equalsIgnoreCase("remove")) {
            currency.setSymbol(null);
            commandSender.sendMessage(Message.getPrefix() + "§7Currency symbol removed for §f" + currency.getIdentifier());
            plugin.getDataStore().saveCurrency(currency);
            return;
        }

        if (symbol.length() == 1) {
            currency.setSymbol(symbol);
            commandSender.sendMessage(Message.getPrefix() + "§7Currency symbol for §f" + currency.getIdentifier() + " §7updated: §a" + symbol);
            plugin.getDataStore().saveCurrency(currency);
        } else {
            commandSender.sendMessage(Message.getPrefix() + "§7Symbol must be 1 character, or remove it with \"remove\".");
        }
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
