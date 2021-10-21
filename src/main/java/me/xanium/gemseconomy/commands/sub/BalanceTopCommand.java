package me.xanium.gemseconomy.commands.sub;

import com.taylorswiftcn.justwei.commands.SubCommand;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.commands.PermissionType;
import me.xanium.gemseconomy.currency.CachedTopListEntry;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.Message;
import org.bukkit.command.CommandSender;

public class BalanceTopCommand extends SubCommand {

    private GemsEconomy plugin = GemsEconomy.getInstance();

    @Override
    public String getIdentifier() {
        return "balTop";
    }

    @Override
    public void perform(CommandSender sender, String[] strings) {
        if (!plugin.getDataStore().isTopSupported()) {
            sender.sendMessage(Message.getBalanceTopNoSupport().replace("{storage}", plugin.getDataStore().getName()));
            return;
        }

        Currency currency = plugin.getCurrencyManager().getDefaultCurrency();
        int page = 1;
        if (strings.length > 0) {
            currency = plugin.getCurrencyManager().getCurrency(strings[0]);
            if (currency == null) {
                sender.sendMessage(Message.getUnknownCurrency());
                return;
            }
        }

        if (strings.length > 1) {
            try {
                page = Math.max(1, Integer.parseInt(strings[1]));
            } catch (NumberFormatException ex) {
                sender.sendMessage(Message.getUnvalidPage());
                return;
            }
        }

        int offset = 10 * (page - 1);
        final int pageNumber = page;
        final Currency curr = currency;

        if (currency != null) {
            plugin.getDataStore().getTopList(currency, offset, 10, cachedTopListEntries -> {
                sender.sendMessage(Message.getBalanceTopHeader()
                        .replace("{currencycolor}", "" + curr.getColor())
                        .replace("{currencyidentifier}", curr.getIdentifier())
                        .replace("{page}", String.valueOf(pageNumber)));

                int num = (10 * (pageNumber - 1)) + 1;
                for (CachedTopListEntry entry : cachedTopListEntries) {
                    double balance = entry.getAmount();
                    sender.sendMessage(Message.getBalanceTop()
                            .replace("{number}", String.valueOf(num))
                            .replace("{currencycolor}", "" + curr.getColor())
                            .replace("{player}", entry.getName())
                            .replace("{balance}", curr.format(balance))
                    );
                    num++;
                }
                if (cachedTopListEntries.isEmpty()) {
                    sender.sendMessage(Message.getBalanceTopEmpty());
                } else {
                    sender.sendMessage(Message.getBalanceTopNext()
                            .replace("{currencycolor}", "" + curr.getColor())
                            .replace("{currencyidentifier}", curr.getIdentifier())
                            .replace("{page}", String.valueOf((pageNumber + 1)))
                    );
                }
            });
        }
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
