package gui.screens.newspapers_list;

import domain.modelo.Newspaper;
import domain.services.ServicesNewspapers;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class NewspaperListViewModel {

    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<NewspaperListState> state;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public NewspaperListViewModel(ServicesNewspapers servicesNewspapers) {
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new NewspaperListState(null));
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<NewspaperListState> getState() {
        return state;
    }

    public ObservableList<Newspaper> getObservableNewspapers() {
        return FXCollections.unmodifiableObservableList(observableNewspapers);
    }

    public void loadNewspapers() {
        List<Newspaper> newspapers = servicesNewspapers.getNewspapers();
        if (newspapers.isEmpty()) {
            state.set(new NewspaperListState("There are no newspapers"));
        } else {
            observableNewspapers.clear();
            observableNewspapers.setAll(newspapers);
        }
    }

    public void cleanState() {
        state.set(new NewspaperListState(null));
    }
}
