package gui.screens.ratings_add;

import domain.modelo.Article;
import domain.modelo.ArticleRating;
import domain.modelo.Reader;
import domain.services.ServicesArticles;
import domain.services.ServicesRatings;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RatingsAddViewModel {

    private final ServicesRatings servicesRatings;
    private final ServicesArticles servicesArticles;
    private final ObjectProperty<RatingsAddState> state;
    private final ObservableList<ArticleRating> observableRatings;
    private final ObservableList<Article> observableArticles;

    @Inject
    public RatingsAddViewModel(ServicesRatings servicesRatings, ServicesArticles servicesArticles) {
        this.servicesRatings = servicesRatings;
        this.servicesArticles = servicesArticles;
        state = new SimpleObjectProperty<>(new RatingsAddState(null, false));
        observableRatings = FXCollections.observableArrayList();
        observableArticles = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<RatingsAddState> getState() {
        return state;
    }

    public ObservableList<ArticleRating> getObservableRatings() {
        return FXCollections.unmodifiableObservableList(observableRatings);
    }

    public ObservableList<Article> getObservableArticles() {
        return FXCollections.unmodifiableObservableList(observableArticles);
    }

    public void loadRatings(Reader reader) {
        List<ArticleRating> ratings = servicesRatings.getRatings(reader);
        if (ratings.isEmpty()) {
            state.set(new RatingsAddState("There are no ratings", false));
        } else {
            observableRatings.clear();
            observableRatings.setAll(ratings);
        }
    }

    public void loadArticles(Reader reader) {
        Either<String, List<Article>> articles = servicesArticles.getArticlesAvailableForReader(reader);
        if (articles.isRight()) {
            observableArticles.clear();
            observableArticles.setAll(articles.get());
        } else {
            state.set(new RatingsAddState(articles.getLeft(), false));
        }
    }

    public void addRating(String idReaderInput, String idArticleInput, String ratingInput) {
        try {
            int idReader = Integer.parseInt(idReaderInput);
            int idArticle = Integer.parseInt(idArticleInput);
            int rating = Integer.parseInt(ratingInput);
            Either<String, Boolean> response = servicesRatings.saveRating(idReader, idArticle, rating);
            if (response.isRight()) {
                state.set(new RatingsAddState(null, true));
            } else {
                state.set(new RatingsAddState(response.getLeft(), false));
            }
        } catch (NumberFormatException e) {
            state.set(new RatingsAddState("All fields must be simple numbers without commas", false));
        }
    }

    public void cleanState() {
        state.set(new RatingsAddState(null, false));
    }
}
