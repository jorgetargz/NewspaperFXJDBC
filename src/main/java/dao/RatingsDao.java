package dao;

import domain.modelo.ArticleRating;
import domain.modelo.Reader;

import java.util.List;

public interface RatingsDao {
    List<ArticleRating> getAll();

    List<ArticleRating> getAll(Reader reader);

    ArticleRating get(ArticleRating articleRating);

    int save(int idReader, int idArticle, int rating);

    int delete(ArticleRating articleRating);

}
