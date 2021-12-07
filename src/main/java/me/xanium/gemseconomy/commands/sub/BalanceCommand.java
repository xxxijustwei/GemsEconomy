package me.xanium.gemseconomy.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.commands.CommandPerms;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import me.xanium.gemseconomy.utils.SchedulerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        SchedulerUtils.runAsync(() -> {
            if (args.length == 0 && sender instanceof Player) {
                Account account = plugin.getAccountManager().getAccount(getPlayer());
                sendAccountInfo(sender, account);
                return;
            }

            if (args.length > 0) {
                Account account = plugin.getAccountManager().getAccount(args[0]);
                sendAccountInfo(sender, account);
            }
        });
    }

    private void sendAccountInfo(CommandSender sender, Account account) {
        if (account == null) {
            sender.sendMessage(Message.getPlayerDoesNotExist());
            return;
        }

        int currencies = plugin.getCurrencyManager().getCurrencies().size();

        if (currencies == 0) {
            sender.sendMessage(Message.getNoDefaultCurrency());
            return;
        }

        if (currencies == 1) {
            Currency currency = plugin.getCurrencyManager().getDefaultCurrency();
            if (currency == null) {
                sender.sendMessage(Message.getBalanceNone().replace("{player}", account.getNickname()));
                return;
            }
            double balance = account.getBalance(currency);
            sender.sendMessage(Message.getBalance().replace("{player}", account.getDisplayName()).replace("{currencycolor}", "" + currency.getColor()).replace("{balance}", currency.format(balance)));
        } else {
            sender.sendMessage(Message.getBalanceMultiple().replace("{player}", account.getDisplayName()));
            for (Currency currency : plugin.getCurrencyManager().getCurrencies()) {
                double balance = account.getBalance(currency);
                sender.sendMessage(Message.getBalanceList().replace("{currencycolor}", currency.getColor() + "").replace("{format}", currency.format(balance)));
            }
        }
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.USER.getNode();
    }
}
