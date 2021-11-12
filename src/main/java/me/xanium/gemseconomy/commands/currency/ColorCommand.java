package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ColorCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "color";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 3) {
            commandSender.sendMessage(Message.getCurrencyUsage_Color());
            return;
        }

        String s = strings[1];
        String colorID = strings[2];

        Currency currency = plugin.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            commandSender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        try {
            ChatColor color = ChatColor.valueOf(colorID.toUpperCase());
            if (color.isFormat()) {
                throw new Exception();
            }
            currency.setColor(color);
            commandSender.sendMessage(Message.getPrefix() + "§7Color for §f" + currency.getIdentifier() + " §7updated: " + color + color.name());
            plugin.getDataStore().saveCurrency(currency);
        } catch (Exception ex) {
            commandSender.sendMessage(Message.getPrefix() + "§cInvalid chat color.");
        }
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
