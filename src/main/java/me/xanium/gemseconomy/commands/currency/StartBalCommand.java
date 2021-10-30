package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import me.xanium.gemseconomy.utils.UtilString;
import org.bukkit.command.CommandSender;

public class StartBalCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "startBal";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Message.getCurrencyUsage_Startbal());
            return;
        }

        String s1 = args[1];
        String s2 = args[2];

        Currency currency = plugin.getCurrencyManager().getCurrency(s1);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        double amount;
        if (currency.isDecimalSupported()) {
            try {
                amount = Double.parseDouble(s2);
                if (amount <= 0.0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                sender.sendMessage(Message.getUnvalidAmount());
                return;
            }
        }
        else {
            try {
                amount = Integer.parseInt(s2);
                if (amount <= 0.0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                sender.sendMessage(Message.getUnvalidAmount());
                return;
            }
        }

        currency.setDefaultBalance(amount);
        sender.sendMessage(Message.getPrefix() + "§7Starting balance for §f" + currency.getIdentifier() + " §7set: §a" + UtilString.format(currency.getDefaultBalance()));
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
