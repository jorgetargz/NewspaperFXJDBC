package gui.screens.readers_delete;

import domain.modelo.Reader;
import domain.services.ServicesReaders;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ReadersDeleteViewModel {

    private final ServicesReaders servicesReaders;
    private final ObjectProperty<ReadersDeleteState> state;
    private final ObservableList<Reader> observableReaders;

    @Inject
    public ReadersDeleteViewModel(ServicesReaders servicesReaders) {
        this.servicesReaders = servicesReaders;
        state = new SimpleObjectProperty<>(new ReadersDeleteState(null, false));
        observableReaders = FXCollections.observableArrayList();
    }

    public ObjectProperty<ReadersDeleteState> getState() {
        return state;
    }

    public ObservableList<Reader> getObservableReaders() {
        return FXCollections.unmodifiableObservableList(observableReaders);
    }

    public void loadReaders() {
        List<Reader> readers = servicesReaders.getReaders();
        if (readers.isEmpty()) {
            state.set(new ReadersDeleteState("There are no readers", false));
        } else {
            observableReaders.clear();
            observableReaders.setAll(readers);
        }
    }

    public void deleteReader(Reader reader) {
        if (reader == null) {
            state.set(new ReadersDeleteState("Select a reader", false));
        } else {
            state.set(new ReadersDeleteState(null, true));
        }

    }

    public void deleteReaderConfirm(Reader reader) {
        if (servicesReaders.deleteReader(reader).isRight()) {
            observableReaders.remove(reader);
            state.set(new ReadersDeleteState("Reader deleted", false));
        } else {
            state.set(new ReadersDeleteState("Error deleting reader", false));
        }
    }

    public void cleanState() {
        state.set(new ReadersDeleteState(null, false));
    }
}
