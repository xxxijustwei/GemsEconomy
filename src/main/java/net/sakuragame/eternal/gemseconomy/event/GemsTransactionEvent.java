package net.sakuragame.eternal.gemseconomy.event;

import lombok.Getter;
import net.sakuragame.eternal.gemseconomy.account.Account;
import net.sakuragame.eternal.gemseconomy.currency.Currency;
import net.sakuragame.eternal.gemseconomy.utils.TranactionType;

public class GemsTransactionEvent {

    @Getter
    public static class Pre extends JustEvent {

        private final double amount;
        private final TranactionType type;

        public Pre(Account account, Currency currency, double amount, TranactionType type) {
            super(account, currency);
            this.amount = amount;
            this.type = type;
        }

        public String getAmountFormatted(){
            return getCurrency().format(getAmount());
        }
    }

    @Getter
    public static class Post extends JustEvent {

        private final TranactionType type;

        public Post(Account account, Currency currency, TranactionType type) {
            super(account, currency);
            this.type = type;
        }
    }
}
