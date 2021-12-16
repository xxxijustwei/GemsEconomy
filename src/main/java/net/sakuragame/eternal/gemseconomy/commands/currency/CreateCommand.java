package net.sakuragame.eternal.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class CreateCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

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
        if (GemsEconomy.getCurrencyManager().currencyExist(identifier)) {
            sender.sendMessage(Message.getPrefix() + "§cCurrency already exists.");
            return;
        }

        GemsEconomy.getCurrencyManager().createNewCurrency(identifier, displayName);
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
