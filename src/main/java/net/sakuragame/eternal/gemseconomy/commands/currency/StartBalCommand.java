package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import net.sakuragame.eternal.gemseconomy.utils.UtilString;
import org.bukkit.command.CommandSender;

public class StartBalCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "startBal";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Message.getCurrencyUsage_Startbal());
            return;
        }

        String s1 = args[0];
        String s2 = args[1];

        Currency currency = GemsEconomy.getCurrencyManager().getCurrency(s1);
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
