package me.xanium.gemseconomy.commands;

import com.taylorswiftcn.justwei.commands.ICommand;
import me.xanium.gemseconomy.commands.sub.BalanceCommand;

public class BalanceMainCommand extends ICommand {

    public BalanceMainCommand() {
        this.setHelpCommand(new BalanceCommand());
    }
}
