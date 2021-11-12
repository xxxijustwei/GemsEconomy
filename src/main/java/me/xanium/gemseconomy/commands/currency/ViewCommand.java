package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class ViewCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "view";
    }

    @Override
    public void perform(CommandSender sender, String[] strings) {
        if (strings.length < 2) {
            sender.sendMessage(Message.getCurrencyUsage_View());
            return;
        }

        String identifier = strings[1];
        Currency currency = plugin.getCurrencyManager().getCurrency(identifier);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        sender.sendMessage(Message.getPrefix() + "§7ID: §c" + currency.getUUID().toString());
        sender.sendMessage(Message.getPrefix() + "§7Identifier: §a" + currency.getIdentifier());
        sender.sendMessage(Message.getPrefix() + "§7DisplayName: §a" + currency.getDisplayName());
        sender.sendMessage(Message.getPrefix() + "§7Start Balance: " + currency.getColor() + currency.format(currency.getDefaultBalance()) + "§7.");
        sender.sendMessage(Message.getPrefix() + "§7Decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
        sender.sendMessage(Message.getPrefix() + "§7Default: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"));
        sender.sendMessage(Message.getPrefix() + "§7Payable: " + (currency.isPayable() ? "§aYes" : "§cNo"));
        sender.sendMessage(Message.getPrefix() + "§7Color: " + currency.getColor() + currency.getColor().name());
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
