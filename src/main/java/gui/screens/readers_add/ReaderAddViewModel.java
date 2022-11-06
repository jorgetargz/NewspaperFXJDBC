package gui.screens.readers_add;

import domain.modelo.Login;
import domain.modelo.Reader;
import domain.services.ServicesReaders;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class ReaderAddViewModel {

    private final ServicesReaders servicesReaders;
    private final ObjectProperty<ReaderAddState> state;
    private final ObservableList<Reader> observableReaders;

    @Inject
    public ReaderAddViewModel(ServicesReaders servicesReaders) {
        this.servicesReaders = servicesReaders;
        state = new SimpleObjectProperty<>(new ReaderAddState(null, false));
        observableReaders = FXCollections.observableArrayList();
    }

    public ObjectProperty<ReaderAddState> getState() {
        return state;
    }

    public ObservableList<Reader> getObservableReaders() {
        return FXCollections.unmodifiableObservableList(observableReaders);
    }

    public void loadReaders() {
        List<Reader> readers = servicesReaders.getReaders();
        if (readers.isEmpty()) {
            state.set(new ReaderAddState("There are no readers", false));
        } else {
            observableReaders.clear();
            observableReaders.setAll(readers);
        }
    }


    public void addReader(String nameInput, LocalDate birthdayInput, String usernameInput, String passwordInput) {
        if (nameInput.isEmpty() || birthdayInput == null || usernameInput.isEmpty() || passwordInput.isEmpty()) {
            state.set(new ReaderAddState("All fields are required", false));
        } else {
            Reader reader = new Reader(nameInput, birthdayInput, new Login(usernameInput, passwordInput));
            Either<String, Boolean> result = servicesReaders.saveReader(reader);
            if (result.isRight()) {
                observableReaders.add(reader);
                state.set(new ReaderAddState(null, true));
            } else {
                state.set(new ReaderAddState("Reader could not be added", false));
            }
        }
    }

    public void cleanState() {
        state.set(new ReaderAddState(null, false));
    }
}

