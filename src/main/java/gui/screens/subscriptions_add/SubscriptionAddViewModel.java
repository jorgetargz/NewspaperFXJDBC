package gui.screens.subscriptions_add;

import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.modelo.Subscription;
import domain.services.ServicesNewspapers;
import domain.services.ServicesSubscriptions;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class SubscriptionAddViewModel {

    private final ServicesSubscriptions servicesSubscriptions;
    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<SubscriptionAddState> state;
    private final ObservableList<Subscription> observableSubscriptions;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public SubscriptionAddViewModel(ServicesSubscriptions servicesSubscriptions, ServicesNewspapers servicesNewspapers) {
        this.servicesSubscriptions = servicesSubscriptions;
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new SubscriptionAddState(null, false));
        observableSubscriptions = FXCollections.observableArrayList();
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<SubscriptionAddState> getState() {
        return state;
    }

    public ObservableList<Subscription> getObservableSubscriptions() {
        return FXCollections.unmodifiableObservableList(observableSubscriptions);
    }

    public ObservableList<Newspaper> getObservableNewspapers() {
        return FXCollections.unmodifiableObservableList(observableNewspapers);
    }

    public void loadNewspapers() {
        Either<String, List<Newspaper>> newspapers = servicesNewspapers.getNewspapers();
        if (newspapers.isRight()) {
            observableNewspapers.clear();
            observableNewspapers.setAll(newspapers.get());
        } else {
            state.set(new SubscriptionAddState(newspapers.getLeft(), false));
        }
    }

    public void loadSubscriptions() {
        List<Subscription> subscriptions = servicesSubscriptions.getSubscriptions();
        if (subscriptions.isEmpty()) {
            state.set(new SubscriptionAddState("There are no subscriptions", false));
        } else {
            observableSubscriptions.clear();
            observableSubscriptions.setAll(subscriptions);
        }
    }

    public void loadSubscriptions(Reader reader) {
        List<Subscription> subscriptions = servicesSubscriptions.getSubscriptionsByReader(reader);
        if (subscriptions.isEmpty()) {
            observableSubscriptions.clear();
            state.set(new SubscriptionAddState("There are no subscriptions", false));
        } else {
            observableSubscriptions.clear();
            observableSubscriptions.setAll(subscriptions);
        }
    }

    public void addSubscription(int idReader, int idNewspaper, LocalDate signingDate) {
        Either<String, Boolean> result = servicesSubscriptions.addSubscription(idReader, idNewspaper, signingDate);
        if (result.isLeft()) {
            state.set(new SubscriptionAddState(result.getLeft(), false));
        } else {
            state.set(new SubscriptionAddState(null, true));
        }
    }

    public void cleanState() {
        state.set(new SubscriptionAddState(null, false));
    }
}
