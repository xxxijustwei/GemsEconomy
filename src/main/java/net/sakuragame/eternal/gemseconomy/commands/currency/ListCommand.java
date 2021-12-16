package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "list";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(Message.getPrefix() + "§7There are §f" + GemsEconomy.getCurrencyManager().getCurrencies().size() + "§7 currencies.");
        for (Currency currency : GemsEconomy.getCurrencyManager().getCurrencies()) {
            sender.sendMessage("§a§l>> §e" + currency.getIdentifier());
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
