package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ColorCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "color";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Message.getCurrencyUsage_Color());
            return;
        }

        String s = args[0];
        String colorID = args[1];

        Currency currency = GemsEconomy.getCurrencyManager().getCurrency(s);
        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        try {
            ChatColor color = ChatColor.valueOf(colorID.toUpperCase());
            if (color.isFormat()) {
                throw new Exception();
            }
            currency.setColor(color);
            sender.sendMessage(Message.getPrefix() + "§7Color for §f" + currency.getIdentifier() + " §7updated: " + color + color.name());
            plugin.getDataStore().saveCurrency(currency);
        } catch (Exception ex) {
            sender.sendMessage(Message.getPrefix() + "§cInvalid chat color.");
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
