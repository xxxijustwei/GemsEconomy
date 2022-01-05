package net.sakuragame.eternal.gemseconomy.storage;

public enum EconomyTables {

    ECONOMY_ACCOUNT(new DatabaseTable("economy_account",
            new String[] {
                    "`uid` int NOT NULL",
                    "`currency` varchar(36) NOT NULL",
                    "`balance` decimal(19,2) default 0",
                    "UNIQUE KEY `account` (`uid`, `currency`)"
            }
    )),

    ECONOMY_CURRENCY(new DatabaseTable("economy_currency",
            new String[] {
                    "`uuid` varchar(36) NOT NULL PRIMARY KEY",
                    "`identifier` varchar(16) NOT NULL",
                    "`displayname` varchar(16) NOT NULL",
                    "`default_balance` decimal(19,2) default 0",
                    "`symbol` varchar(8)",
                    "`is_decimals` int",
                    "`is_default` int",
                    "`payable` int",
                    "`color` varchar(32)"
            }
    )),

    ECONOMY_LOGGER(new DatabaseTable("economy_logger",
            new String[] {
                    "`id` int NOT NULL PRIMARY KEY AUTO_INCREMENT",
                    "`uid` int NOT NULL",
                    "`record` timestamp DEFAULT CURRENT_TIMESTAMP",
                    "`identifier` varchar(36) NOT NULL",
                    "`change` decimal(19,2) NOT NULL",
                    "`content` VARCHAR(128) NOT NULL"
            }
    ));

    private final DatabaseTable table;

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
