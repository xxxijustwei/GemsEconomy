package net.sakuragame.eternal.gemseconomy.data.mysql;

import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;

public class DatabaseTable {

    String tableName;
    String[] tableColumns;

    public DatabaseTable(String tableName, String[] tableColumns) {
        this.tableColumns = tableColumns;
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String[] getTableColumns() {
        return this.tableColumns;
    }

    public void createTable() {
        ClientManagerAPI.getDataManager().createTable(this.getTableName(), this.getTableColumns());
    }
}
