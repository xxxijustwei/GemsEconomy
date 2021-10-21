package me.xanium.gemseconomy.commands;

import com.taylorswiftcn.justwei.commands.ICommand;
import me.xanium.gemseconomy.commands.economy.GiveCommand;
import me.xanium.gemseconomy.commands.economy.HelpCommand;
import me.xanium.gemseconomy.commands.economy.SetCommand;
import me.xanium.gemseconomy.commands.economy.TakeCommand;

public class EconomyCommand extends ICommand {

    public EconomyCommand() {
        this.setHelpCommand(new HelpCommand());
        this.register(new GiveCommand());
        this.register(new TakeCommand());
        this.register(new SetCommand());
    }
}
