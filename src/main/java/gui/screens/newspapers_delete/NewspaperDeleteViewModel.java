package gui.screens.newspapers_delete;

import domain.modelo.Newspaper;
import domain.services.ServicesNewspapers;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class NewspaperDeleteViewModel {

    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<NewspaperDeleteState> state;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public NewspaperDeleteViewModel(ServicesNewspapers servicesNewspapers) {
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new NewspaperDeleteState(null, false));
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<NewspaperDeleteState> getState() {
        return state;
    }

    public ObservableList<Newspaper> getObservableNewspapers() {
        return FXCollections.unmodifiableObservableList(observableNewspapers);
    }

    public void loadNewspapers() {
        Either<String, List<Newspaper>> response = servicesNewspapers.getNewspapers();
        if (response.isRight()) {
            observableNewspapers.clear();
            observableNewspapers.setAll(response.get());
        } else {
            state.set(new NewspaperDeleteState(response.getLeft(), false));
        }
    }

    public void deleteNewspaper(Newspaper newspaper) {
        if (newspaper == null) {
            state.set(new NewspaperDeleteState("Select a newspaper", false));
        } else {
            if (servicesNewspapers.hasArticles(newspaper)) {
                state.set(new NewspaperDeleteState(null, true));
            } else {
                deleteNewspaperConfirmed(newspaper);
            }
        }
    }

    public void deleteNewspaperConfirmed(Newspaper newspaper) {
        Either<String, Boolean> resultNewspaperDelete = servicesNewspapers.deleteNewspaper(newspaper);
        if (resultNewspaperDelete.isRight()) {
            loadNewspapers();
        } else {
            state.set(new NewspaperDeleteState(resultNewspaperDelete.getLeft(), false));
        }
    }

    public void cleanState() {
        state.set(new NewspaperDeleteState(null, false));
    }
}
