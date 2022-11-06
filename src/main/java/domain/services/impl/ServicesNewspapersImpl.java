package domain.services.impl;

import dao.ArticlesDao;
import dao.NewspapersDao;
import dao.RatingsDao;
import dao.SubscriptionsDao;
import domain.modelo.Newspaper;
import domain.services.ServicesNewspapers;
import io.vavr.control.Either;
import jakarta.inject.Inject;

import java.util.List;

public class ServicesNewspapersImpl implements ServicesNewspapers {

    private final NewspapersDao daoNewspapers;
    private final RatingsDao daoRatings;
    private final SubscriptionsDao daoSubscriptions;
    private final ArticlesDao daoArticles;

    @Inject
    public ServicesNewspapersImpl(NewspapersDao daoNewspapers, RatingsDao daoRatings, SubscriptionsDao daoSubscriptions, ArticlesDao daoArticles) {
        this.daoNewspapers = daoNewspapers;
        this.daoRatings = daoRatings;
        this.daoSubscriptions = daoSubscriptions;
        this.daoArticles = daoArticles;
    }

    @Override
    public List<Newspaper> getNewspapers() {
        return daoNewspapers.getAll();
    }

    @Override
    public Newspaper get(int id) {
        return daoNewspapers.get(id);
    }

    @Override
    public Either<String, Boolean> saveNewspaper(Newspaper newspaper) {
        return daoNewspapers.save(newspaper) == 1 ? Either.right(true) : Either.left("Error saving the newspaper");
    }

    @Override
    public Either<String, Boolean> deleteNewspaper(Newspaper newspaper) {
        if (daoSubscriptions.deleteAll(newspaper) == 0) {
            if (daoRatings.deleteAll(newspaper) == 0) {
                if (daoArticles.deleteAll(newspaper) >= 0) {
                    return daoNewspapers.delete(newspaper) == 1 ? Either.right(true) : Either.left("Error deleting the newspaper");
                } else {
                    return Either.left("Error deleting the articles");
                }
            } else {
                return Either.left("Error deleting the ratings");
            }
        } else {
            return Either.left("Error deleting the subscriptions");
        }
    }

    @Override
    public boolean hasArticles(Newspaper newspaper) {
        return !daoArticles.getAll(newspaper).isEmpty();
    }

}
