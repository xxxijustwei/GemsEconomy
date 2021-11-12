package me.xanium.gemseconomy.commands;

import com.taylorswiftcn.justwei.commands.JustCommand;
import me.xanium.gemseconomy.commands.sub.BalanceCommand;

public class BalanceMainCommand extends JustCommand {

    public BalanceMainCommand() {
        super(new BalanceCommand());
    }
}
