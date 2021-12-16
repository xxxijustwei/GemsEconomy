package net.sakuragame.eternal.gemseconomy.commands;

import com.taylorswiftcn.justwei.commands.JustCommand;
import net.sakuragame.eternal.gemseconomy.commands.sub.BalanceCommand;

public class BalanceMainCommand extends JustCommand {

    public BalanceMainCommand() {
        super(new BalanceCommand());
    }
}
