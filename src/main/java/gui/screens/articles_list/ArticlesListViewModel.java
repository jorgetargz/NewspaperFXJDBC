package gui.screens.articles_list;

import domain.modelo.Article;
import domain.modelo.ArticleQuery1;
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

public class ArticlesListViewModel {

    private final ServicesArticles servicesArticles;
    private final ObjectProperty<ArticlesListState> state;
    private final ObservableList<Article> observableArticles;
    private final ObservableList<ArticleType> observableArticleTypes;

    @Inject
    public ArticlesListViewModel(ServicesArticles servicesArticles) {
        this.servicesArticles = servicesArticles;
        state = new SimpleObjectProperty<>(new ArticlesListState(null));
        observableArticles = FXCollections.observableArrayList();
        observableArticleTypes = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<ArticlesListState> getState() {
        return state;
    }

    public ObservableList<Article> getObservableArticles() {
        return FXCollections.unmodifiableObservableList(observableArticles);
    }

    public ObservableList<ArticleType> getObservableArticleTypes() {
        return FXCollections.unmodifiableObservableList(observableArticleTypes);
    }

    public void loadArticles() {
        Either<String, List<Article>> response = servicesArticles.getArticles();
        if (response.isRight()) {
            observableArticles.clear();
            observableArticles.setAll(response.get());
        } else {
            state.set(new ArticlesListState(response.getLeft()));
        }
    }

    public void loadArticleTypes() {
        Either<String, List<ArticleType>> response = servicesArticles.getArticleTypes();
        if (response.isRight()) {
            observableArticleTypes.clear();
            observableArticleTypes.setAll(response.get());
        } else {
            state.set(new ArticlesListState(response.getLeft()));
        }
    }

    public void filter(ArticleType articleType) {
        if (articleType == null) {
            state.set(new ArticlesListState("Select an article type"));
        } else {
            Either<String, List<Article>> response = servicesArticles.getArticlesByType(articleType);
            if (response.isRight()) {
                observableArticles.clear();
                observableArticles.setAll(response.get());
            } else {
                state.set(new ArticlesListState(response.getLeft()));
            }
        }
    }

    public void cleanState() {
        state.set(new ArticlesListState(null));
    }

    public ArticleQuery1 getArticleQuery(int id) {
        Either<String, ArticleQuery1> result = servicesArticles.getArticleInfo(id);
        if (result.isLeft()) {
            state.set(new ArticlesListState(result.getLeft()));
            return null;
        } else {
            return result.get();
        }
    }
}
