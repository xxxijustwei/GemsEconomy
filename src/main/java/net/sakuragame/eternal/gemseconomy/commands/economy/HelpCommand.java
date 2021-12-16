package net.sakuragame.eternal.gemseconomy.commands.economy;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.file.Message;
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
