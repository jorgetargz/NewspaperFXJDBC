package gui.screens.articles_add;

import domain.modelo.Article;
import domain.modelo.ArticleType;
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

public class ArticlesAddViewModel {
    private final ServicesArticles servicesArticles;
    private final ServicesNewspapers servicesNewspapers;
    private final ObjectProperty<ArticlesAddState> state;
    private final ObservableList<Article> observableArticles;
    private final ObservableList<ArticleType> observableArticleTypes;
    private final ObservableList<Newspaper> observableNewspapers;

    @Inject
    public ArticlesAddViewModel(ServicesArticles servicesArticles, ServicesNewspapers servicesNewspapers) {
        this.servicesArticles = servicesArticles;
        this.servicesNewspapers = servicesNewspapers;
        state = new SimpleObjectProperty<>(new ArticlesAddState(null, false));
        observableArticles = FXCollections.observableArrayList();
        observableArticleTypes = FXCollections.observableArrayList();
        observableNewspapers = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<ArticlesAddState> getState() {
        return state;
    }

    public ObservableList<Article> getObservableArticles() {
        return FXCollections.unmodifiableObservableList(observableArticles);
    }

    public ObservableList<ArticleType> getObservableArticleTypes() {
        return FXCollections.unmodifiableObservableList(observableArticleTypes);
    }

    public ObservableList<Newspaper> getObservableNewspapers() {
        return FXCollections.unmodifiableObservableList(observableNewspapers);
    }

    public void loadArticles() {
        Either<String, List<Article>> response = servicesArticles.getArticles();
        if (response.isRight()) {
            observableArticles.clear();
            observableArticles.setAll(response.get());
        } else {
            state.set(new ArticlesAddState(response.getLeft(), false));
        }
    }

    public void loadArticleTypes() {
        Either<String, List<ArticleType>> response = servicesArticles.getArticleTypes();
        if (response.isRight()) {
            observableArticleTypes.clear();
            observableArticleTypes.setAll(response.get());
        } else {
            state.set(new ArticlesAddState(response.getLeft(), false));
        }
    }

    public void loadNewspapers() {
        Either<String, List<Newspaper>> response = servicesNewspapers.getNewspapers();
        if (response.isRight()) {
            observableNewspapers.clear();
            observableNewspapers.setAll(response.get());
        } else {
            state.set(new ArticlesAddState(response.getLeft(), false));
        }
    }

    public void addArticle(String nameText, ArticleType articleType, Newspaper newspaper) {
        if (nameText.isEmpty() || articleType == null || newspaper == null) {
            state.set(new ArticlesAddState("Please fill all the fields", false));
        } else {
            Article article = new Article(nameText, articleType, newspaper.getId());
            Either<String, Boolean> result = servicesArticles.saveArticle(article);
            if (result.isRight()) {
                state.set(new ArticlesAddState(null, true));
                loadArticles();
            } else {
                state.set(new ArticlesAddState(result.getLeft(), false));
            }
        }
    }

    public void cleanState() {
        state.set(new ArticlesAddState(null, false));
    }
}
