package domain.services;

import domain.modelo.ArticleRating;
import domain.modelo.Reader;
import io.vavr.control.Either;

import java.util.List;

public interface ServicesRatings {
    List<ArticleRating> getRatings();

    List<ArticleRating> getRatings(Reader reader);

    Either<String, Boolean> saveRating(int idReader, int idArticle, int rating);
}
