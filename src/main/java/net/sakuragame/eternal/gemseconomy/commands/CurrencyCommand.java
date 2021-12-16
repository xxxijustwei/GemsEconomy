package net.sakuragame.eternal.gemseconomy.commands;

import com.taylorswiftcn.justwei.commands.JustCommand;
import net.sakuragame.eternal.gemseconomy.commands.currency.*;

public class CurrencyCommand extends JustCommand {

    public CurrencyCommand() {
        super(new HelpCommand());
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
