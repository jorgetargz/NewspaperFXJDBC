package dao;

import domain.modelo.*;
import io.vavr.control.Either;

import java.util.List;

public interface ArticlesDao {
    Either<Integer, List<Article>> getAll();

    Either<Integer, List<Article>> getAll(Newspaper newspaper);

    Either<Integer, List<Article>> getAll(ArticleType articleType);

    Either<Integer, List<Article>> getAll(Reader reader);

    Either<Integer, Article> get(int id);

    int save(Article article);

    int delete(Article article);

    ArticleQuery1 getArticleQuery1(int id);

    Either<Integer, List<ArticleQuery2>> getArticlesQuery2(ArticleType articleType);

    Either<Integer, List<ArticleQuery3>> getArticlesQuery3(Newspaper newspaper);
}
