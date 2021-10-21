package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.PermissionType;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class PayableCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "payable";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 2) {
            commandSender.sendMessage(Message.getCurrencyUsage_Payable());
            return;
        }

        String s = strings[1];
        Currency currency = plugin.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            commandSender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        currency.setPayable(!currency.isPayable());
        commandSender.sendMessage(Message.getPrefix() + "§7Toggled payability for §f" + currency.getIdentifier() + "§7: " + (currency.isPayable() ? "§aYes" : "§cNo"));
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
