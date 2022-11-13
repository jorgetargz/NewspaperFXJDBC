package gui.screens.readers_list;


import domain.modelo.ArticleType;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.services.ServicesArticles;
import domain.services.ServicesNewspapers;
import domain.services.ServicesReaders;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ReadersListViewModel {

    private final ServicesReaders servicesReaders;
    private final ServicesArticles servicesArticles;
    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<ReadersListState> state;
    private final ObservableList<Reader> observableReaders;
    private final ObservableList<ArticleType> observableArticleTypes;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public ReadersListViewModel(ServicesReaders servicesReaders, ServicesArticles servicesArticles, ServicesNewspapers servicesNewspapers) {
        this.servicesReaders = servicesReaders;
        this.servicesArticles = servicesArticles;
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new ReadersListState(null));
        observableReaders = FXCollections.observableArrayList();
        observableArticleTypes = FXCollections.observableArrayList();
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ObjectProperty<ReadersListState> getState() {
        return state;
    }

    public ObservableList<Reader> getObservableReaders() {
        return FXCollections.unmodifiableObservableList(observableReaders);
    }

    public ObservableList<ArticleType> getObservableArticleTypes() {
        return FXCollections.unmodifiableObservableList(observableArticleTypes);
    }

    public ObservableList<Newspaper> getObservableNewspapers() {
        return FXCollections.unmodifiableObservableList(observableNewspapers);
    }

    public void loadReaders() {
        List<Reader> readers = servicesReaders.getReaders();
        if (readers.isEmpty()) {
            state.set(new ReadersListState("There are no readers"));
        } else {
            observableReaders.clear();
            observableReaders.setAll(readers);
        }
    }

    public void loadArticleTypes() {
        Either<String, List<ArticleType>> articleTypes = servicesArticles.getArticleTypes();
        if (articleTypes.isRight()) {
            observableArticleTypes.clear();
            observableArticleTypes.setAll(articleTypes.get());
        } else {
            state.set(new ReadersListState(articleTypes.getLeft()));
        }
    }

    public void loadNewspapers() {
        Either<String, List<Newspaper>> newspapers = servicesNewspapers.getNewspapers();
        if (newspapers.isRight()) {
            observableNewspapers.clear();
            observableNewspapers.setAll(newspapers.get());
        } else {
            state.set(new ReadersListState(newspapers.getLeft()));
        }
    }

    public void filterByArticleType(ArticleType articleType) {
        if (articleType == null) {
            state.set(new ReadersListState("Select an article type"));
        } else {
            List<Reader> readers = servicesReaders.getReadersByArticleType(articleType);
            if (readers.isEmpty()) {
                observableReaders.clear();
                state.set(new ReadersListState("There are no readers reading this article type"));
            } else {
                observableReaders.clear();
                observableReaders.setAll(readers);
            }
        }
    }

    public void filterByNewspaper(Newspaper newspaper) {
        if (newspaper == null) {
            state.set(new ReadersListState("Select a newspaper"));
        } else {
            List<Reader> readers = servicesReaders.getReadersByNewspaper(newspaper);
            if (readers.isEmpty()) {
                observableReaders.clear();
                state.set(new ReadersListState("There are no readers reading this newspaper"));
            } else {
                observableReaders.clear();
                observableReaders.setAll(readers);
            }
        }
    }

    public void cleanState() {
        state.set(new ReadersListState(null));
    }

}
