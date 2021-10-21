package me.xanium.gemseconomy.commands;

import com.taylorswiftcn.justwei.commands.ICommand;
import me.xanium.gemseconomy.commands.currency.*;

public class CurrencyCommand extends ICommand {

    public CurrencyCommand() {
        this.setHelpCommand(new HelpCommand());
        this.register(new CreateCommand());
        this.register(new DeleteCommand());
        this.register(new ViewCommand());
        this.register(new ListCommand());
        this.register(new SymbolCommand());
        this.register(new ColorCommand());
        this.register(new DecimalsCommand());
        this.register(new PayableCommand());
        this.register(new DefaultCommand());
        this.register(new StartBalCommand());
    }
}
