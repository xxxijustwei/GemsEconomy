package net.sakuragame.eternal.gemseconomy.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import net.sakuragame.eternal.gemseconomy.utils.SchedulerUtils;
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

        int currencies = GemsEconomy.getCurrencyManager().getCurrencies().size();

        if (currencies == 0) {
            sender.sendMessage(Message.getNoDefaultCurrency());
            return;
        }

        if (currencies == 1) {
            Currency currency = GemsEconomy.getCurrencyManager().getDefaultCurrency();
            if (currency == null) {
                sender.sendMessage(Message.getBalanceNone().replace("{player}", account.getNickname()));
                return;
            }
            double balance = account.getBalance(currency);
            sender.sendMessage(Message.getBalance().replace("{player}", account.getDisplayName()).replace("{currencycolor}", "" + currency.getColor()).replace("{balance}", currency.format(balance)));
        } else {
            sender.sendMessage(Message.getBalanceMultiple().replace("{player}", account.getDisplayName()));
            for (Currency currency : GemsEconomy.getCurrencyManager().getCurrencies()) {
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
