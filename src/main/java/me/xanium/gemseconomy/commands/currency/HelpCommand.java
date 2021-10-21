package me.xanium.gemseconomy.commands.currency;

import com.taylorswiftcn.justwei.commands.SubCommand;
import me.xanium.gemseconomy.commands.PermissionType;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        Message.sendCurrencyUsage(commandSender);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return PermissionType.ADMIN.name();
    }
}
