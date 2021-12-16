package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class ViewCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "view";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Message.getCurrencyUsage_View());
            return;
        }

        String identifier = args[0];
        Currency currency = GemsEconomy.getCurrencyManager().getCurrency(identifier);
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
