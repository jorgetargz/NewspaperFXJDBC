package gui.screens.newspapers_add;

import domain.modelo.Newspaper;
import domain.services.ServicesNewspapers;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class NewspaperAddViewModel {

    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<NewspaperAddState> state;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public NewspaperAddViewModel(ServicesNewspapers servicesNewspapers) {
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new NewspaperAddState(null, false));
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<NewspaperAddState> getState() {
        return state;
    }

    public ObservableList<Newspaper> getObservableNewspapers() {
        return FXCollections.unmodifiableObservableList(observableNewspapers);
    }

    public void loadNewspapers() {
        Either<String,List<Newspaper>> response = servicesNewspapers.getNewspapers();
        if (response.isRight()) {
            observableNewspapers.clear();
            observableNewspapers.setAll(response.get());
        } else {
            state.set(new NewspaperAddState(response.getLeft(), false));
        }
    }

    public void cleanState() {
        state.set(new NewspaperAddState(null, false));
    }

    public void addNewspaper(String name, LocalDate releaseDate) {
        Newspaper newspaper = new Newspaper(name, releaseDate);
        Either<String, Boolean> result = servicesNewspapers.saveNewspaper(newspaper);
        if (result.isLeft()) {
            state.set(new NewspaperAddState(result.getLeft(), false));
        } else {
            loadNewspapers();
            state.set(new NewspaperAddState(null, true));
        }
    }
}
