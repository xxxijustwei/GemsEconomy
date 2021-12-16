package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class SymbolCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "symbol";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Message.getCurrencyUsage_Symbol());
            return;
        }

        String s = args[0];
        String symbol = args[1];

        Currency currency = GemsEconomy.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        if (symbol.equalsIgnoreCase("remove")) {
            currency.setSymbol(null);
            sender.sendMessage(Message.getPrefix() + "§7Currency symbol removed for §f" + currency.getIdentifier());
            plugin.getDataStore().saveCurrency(currency);
            return;
        }

        if (symbol.length() == 1) {
            currency.setSymbol(symbol);
            sender.sendMessage(Message.getPrefix() + "§7Currency symbol for §f" + currency.getIdentifier() + " §7updated: §a" + symbol);
            plugin.getDataStore().saveCurrency(currency);
        } else {
            sender.sendMessage(Message.getPrefix() + "§7Symbol must be 1 character, or remove it with \"remove\".");
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
