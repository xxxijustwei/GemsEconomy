package net.sakuragame.eternal.gemseconomy.commands;

public enum CommandPerms {

    ADMIN("economy.admin"),
    PAY("economy.pay"),
    USER("economy.user");

    private final String node;

    CommandPerms(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
