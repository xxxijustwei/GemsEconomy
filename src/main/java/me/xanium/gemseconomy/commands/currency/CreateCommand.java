package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class CreateCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "create";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (strings.length < 3) {
            commandSender.sendMessage(Message.getCurrencyUsage_Create());
            return;
        }

        String identifier = strings[1];
        String displayName = strings[2];
        if (plugin.getCurrencyManager().currencyExist(identifier)) {
            commandSender.sendMessage(Message.getPrefix() + "§cCurrency already exists.");
            return;
        }

        plugin.getCurrencyManager().createNewCurrency(identifier, displayName);
        commandSender.sendMessage(Message.getPrefix() + "§7Created currency: §a" + identifier);
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
