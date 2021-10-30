package me.xanium.gemseconomy.commands.economy;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Message.getManageHelp(sender);
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
