/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package net.sakuragame.eternal.gemseconomy.currency;

import com.taylorswiftcn.justwei.util.UnitConvert;
import net.sakuragame.eternal.gemseconomy.utils.UtilString;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Currency {

    private final  UUID uuid;
    private String identifier;
    private String displayName;
    private String symbol = null;
    private ChatColor color = ChatColor.WHITE;
    private boolean decimalSupported = true;
    private boolean payable = false;
    private boolean defaultCurrency = false;
    private double defaultBalance = 0.0;

    public Currency(UUID uuid, String identifier, String displayName) {
        this.uuid = uuid;
        this.identifier = identifier;
        this.displayName = displayName;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDefaultBalance(double defaultBalance) {
        this.defaultBalance = defaultBalance;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultBalance() {
        return this.defaultBalance;
    }

    public String format(double amount) {
        return format(amount, true);
    }

    public String format(double amount, boolean symbol) {
        StringBuilder amt = new StringBuilder();
        if (this.getSymbol() != null && symbol) {
            amt.append(this.getSymbol());
        }
        if (this.isDecimalSupported()) {
            amt.append(amount < 10000 ? UtilString.format(amount) : UnitConvert.formatEN(UnitConvert.TenThousand, amount, 2));
        } else {
            amt.append(amount < 10000 ? ((int) amount) : UnitConvert.formatEN(UnitConvert.TenThousand, amount));
        }
        amt.append(" ");
        amt.append(color);
        amt.append(displayName);
        return amt.toString();
    }

    public boolean isDefaultCurrency() {
        return this.defaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public boolean isPayable() {
        return this.payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public boolean isDecimalSupported() {
        return this.decimalSupported;
    }

    public void setDecimalSupported(boolean decimalSupported) {
        this.decimalSupported = decimalSupported;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

