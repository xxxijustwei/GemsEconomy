package me.xanium.gemseconomy.commands.economy;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TakeCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "take";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (args.length < 3) {
                sender.sendMessage(Message.getTakeUsage());
                return;
            }
            String user = args[1];
            String s = args[2];

            if (!MegumiUtil.isFloat(s)) {
                sender.sendMessage(Message.getUnvalidAmount());
                return;
            }

            Currency currency = plugin.getCurrencyManager().getDefaultCurrency();

            if (args.length > 3) {
                currency = plugin.getCurrencyManager().getCurrency(args[3]);
            }

            if (currency == null) {
                sender.sendMessage(Message.getUnknownCurrency());
                return;
            }

            double amount = Double.parseDouble(s);

            Account account = plugin.getAccountManager().getAccount(user);
            if (account == null) {
                sender.sendMessage(Message.getPlayerDoesNotExist());
                return;
            }

            if (account.withdraw(currency, amount)) {
                sender.sendMessage(Message.getTakeMessage()
                        .replace("{player}", account.getNickname())
                        .replace("{currencycolor}", currency.getColor() + "")
                        .replace("{amount}", currency.format(amount)));
            } else {
                sender.sendMessage(Message.getTargetInsufficientFunds()
                        .replace("{currencycolor}", currency.getColor() + "")
                        .replace("{currency}", currency.getIdentifier())
                        .replace("{target}", account.getDisplayName()));
            }
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
