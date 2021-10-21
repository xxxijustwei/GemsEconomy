package me.xanium.gemseconomy.commands.sub;

import com.taylorswiftcn.justwei.commands.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.commands.PermissionType;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import me.xanium.gemseconomy.utils.SchedulerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        SchedulerUtils.runAsync(() -> {
            if (strings.length == 0 && commandSender instanceof Player) {
                Account account = plugin.getAccountManager().getAccount(getPlayer());
                sendAccountInfo(commandSender, account);
                return;
            }

            if (strings.length > 1) {
                Account account = plugin.getAccountManager().getAccount(strings[0]);
                sendAccountInfo(commandSender, account);
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
        return PermissionType.USER.name();
    }
}
