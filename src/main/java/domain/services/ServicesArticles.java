package domain.services;

import domain.modelo.*;
import io.vavr.control.Either;

import java.util.List;

public interface ServicesArticles {
    List<Article> getArticles();

    List<Article> getArticlesByType(int typeId);

    List<Article> getArticlesAvailableForReader(Reader reader);

    List<ArticleType> getArticleTypes();

    Either<String, Boolean> saveArticle(Article article);

    Either<String, ArticleQuery1> getArticleInfo(int id);

    List<ArticleQuery2> getArticlesByTypeWithNewspaper(ArticleType articleType);

    List<ArticleQuery3> getArticlesByNewspaperWithBadRatings(Newspaper newspaper);
}
