package me.xanium.gemseconomy.commands;

public enum PermissionType {

    ADMIN("economy.admin"),
    PAY("economy.pay"),
    USER("economy.user");

    private String node;

    private PermissionType(String node) {
        this.node = node;
    }
}
