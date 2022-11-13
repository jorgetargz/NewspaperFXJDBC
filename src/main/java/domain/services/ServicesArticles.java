package domain.services;

import domain.modelo.*;
import io.vavr.control.Either;

import java.util.List;

public interface ServicesArticles {
    Either<String, List<Article>> getArticles();

    Either<String, List<Article>> getArticlesByType(ArticleType typeId);

    Either<String, List<Article>> getArticlesAvailableForReader(Reader reader);

    Either<String, List<ArticleType>> getArticleTypes();

    Either<String, Boolean> saveArticle(Article article);

    Either<String, ArticleQuery1> getArticleInfo(int id);

    Either<String, List<ArticleQuery2>> getArticlesByTypeWithNewspaper(ArticleType articleType);

    Either<String, List<ArticleQuery3>> getArticlesByNewspaperWithBadRatings(Newspaper newspaper);
}
