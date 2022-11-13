package gui.screens.articles_list_with_newspaper;

import domain.modelo.ArticleQuery2;
import domain.modelo.ArticleType;
import domain.services.ServicesArticles;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ArticlesListWithNewspaperViewModel {

    private final ServicesArticles servicesArticles;
    private final ObjectProperty<ArticlesListWithNewspaperState> state;
    private final ObservableList<ArticleQuery2> observableArticles;
    private final ObservableList<ArticleType> observableArticleTypes;

    @Inject
    public ArticlesListWithNewspaperViewModel(ServicesArticles servicesArticles) {
        this.servicesArticles = servicesArticles;
        state = new SimpleObjectProperty<>(new ArticlesListWithNewspaperState(null));
        observableArticles = FXCollections.observableArrayList();
        observableArticleTypes = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<ArticlesListWithNewspaperState> getState() {
        return state;
    }

    public ObservableList<ArticleQuery2> getObservableArticles() {
        return FXCollections.unmodifiableObservableList(observableArticles);
    }

    public ObservableList<ArticleType> getObservableArticleTypes() {
        return FXCollections.unmodifiableObservableList(observableArticleTypes);
    }

    public void loadArticleTypes() {
        Either<String,List<ArticleType>> response = servicesArticles.getArticleTypes();
        if (response.isRight()) {
            observableArticleTypes.clear();
            observableArticleTypes.setAll(response.get());
        } else {
            state.set(new ArticlesListWithNewspaperState(response.getLeft()));
        }
    }

    public void filter(ArticleType articleType) {
        if (articleType == null) {
            state.set(new ArticlesListWithNewspaperState("Select an article type"));
        } else {
            Either<String,List<ArticleQuery2>> response = servicesArticles.getArticlesByTypeWithNewspaper(articleType);
            if (response.isRight()) {
                observableArticles.clear();
                observableArticles.setAll(response.get());
            } else {
                state.set(new ArticlesListWithNewspaperState(response.getLeft()));
            }
        }
    }

    public void cleanState() {
        state.set(new ArticlesListWithNewspaperState(null));
    }

}
