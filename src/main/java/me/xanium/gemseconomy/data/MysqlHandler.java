package me.xanium.gemseconomy.data;

import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.currency.CachedTopList;
import me.xanium.gemseconomy.currency.CachedTopListEntry;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.data.mysql.EconomyTables;
import me.xanium.gemseconomy.utils.SchedulerUtils;
import me.xanium.gemseconomy.utils.UtilServer;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MysqlHandler extends DataStorage {

    private final DataManager dataManager;
    private final HashMap<UUID, CachedTopList> topList;

    public MysqlHandler() {
        super("MySQL", true);
        this.dataManager = ClientManagerAPI.getDataManager();
        this.topList = new HashMap<>();
    }

    @Override
    public void initialize() {
        for (EconomyTables table : EconomyTables.values()) {
            table.createTable();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void loadCurrencies() {
        try (DatabaseQuery query = dataManager.createQuery("SELECT * FROM " + EconomyTables.ECONOMY_CURRENCY.getTableName())) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String identifier = result.getString("identifier");
                String displayName = result.getString("displayname");
                double defaultBalance = result.getDouble("default_balance");
                String symbol = result.getString("symbol");
                boolean isDecimals = result.getInt("is_decimals") == 1;
                boolean isDefault = result.getInt("is_default") == 1;
                boolean payable = result.getInt("payable") == 1;
                ChatColor color = ChatColor.valueOf(result.getString("color"));
                double exchangeRate = result.getDouble("exchange_rate");

                Currency currency = new Currency(uuid, identifier, displayName);
                currency.setDefaultBalance(defaultBalance);
                currency.setSymbol(symbol);
                currency.setDecimalSupported(isDecimals);
                currency.setDefaultCurrency(isDefault);
                currency.setPayable(payable);
                currency.setColor(color);
                currency.setExchangeRate(exchangeRate);

                plugin.getCurrencyManager().add(currency);
                UtilServer.consoleLog("Loaded currency: " + currency.getIdentifier() + " (" + currency.getDisplayName() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCurrencyLocally(Currency currency) {
        try (DatabaseQuery query = dataManager.createQuery(EconomyTables.ECONOMY_CURRENCY.getTableName(), "uuid", currency.getUUID().toString())) {
            ResultSet result = query.getResultSet();
            if (result.next()) {
                double defaultBalance = result.getDouble("default_balance");
                String symbol = result.getString("symbol");
                boolean isDecimals = result.getInt("is_decimals") == 1;
                boolean isDefault = result.getInt("is_default") == 1;
                boolean payable = result.getInt("payable") == 1;
                ChatColor color = ChatColor.valueOf(result.getString("color"));
                double exchangeRate = result.getDouble("exchange_rate");

                currency.setDefaultBalance(defaultBalance);
                currency.setSymbol(symbol);
                currency.setDecimalSupported(isDecimals);
                currency.setDefaultCurrency(isDefault);
                currency.setPayable(payable);
                currency.setColor(color);
                currency.setExchangeRate(exchangeRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCurrency(Currency currency) {
        dataManager.executeReplace(
                EconomyTables.ECONOMY_CURRENCY.getTableName(),
                new String[] {
                        "uuid",
                        "identifier",
                        "displayname",
                        "default_balance",
                        "symbol",
                        "is_decimals",
                        "is_default",
                        "payable",
                        "color",
                        "exchange_rate"
                },
                new Object[] {
                        currency.getUUID().toString(),
                        currency.getIdentifier(),
                        currency.getDisplayName(),
                        currency.getDefaultBalance(),
                        currency.getSymbol(),
                        currency.isDecimalSupported() ? 1 : 0,
                        currency.isDefaultCurrency() ? 1 : 0,
                        currency.isPayable() ? 1 : 0,
                        currency.getColor().name(),
                        currency.getExchangeRate()
                }
        );
    }

    @Override
    public void deleteCurrency(Currency currency) {
        dataManager.executeDelete(
                EconomyTables.ECONOMY_CURRENCY.getTableName(),
                "uuid",
                currency.getUUID().toString()
        );
        dataManager.executeDelete(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                "currency",
                currency.getUUID().toString()
        );
    }

    @Override
    public void getTopList(Currency currency, int offset, int amount, Callback<LinkedList<CachedTopListEntry>> callback) {
        CachedTopList cache = topList.get(currency.getUUID());
        if (cache != null && !cache.isExpired()) {
            LinkedList<CachedTopListEntry> result = new LinkedList<>();
            int collected = 0;
            for(int i = offset; i < cache.getResults().size(); i++){
                if(collected == amount) break;
                result.add(cache.getResults().get(i));
                collected++;
            }
            SchedulerUtils.run(() -> callback.call(result));
        }

        SchedulerUtils.runAsync(() -> {
            LinkedList<CachedTopListEntry> entries = new LinkedList<>();
            try (DatabaseQuery query = dataManager.createQuery(
                    String.format(
                            "SELECT * FROM %s WHERE currency = '%s' ORDER BY balance DESC LIMIT %s, %s",
                            EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                            currency.getUUID().toString(),
                            offset,
                            offset + amount
                    )
            )) {
                ResultSet result = query.getResultSet();
                while (result.next()) {
                    String playerName = result.getString("player");
                    double balance = result.getDouble("balance");
                    entries.add(new CachedTopListEntry(playerName, balance));
                }

                SchedulerUtils.run(() -> callback.call(entries));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Account loadAccount(String name) {
        Account account = null;
        try (DatabaseQuery query = dataManager.createQuery(EconomyTables.ECONOMY_ACCOUNT.getTableName(), "player", name)) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                UUID currencyUUID = UUID.fromString(result.getString("currency"));
                double balance = result.getDouble("balance");

                if (account == null) account = new Account(uuid, name);

                Currency currency = plugin.getCurrencyManager().getCurrency(currencyUUID);
                if (currency == null) continue;

                account.getBalances().put(currency, balance);
            }

            if (account != null) {
                account.initBalance();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public Account loadAccount(UUID uuid) {
        Account account = null;
        try (DatabaseQuery query = dataManager.createQuery(EconomyTables.ECONOMY_ACCOUNT.getTableName(), "uuid", uuid.toString())) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                String name = result.getString("player");
                UUID currencyUUID = UUID.fromString(result.getString("currency"));
                double balance = result.getDouble("balance");

                if (account == null) account = new Account(uuid, name);

                Currency currency = plugin.getCurrencyManager().getCurrency(currencyUUID);
                if (currency == null) continue;

                account.getBalances().put(currency, balance);
            }

            if (account != null) {
                account.initBalance();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public void loadAccount(UUID uuid, Callback<Account> callback) {
        SchedulerUtils.runAsync(() -> {
            Account account = this.loadAccount(uuid);
            SchedulerUtils.run(() -> callback.call(account));
        });
    }

    @Override
    public void loadAccount(String name, Callback<Account> callback) {
        SchedulerUtils.runAsync(() -> {
            Account account = this.loadAccount(name);
            SchedulerUtils.run(() -> callback.call(account));
        });
    }

    @Override
    public void saveAccount(Account account) {
        List<Object[]> datum = new ArrayList<>();
        for (Currency currency : account.getBalances().keySet()) {
            UUID cid = currency.getUUID();
            double balance = account.getBalance(currency);
            datum.add(new Object[] {
                    account.getUuid().toString(),
                    account.getNickname(),
                    cid.toString(),
                    balance
            });
        }

        dataManager.executeReplace(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                new String[] {
                        "uuid",
                        "player",
                        "currency",
                        "balance"
                },
                datum
        );
    }

    @Override
    public void deleteAccount(Account account) {
        dataManager.executeDelete(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                "uuid",
                account.getUuid().toString()
        );
    }

    @Override
    public void createAccount(Account account) {
        Object[][] datum = new Object[account.getBalances().size()][];
        int i = 0;
        for (Currency currency : account.getBalances().keySet()) {
            UUID cid = currency.getUUID();
            double balance = account.getBalance(currency);
            datum[i] = new Object[] {
                    account.getUuid().toString(),
                    account.getNickname(),
                    cid.toString(),
                    balance
            };
            i++;
        }

        dataManager.executeInsert(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                new String[] {
                        "uuid",
                        "player",
                        "currency",
                        "balance"
                },
                datum
        );
    }

    @Override
    public void addAccountCurrencies(UUID uuid, String name, List<Currency> currencies) {
        Object[][] datum = new Object[currencies.size()][];
        int i = 0;
        for (Currency currency : currencies) {
            datum[i] = new Object[] {
                    uuid.toString(),
                    name,
                    currency.getUUID().toString(),
                    currency.getDefaultBalance()
            };
            i++;
        }

        dataManager.executeInsert(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                new String[] {
                        "uuid",
                        "player",
                        "currency",
                        "balance"
                },
                datum
        );
    }

    @Override
    public ArrayList<Account> getOfflineAccounts() {
        return null;
    }
}
