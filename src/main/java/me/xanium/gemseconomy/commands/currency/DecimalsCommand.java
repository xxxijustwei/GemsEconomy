package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class DecimalsCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "decimals";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Message.getCurrencyUsage_Decimals());
            return;
        }

        String s = args[1];
        Currency currency = plugin.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        currency.setDecimalSupported(!currency.isDecimalSupported());
        sender.sendMessage(Message.getPrefix() + "§7Toggled Decimal Support for §f" + currency.getIdentifier() + "§7: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
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
