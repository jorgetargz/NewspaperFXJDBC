package gui.screens.subscriptions_cancel;

import domain.modelo.Reader;
import domain.modelo.Subscription;
import domain.services.ServicesSubscriptions;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SubscriptionCancelViewModel {

    private final ServicesSubscriptions servicesSubscriptions;
    private final ObjectProperty<SubscriptionCancelState> state;
    private final ObservableList<Subscription> observableSubscriptions;

    @Inject
    public SubscriptionCancelViewModel(ServicesSubscriptions servicesSubscriptions) {
        this.servicesSubscriptions = servicesSubscriptions;
        state = new SimpleObjectProperty<>(new SubscriptionCancelState(null, false));
        observableSubscriptions = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<SubscriptionCancelState> getState() {
        return state;
    }

    public ObservableList<Subscription> getObservableSubscriptions() {
        return FXCollections.unmodifiableObservableList(observableSubscriptions);
    }

    public void loadSubscriptions() {
        List<Subscription> subscriptions = servicesSubscriptions.getSubscriptions();
        if (subscriptions.isEmpty()) {
            state.set(new SubscriptionCancelState("There are no subscriptions", false));
        } else {
            observableSubscriptions.clear();
            observableSubscriptions.setAll(subscriptions);
        }
    }


    public void loadSubscriptions(Reader reader) {
        List<Subscription> subscriptions = servicesSubscriptions.getSubscriptionsByReader(reader);
        if (subscriptions.isEmpty()) {
            observableSubscriptions.clear();
            state.set(new SubscriptionCancelState("There are no subscriptions", false));
        } else {
            observableSubscriptions.clear();
            observableSubscriptions.setAll(subscriptions);
        }
    }


    public void cleanState() {
        state.set(new SubscriptionCancelState(null, false));
    }

    public void cancelSubscription(Subscription subscription) {
        Either<String, Boolean> result = servicesSubscriptions.cancelSubscription(subscription);
        if (result.isLeft()) {
            state.set(new SubscriptionCancelState(result.getLeft(), false));
        } else {
            state.set(new SubscriptionCancelState(null, true));
            loadSubscriptions(subscription.getReader());
        }
    }
}
