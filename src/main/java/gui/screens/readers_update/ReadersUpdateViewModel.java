package gui.screens.readers_update;

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

public class ReadersUpdateViewModel {

    private final ServicesReaders servicesReaders;
    private final ObjectProperty<ReadersUpdateState> state;
    private final ObservableList<Reader> observableReaders;

    @Inject
    public ReadersUpdateViewModel(ServicesReaders servicesReaders) {
        this.servicesReaders = servicesReaders;
        state = new SimpleObjectProperty<>(new ReadersUpdateState(null, false));
        observableReaders = FXCollections.observableArrayList();
    }

    public ObjectProperty<ReadersUpdateState> getState() {
        return state;
    }

    public ObservableList<Reader> getObservableReaders() {
        return FXCollections.unmodifiableObservableList(observableReaders);
    }

    public void loadReaders() {
        List<Reader> readers = servicesReaders.getReaders();
        if (readers.isEmpty()) {
            state.set(new ReadersUpdateState("There are no readers", false));
        } else {
            observableReaders.clear();
            observableReaders.setAll(readers);
        }
    }


    public void updateReader(Reader reader, String nameInput, LocalDate birthdayInput, String passwordInput) {
        if (nameInput.isEmpty() || birthdayInput == null) {
            state.set(new ReadersUpdateState("All fields except password are required", false));
        } else {
            reader.setName(nameInput);
            reader.setDateOfBirth(birthdayInput);
            Either<String, Boolean> result = servicesReaders.updateReader(reader, passwordInput);
            if (result.isRight()) {
                observableReaders.removeIf(r -> r.getId() == reader.getId());
                observableReaders.add(reader);
                state.set(new ReadersUpdateState(null, true));
            } else {
                state.set(new ReadersUpdateState("Reader could not be updated", false));
            }
        }
    }

    public void cleanState() {
        state.set(new ReadersUpdateState(null, false));
    }
}
