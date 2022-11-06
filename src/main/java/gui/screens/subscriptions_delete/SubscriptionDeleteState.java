package gui.screens.subscriptions_delete;

import lombok.Data;

@Data
public class SubscriptionDeleteState {
    private final String error;
    private final boolean subscriptionDeleted;
}
