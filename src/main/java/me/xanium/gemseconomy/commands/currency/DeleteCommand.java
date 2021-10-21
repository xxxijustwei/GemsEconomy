package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.PermissionType;
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
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 2) {
            commandSender.sendMessage(Message.getCurrencyUsage_Delete());
            return;
        }

        String identifier = strings[1];
        Currency currency = plugin.getCurrencyManager().getCurrency(identifier);
        if (currency == null) {
            commandSender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        plugin.getAccountManager().getAccounts().stream().filter(account -> account.getBalances().containsKey(currency)).forEach(account -> account.getBalances().remove(currency));
        plugin.getDataStore().deleteCurrency(currency);
        plugin.getCurrencyManager().getCurrencies().remove(currency);
        commandSender.sendMessage(Message.getPrefix() + "§7Deleted currency: §a" + currency.getIdentifier());
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
