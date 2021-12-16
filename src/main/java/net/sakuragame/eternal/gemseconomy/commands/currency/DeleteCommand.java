package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

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
        Currency currency = GemsEconomy.getCurrencyManager().getCurrency(identifier);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        plugin.getAccountManager().getAccounts().stream().filter(account -> account.getBalances().containsKey(currency)).forEach(account -> account.getBalances().remove(currency));
        plugin.getDataStore().deleteCurrency(currency);
        GemsEconomy.getCurrencyManager().getCurrencies().remove(currency);
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
