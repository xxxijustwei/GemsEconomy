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
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Message.getCurrencyUsage_Create());
            return;
        }

        String identifier = args[0];
        String displayName = args[1];
        if (plugin.getCurrencyManager().currencyExist(identifier)) {
            sender.sendMessage(Message.getPrefix() + "§cCurrency already exists.");
            return;
        }

        plugin.getCurrencyManager().createNewCurrency(identifier, displayName);
        sender.sendMessage(Message.getPrefix() + "§7Created currency: §a" + identifier);
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
