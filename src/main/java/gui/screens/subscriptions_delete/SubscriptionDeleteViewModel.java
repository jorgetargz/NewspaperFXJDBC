package gui.screens.subscriptions_delete;

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

public class SubscriptionDeleteViewModel {

    private final ServicesSubscriptions servicesSubscriptions;
    private final ObjectProperty<SubscriptionDeleteState> state;
    private final ObservableList<Subscription> observableSubscriptions;

    @Inject
    public SubscriptionDeleteViewModel(ServicesSubscriptions servicesSubscriptions) {
        this.servicesSubscriptions = servicesSubscriptions;
        state = new SimpleObjectProperty<>(new SubscriptionDeleteState(null, false));
        observableSubscriptions = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<SubscriptionDeleteState> getState() {
        return state;
    }

    public ObservableList<Subscription> getObservableSubscriptions() {
        return FXCollections.unmodifiableObservableList(observableSubscriptions);
    }

    public void loadSubscriptions() {
        List<Subscription> subscriptions = servicesSubscriptions.getSubscriptions();
        if (subscriptions.isEmpty()) {
            state.set(new SubscriptionDeleteState("There are no subscriptions", false));
        } else {
            observableSubscriptions.clear();
            observableSubscriptions.setAll(subscriptions);
        }
    }

    public void deleteSubscription(Subscription subscription) {
        Either<String, Boolean> result = servicesSubscriptions.deleteSubscription(subscription);
        if (result.isLeft()) {
            state.set(new SubscriptionDeleteState(result.getLeft(), false));
        } else {
            state.set(new SubscriptionDeleteState(null, true));
            loadSubscriptions();
        }
    }

    public void cleanState() {
        state.set(new SubscriptionDeleteState(null, false));
    }
}
