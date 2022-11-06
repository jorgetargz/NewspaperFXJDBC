package gui.screens.subscriptions_cancel;

import lombok.Data;

@Data
public class SubscriptionCancelState {
    private final String error;
    private final boolean subscriptionCancelled;
}
