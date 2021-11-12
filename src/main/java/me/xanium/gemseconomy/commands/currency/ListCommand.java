package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "list";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(Message.getPrefix() + "§7There are §f" + plugin.getCurrencyManager().getCurrencies().size() + "§7 currencies.");
        for (Currency currency : plugin.getCurrencyManager().getCurrencies()) {
            commandSender.sendMessage("§a§l>> §e" + currency.getIdentifier());
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
