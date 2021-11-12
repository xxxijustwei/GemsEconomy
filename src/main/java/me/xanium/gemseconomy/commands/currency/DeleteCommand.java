package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "delete";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Message.getCurrencyUsage_Delete());
            return;
        }

        String identifier = args[0];
        Currency currency = plugin.getCurrencyManager().getCurrency(identifier);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        plugin.getAccountManager().getAccounts().stream().filter(account -> account.getBalances().containsKey(currency)).forEach(account -> account.getBalances().remove(currency));
        plugin.getDataStore().deleteCurrency(currency);
        plugin.getCurrencyManager().getCurrencies().remove(currency);
        sender.sendMessage(Message.getPrefix() + "§7Deleted currency: §a" + currency.getIdentifier());
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
