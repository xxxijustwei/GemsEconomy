package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class DefaultCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "default";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Message.getCurrencyUsage_Default());
            return;
        }

        String s = args[0];
        Currency currency = GemsEconomy.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        Currency current = GemsEconomy.getCurrencyManager().getDefaultCurrency();
        if (current != null) {
            current.setDefaultCurrency(false);
            plugin.getDataStore().saveCurrency(current);
        }

        currency.setDefaultCurrency(true);
        sender.sendMessage(Message.getPrefix() + "§7Set default currency to §f" + currency.getIdentifier());
        plugin.getDataStore().saveCurrency(currency);
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
