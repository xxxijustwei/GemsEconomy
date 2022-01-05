package net.sakuragame.eternal.gemseconomy.commands.economy;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.file.Message;
import net.sakuragame.eternal.gemseconomy.utils.SchedulerUtils;
import org.bukkit.command.CommandSender;

public class ClearCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "clear";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 1) return;

        String s = args[0];
        if (!MegumiUtil.isNumber(s)) return;

        int day = Integer.parseInt(s);

        SchedulerUtils.runAsync(() -> {
            GemsEconomy.getInstance().getDataStore().clearLogger(day);
            sender.sendMessage(Message.getPrefix() + " clear logger succeed!");
        });
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
