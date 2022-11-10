package gui.screens.subscriptions_list;

import domain.modelo.Newspaper;
import domain.modelo.Subscription;
import domain.services.ServicesNewspapers;
import domain.services.ServicesSubscriptions;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SubscriptionListViewModel {

    private final ServicesSubscriptions servicesSubscriptions;
    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<SubscriptionListState> state;
    private final ObservableList<Subscription> observableSubscriptions;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public SubscriptionListViewModel(ServicesSubscriptions servicesSubscriptions, ServicesNewspapers servicesNewspapers) {
        this.servicesSubscriptions = servicesSubscriptions;
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new SubscriptionListState(null));
        observableSubscriptions = FXCollections.observableArrayList();
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<SubscriptionListState> getState() {
        return state;
    }

    public ObservableList<Subscription> getObservableSubscriptions() {
        return FXCollections.unmodifiableObservableList(observableSubscriptions);
    }

    public ObservableList<Newspaper> getObservableNewspapers() {
        return FXCollections.unmodifiableObservableList(observableNewspapers);
    }

    public void loadSubscriptions() {
        List<Subscription> subscriptions = servicesSubscriptions.getSubscriptions();
        if (subscriptions.isEmpty()) {
            state.set(new SubscriptionListState("There are no subscriptions"));
        } else {
            observableSubscriptions.clear();
            observableSubscriptions.setAll(subscriptions);
        }
    }

    public void loadNewspapers() {
        List<Newspaper> newspapers = servicesNewspapers.getNewspapers();
        if (newspapers.isEmpty()) {
            state.set(new SubscriptionListState("There are no newspapers"));
        } else {
            observableNewspapers.clear();
            observableNewspapers.setAll(newspapers);
        }
    }

    public void loadSubscriptionsByNewspaper(Newspaper newspaper) {
        if (newspaper == null) {
            state.set(new SubscriptionListState("Select a newspaper"));
        } else {
            List<Subscription> subscriptions = servicesSubscriptions.getSubscriptionsByNewspaper(newspaper);
            if (subscriptions.isEmpty()) {
                state.set(new SubscriptionListState("There are no subscriptions for this newspaper"));
            } else {
                observableSubscriptions.clear();
                observableSubscriptions.setAll(subscriptions);
            }
        }
    }

    public void loadOldestSubscriptionsByNewspaper(Newspaper newspaper) {
        if (newspaper == null) {
            state.set(new SubscriptionListState("Please select a newspaper"));
        } else {
            List<Subscription> subscriptions = servicesSubscriptions.getOldestSubscriptionsByNewspaper(newspaper);
            if (subscriptions.isEmpty()) {
                state.set(new SubscriptionListState("There are no subscriptions for this newspaper"));
            } else {
                observableSubscriptions.clear();
                observableSubscriptions.setAll(subscriptions);
            }
        }
    }

    public void cleanState() {
        state.set(new SubscriptionListState(null));
    }
}
