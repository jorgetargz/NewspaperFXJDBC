package domain.services.impl;

import dao.*;
import domain.modelo.*;
import domain.services.ServicesRatings;
import io.vavr.control.Either;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ServicesRatingsImpl implements ServicesRatings {

    private final RatingsDao daoRatings;
    private final ArticlesDao daoArticles;
    private final ReadersDao daoReaders;
    private final NewspapersDao daoNewspapers;
    private final SubscriptionsDao daoSubscriptions;

    @Inject
    public ServicesRatingsImpl(RatingsDao daoRatings, ArticlesDao daoArticles, ReadersDao daoReaders, NewspapersDao daoNewspapers, SubscriptionsDao daoSubscriptions) {
        this.daoRatings = daoRatings;
        this.daoArticles = daoArticles;
        this.daoReaders = daoReaders;
        this.daoNewspapers = daoNewspapers;
        this.daoSubscriptions = daoSubscriptions;
    }

    @Override
    public List<ArticleRating> getRatings() {
        return daoRatings.getAll();
    }

    @Override
    public List<ArticleRating> getRatings(Reader reader) {
        return daoRatings.getAll(reader);
    }


    @Override
    public Either<String, Boolean> saveRating(int idReader, int idArticle, int rating) {
        Reader reader = daoReaders.get(idReader);
        if (reader == null) {
            return Either.left("Reader not found");
        }
        Article article = daoArticles.get(idArticle).get();
        if (article == null) {
            return Either.left("Article not found");
        }
        Newspaper newspaper = daoNewspapers.get(article.getNewspaperId()).get();
        if (newspaper == null) {
            return Either.left("Newspaper not found");
        }
        Subscription subscription = daoSubscriptions.get(newspaper, reader);
        if (subscription == null) {
            return Either.left("You are not subscribed to this newspaper");
        } else if (subscription.getCancellationDate() != null && subscription.getCancellationDate().isBefore(LocalDate.now().plus(1, ChronoUnit.DAYS))) {
            return Either.left("You have canceled your subscription to this newspaper");
        } else if (subscription.getSigningDate().isAfter(LocalDate.now())) {
            return Either.left("You have not yet started your subscription to this newspaper");
        } else if (rating < 0 || rating > 10) {
            return Either.left("Rating must be between 0 and 10");
        } else {
            int result = daoRatings.save(idReader, idArticle, rating);
            return switch (result) {
                case 0 -> Either.right(true);
                case -1 -> Either.left("Error saving the rating");
                case -4 -> Either.left("Rating already exists");
                default -> Either.left("Unknown error");
            };
        }
    }

}
