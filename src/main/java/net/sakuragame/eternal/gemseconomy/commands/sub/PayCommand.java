package net.sakuragame.eternal.gemseconomy.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.commands.CommandPerms;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.event.GemsPayEvent;
import net.sakuragame.eternal.gemseconomy.file.Message;
import net.sakuragame.eternal.gemseconomy.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand extends SubCommand {

    private final GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "pay";
    }

    @Override
    public void perform(CommandSender sender, String[] strings) {
        if (strings.length < 2) {
            sender.sendMessage(Message.getPayUsage());
            return;
        }

        if (GemsEconomy.getCurrencyManager().getDefaultCurrency() == null) {
            sender.sendMessage(Message.getNoDefaultCurrency());
            return;
        }

        Player player = getPlayer();
        String s0 = strings[0];
        String s1 = strings[1];
        String s2 = strings[2];

        Currency currency = GemsEconomy.getCurrencyManager().getDefaultCurrency();
        if (strings.length == 3) {
            currency = GemsEconomy.getCurrencyManager().getCurrency(s2);
        }

        if (currency == null) {
            sender.sendMessage(Message.getUnknownCurrency());
            return;
        }

        if (!currency.isPayable()) {
            sender.sendMessage(Message.getCurrencyNotPayable().replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getIdentifier()));
            return;
        }

        if (!sender.hasPermission(CommandPerms.PAY.getNode() + "." + currency.getIdentifier().toLowerCase()) && !sender.hasPermission(CommandPerms.PAY.getNode() + "." + currency.getIdentifier().toLowerCase())) {
            sender.sendMessage(Message.getPayNoPerms().replace("{currencycolor}", "" + currency.getColor()).replace("{currency}", currency.getIdentifier()));
            return;
        }

        double amount;
        if (currency.isDecimalSupported()) {
            try {
                amount = Double.parseDouble(s1);
                if (amount <= 0.0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                sender.sendMessage(Message.getUnvalidAmount());
                return;
            }
        } else {
            try {
                amount = Integer.parseInt(s1);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                sender.sendMessage(Message.getUnvalidAmount());
                return;
            }
        }

        Account payer = plugin.getAccountManager().getAccount(player);
        if (payer == null) {
            sender.sendMessage(Message.getAccountMissing());
            return;
        }

        Account payee = plugin.getAccountManager().getAccount(s0);
        if (payee == null) {
            sender.sendMessage(Message.getPlayerDoesNotExist());
            return;
        }

        if (payee.getUUID().equals(payer.getUUID())) {
            sender.sendMessage(Message.getPayYourself());
            return;
        }

        if (!payee.canReceiveCurrency()) {
            sender.sendMessage(Message.getCannotReceive().replace("{player}", payee.getDisplayName()));
            return;
        }

        if (!payer.hasEnough(currency, amount)) {
            sender.sendMessage(Message.getInsufficientFunds()
                    .replace("{currencycolor}", "" + currency.getColor())
                    .replace("{currency}", currency.getIdentifier())
            );
            return;
        }

        GemsPayEvent event = new GemsPayEvent(currency, payer, payee, amount);
        SchedulerUtils.run(() -> Bukkit.getPluginManager().callEvent(event));
        if (event.isCancelled()) return;

        double payerBal = payer.getBalance(currency) - amount;
        double payeeBal = payee.getBalance(currency) + amount;
        payer.modifyBalance(currency, payerBal);
        payee.modifyBalance(currency, payeeBal);

        if (Bukkit.getPlayer(payee.getUUID()) != null) {
            Bukkit.getPlayer(payee.getUUID()).sendMessage(Message.getPaidMessage()
                    .replace("{currencycolor}", currency.getColor() + "")
                    .replace("{amount}", currency.format(amount))
                    .replace("{player}", sender.getName())
            );
        }
        sender.sendMessage(Message.getPayerMessage()
                .replace("{currencycolor}", currency.getColor() + "")
                .replace("{amount}", currency.format(amount))
                .replace("{player}", payee.getDisplayName())
        );
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return CommandPerms.PAY.getNode();
    }
}
