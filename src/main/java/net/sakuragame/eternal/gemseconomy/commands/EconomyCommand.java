package net.sakuragame.eternal.gemseconomy.commands;

import com.taylorswiftcn.justwei.commands.JustCommand;
import net.sakuragame.eternal.gemseconomy.commands.economy.GiveCommand;
import net.sakuragame.eternal.gemseconomy.commands.economy.HelpCommand;
import net.sakuragame.eternal.gemseconomy.commands.economy.SetCommand;
import net.sakuragame.eternal.gemseconomy.commands.economy.TakeCommand;

public class EconomyCommand extends JustCommand {

    public EconomyCommand() {
        super(new HelpCommand());
        this.register(new GiveCommand());
        this.register(new TakeCommand());
        this.register(new SetCommand());
    }
}
