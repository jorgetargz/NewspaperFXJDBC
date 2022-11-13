package gui.screens.articles_list_with_bad_rating;

import domain.modelo.ArticleQuery3;
import domain.modelo.Newspaper;
import domain.services.ServicesArticles;
import domain.services.ServicesNewspapers;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ArticlesListWithBadRatingViewModel {

    private final ServicesArticles servicesArticles;
    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<ArticlesListWithBadRatingState> state;
    private final ObservableList<ArticleQuery3> observableArticles;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public ArticlesListWithBadRatingViewModel(ServicesArticles servicesArticles, ServicesNewspapers servicesNewspapers) {
        this.servicesArticles = servicesArticles;
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new ArticlesListWithBadRatingState(null));
        observableArticles = FXCollections.observableArrayList();
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<ArticlesListWithBadRatingState> getState() {
        return state;
    }

    public ObservableList<ArticleQuery3> getObservableArticles() {
        return FXCollections.unmodifiableObservableList(observableArticles);
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
            state.set(new ArticlesListWithBadRatingState(response.getLeft()));
        }
    }

    public void filter(Newspaper newspaper) {
        if (newspaper == null) {
            state.set(new ArticlesListWithBadRatingState("Select a newspaper"));
        } else {
            Either<String, List<ArticleQuery3>> response = servicesArticles.getArticlesByNewspaperWithBadRatings(newspaper);
            if (response.isRight()) {
                observableArticles.clear();
                observableArticles.setAll(response.get());
            } else {
                state.set(new ArticlesListWithBadRatingState(response.getLeft()));
            }
        }
    }

    public void cleanState() {
        state.set(new ArticlesListWithBadRatingState(null));
    }

}
