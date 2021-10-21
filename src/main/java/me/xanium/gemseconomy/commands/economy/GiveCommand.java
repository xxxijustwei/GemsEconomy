package me.xanium.gemseconomy.commands.economy;

import com.taylorswiftcn.justwei.commands.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.commands.PermissionType;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class GiveCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "give";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (strings.length < 3) {
                commandSender.sendMessage(Message.getGiveUsage());
                return;
            }
            String user = strings[1];
            String s = strings[2];

            if (!MegumiUtil.isFloat(s)) {
                commandSender.sendMessage(Message.getUnvalidAmount());
                return;
            }

            Currency currency = plugin.getCurrencyManager().getDefaultCurrency();

            if (strings.length > 3) {
                currency = plugin.getCurrencyManager().getCurrency(strings[3]);
            }

            if (currency == null) {
                commandSender.sendMessage(Message.getUnknownCurrency());
                return;
            }

            double amount = Double.parseDouble(s);

            Account account = plugin.getAccountManager().getAccount(user);
            if (account == null) {
                commandSender.sendMessage(Message.getPlayerDoesNotExist());
                return;
            }

            if (account.deposit(currency, amount)) {
                commandSender.sendMessage(Message.getAddMessage()
                        .replace("{player}", account.getNickname())
                        .replace("{currencycolor}", currency.getColor() + "")
                        .replace("{amount}", currency.format(amount)));
            }
        });
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return PermissionType.ADMIN.name();
    }
}
