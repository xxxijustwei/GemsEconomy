package net.sakuragame.eternal.gemseconomy.storage;

import net.sakuragame.eternal.gemseconomy.GemsEconomy;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import net.sakuragame.eternal.gemseconomy.utils.SchedulerUtils;
import net.sakuragame.eternal.gemseconomy.utils.UtilServer;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MysqlHandler extends DataStorage {

    private final DataManager dataManager;

    public MysqlHandler() {
        super("MySQL");
        this.dataManager = ClientManagerAPI.getDataManager();
    }

    @Override
    public void initialize() {
        for (EconomyTables table : EconomyTables.values()) {
            table.createTable();
        }

        clearLogger(180);
    }

    @Override
    public Map<Integer, Double> getAllBalance(EternalCurrency currency) {
        Map<Integer, Double> map = new HashMap<>();

        try (DatabaseQuery query = dataManager.createQuery(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                "currency", currency.getCurrency().getUUID()
        )) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                int uid = result.getInt("uid");
                double balance = result.getDouble("balance");

                map.put(uid, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
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

                Currency currency = new Currency(uuid, identifier, displayName);
                currency.setDefaultBalance(defaultBalance);
                currency.setSymbol(symbol);
                currency.setDecimalSupported(isDecimals);
                currency.setDefaultCurrency(isDefault);
                currency.setPayable(payable);
                currency.setColor(color);

                GemsEconomy.getCurrencyManager().add(currency);
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

                currency.setDefaultBalance(defaultBalance);
                currency.setSymbol(symbol);
                currency.setDecimalSupported(isDecimals);
                currency.setDefaultCurrency(isDefault);
                currency.setPayable(payable);
                currency.setColor(color);
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
                        "color"
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
                        currency.getColor().name()
                }
        );

        plugin.getUpdateForwarder().updateCurrency(currency.getUUID());
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
    public Account loadAccount(String name) {
        int uid = ClientManagerAPI.getUserID(name);
        if (uid == -1) return null;

        UUID uuid = ClientManagerAPI.getUserUUID(uid);
        Account account = new Account(uuid, name);

        try (DatabaseQuery query = dataManager.createQuery(EconomyTables.ECONOMY_ACCOUNT.getTableName(), "uid", uid)) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                UUID currencyUUID = UUID.fromString(result.getString("currency"));
                double balance = result.getDouble("balance");

                Currency currency = GemsEconomy.getCurrencyManager().getCurrency(currencyUUID);
                if (currency == null) continue;

                account.getBalances().put(currency, balance);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        account.initBalance();
        return account;
    }

    @Override
    public Account loadAccount(UUID uuid) {
        int uid = ClientManagerAPI.getUserID(uuid);
        if (uid == -1) return null;

        String name = ClientManagerAPI.getUserName(uid);
        Account account = new Account(uuid, name);

        try (DatabaseQuery query = dataManager.createQuery(EconomyTables.ECONOMY_ACCOUNT.getTableName(), "uid", uid)) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                UUID currencyUUID = UUID.fromString(result.getString("currency"));
                double balance = result.getDouble("balance");

                Currency currency = GemsEconomy.getCurrencyManager().getCurrency(currencyUUID);
                if (currency == null) continue;

                account.getBalances().put(currency, balance);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        account.initBalance();
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
        int uid = ClientManagerAPI.getUserID(account.getUUID());
        if (uid == -1) return;

        List<Object[]> datum = new ArrayList<>();
        for (Currency currency : account.getBalances().keySet()) {
            UUID cid = currency.getUUID();
            double balance = account.getBalance(currency);
            datum.add(new Object[] {
                    uid,
                    cid.toString(),
                    balance
            });
        }

        dataManager.executeReplace(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                new String[] {
                        "uid",
                        "currency",
                        "balance"
                },
                datum
        );
    }

    @Override
    public void updateBalance(UUID uuid, Currency currency, double amount) {
        int uid = ClientManagerAPI.getUserID(uuid);
        if (uid == -1) return;

        dataManager.executeReplace(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                new String[] {"uid", "currency", "balance"},
                new Object[] {uid, currency.getUUID().toString(), amount}
        );
    }

    @Override
    public void deleteAccount(Account account) {
        int uid = ClientManagerAPI.getUserID(account.getUUID());
        if (uid == -1) return;

        dataManager.executeDelete(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                "uid",
                uid
        );
    }

    @Override
    public void createAccount(Account account) {
        int uid = ClientManagerAPI.getUserID(account.getUUID());
        if (uid == -1) return;

        Object[][] datum = new Object[GemsEconomy.getCurrencyManager().getCurrencies().size()][];
        int i = 0;
        for (Currency currency : GemsEconomy.getCurrencyManager().getCurrencies()) {
            UUID cid = currency.getUUID();
            double balance = account.getBalance(currency);
            datum[i] = new Object[] {
                    uid,
                    cid.toString(),
                    balance
            };
            i++;
        }

        dataManager.executeInsert(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                new String[] {
                        "uid",
                        "currency",
                        "balance"
                },
                datum
        );
    }

    @Override
    public void addAccountCurrencies(UUID uuid, String name, List<Currency> currencies) {
        int uid = ClientManagerAPI.getUserID(uuid);
        if (uid == -1) return;

        Object[][] datum = new Object[currencies.size()][];
        int i = 0;
        for (Currency currency : currencies) {
            datum[i] = new Object[] {
                    uid,
                    currency.getUUID().toString(),
                    currency.getDefaultBalance()
            };
            i++;
        }

        dataManager.executeInsert(
                EconomyTables.ECONOMY_ACCOUNT.getTableName(),
                new String[] {
                        "uid",
                        "currency",
                        "balance"
                },
                datum
        );
    }

    @Override
    public void insertLogger(UUID uuid, Currency currency, double amount, String content) {
        int uid = ClientManagerAPI.getUserID(uuid);
        if (uid == -1) return;

        dataManager.executeInsert(
                EconomyTables.ECONOMY_LOGGER.getTableName(),
                new String[] {"uid", "identifier", "change", "content"},
                new Object[] {uid, currency.getIdentifier(), amount, content}
        );
    }

    @Override
    public void clearLogger(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day * -1);
        String delete = "DELETE FROM %s WHERE record <= '%s'";

        dataManager.executeSQL(String.format(delete, EconomyTables.ECONOMY_LOGGER.getTableName(), calendar.getTimeInMillis()));
    }
}
