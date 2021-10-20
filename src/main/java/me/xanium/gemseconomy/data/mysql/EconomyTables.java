package me.xanium.gemseconomy.data.mysql;

public enum EconomyTables {

    ECONOMY_ACCOUNT(new DatabaseTable("economy_account",
            new String[] {
                    "`uuid` varchar(36) NOT NULL",
                    "`player` varchar(48) NOT NULL",
                    "`currency` varchar(36) NOT NULL",
                    "`balance` decimal(19,10) default 0",
                    "UNIQUE KEY `account` (`uuid`, `currency`)"
            }
    )),

    ECONOMY_CURRENCY(new DatabaseTable("economy_currency",
            new String[] {
                    "`uuid` varchar(36) NOT NULL PRIMARY KEY",
                    "`identifier` varchar(16) NOT NULL",
                    "`displayname` varchar(16) NOT NULL",
                    "`default_balance` decimal(19,4) default 0",
                    "`symbol` varchar(8)",
                    "`is_default` int",
                    "`payable` int",
                    "`color` varchar(32)",
                    "`exchange_rate` decimal(19,4)"
            }
    ));

    private DatabaseTable table;

    EconomyTables(DatabaseTable table) {
        this.table = table;
    }

    public String getTableName() {
        return table.getTableName();
    }

    public String[] getColumns() {
        return table.getTableColumns();
    }

    public DatabaseTable getTable() {
        return table;
    }

    public void createTable() {
        table.createTable();
    }
}
