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
        List<Article> articles = servicesArticles.getArticles();
        if (articles.isEmpty()) {
            state.set(new ArticlesListState("There are no articles"));
        } else {
            observableArticles.clear();
            observableArticles.setAll(articles);
        }
    }

    public void loadArticleTypes() {
        List<ArticleType> articleTypes = servicesArticles.getArticleTypes();
        if (articleTypes.isEmpty()) {
            state.set(new ArticlesListState("There are no article types"));
        } else {
            observableArticleTypes.clear();
            observableArticleTypes.setAll(articleTypes);
        }
    }

    public void filter(ArticleType articleType) {
        if (articleType == null) {
            state.set(new ArticlesListState("Select an article type"));
        } else {
            List<Article> articles = servicesArticles.getArticlesByType(articleType.getId());
            if (articles.isEmpty()) {
                observableArticles.clear();
                state.set(new ArticlesListState("There are no articles of this type"));
            } else {
                observableArticles.clear();
                observableArticles.setAll(articles);
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
