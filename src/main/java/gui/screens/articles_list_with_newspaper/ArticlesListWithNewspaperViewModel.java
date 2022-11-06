package gui.screens.articles_list_with_newspaper;

import domain.modelo.ArticleQuery2;
import domain.modelo.ArticleType;
import domain.services.ServicesArticles;
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
        List<ArticleType> articleTypes = servicesArticles.getArticleTypes();
        if (articleTypes.isEmpty()) {
            state.set(new ArticlesListWithNewspaperState("There are no article types"));
        } else {
            observableArticleTypes.clear();
            observableArticleTypes.setAll(articleTypes);
        }
    }

    public void filter(ArticleType articleType) {
        if (articleType == null) {
            state.set(new ArticlesListWithNewspaperState("Select an article type"));
        } else {
            List<ArticleQuery2> articles = servicesArticles.getArticlesByTypeWithNewspaper(articleType);
            if (articles.isEmpty()) {
                observableArticles.clear();
                state.set(new ArticlesListWithNewspaperState("There are no articles of this type"));
            } else {
                observableArticles.clear();
                observableArticles.setAll(articles);
            }
        }
    }

    public void cleanState() {
        state.set(new ArticlesListWithNewspaperState(null));
    }

}
