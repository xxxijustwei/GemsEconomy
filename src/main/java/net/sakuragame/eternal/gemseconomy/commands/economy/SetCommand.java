package net.sakuragame.eternal.gemseconomy.commands.economy;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.file.Message;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class SetCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "set";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (args.length < 2) {
                sender.sendMessage(Message.getSetUsage());
                return;
            }
            String user = args[0];
            String s = args[1];

            if (!MegumiUtil.isFloat(s)) {
                sender.sendMessage(Message.getUnvalidAmount());
                return;
            }

            Currency currency = GemsEconomy.getCurrencyManager().getDefaultCurrency();

            if (args.length > 2) {
                currency = GemsEconomy.getCurrencyManager().getCurrency(args[2]);
            }

            if (currency == null) {
                sender.sendMessage(Message.getUnknownCurrency());
                return;
            }

            double amount = Double.parseDouble(s);

            Account account = plugin.getAccountManager().getAccount(user);
            if (account == null) {
                UUID uuid = ClientManagerAPI.getUserUUID(user);
                if (uuid == null) {
                    sender.sendMessage(Message.getPlayerDoesNotExist());
                    return;
                }
                account = plugin.getAccountManager().getAccount(uuid, true);
            }

            account.setBalance(currency, amount);
            sender.sendMessage(Message.getSetMessage()
                    .replace("{player}", account.getNickname())
                    .replace("{currencycolor}", currency.getColor() + "")
                    .replace("{amount}", currency.format(amount)));
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
