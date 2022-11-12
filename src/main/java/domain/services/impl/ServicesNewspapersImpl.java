package domain.services.impl;

import dao.ArticlesDao;
import dao.NewspapersDao;
import domain.modelo.Newspaper;
import domain.services.ServicesNewspapers;
import io.vavr.control.Either;
import jakarta.inject.Inject;

import java.util.List;

public class ServicesNewspapersImpl implements ServicesNewspapers {

    private final NewspapersDao daoNewspapers;
    private final ArticlesDao daoArticles;

    @Inject
    public ServicesNewspapersImpl(NewspapersDao daoNewspapers, ArticlesDao daoArticles) {
        this.daoNewspapers = daoNewspapers;
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
        return switch (daoNewspapers.delete(newspaper)) {
            case 0 -> Either.right(true);
            case -1 - 4 -> Either.left("SQL error deleting the newspaper");
            default -> Either.left("Error deleting the newspaper");
        };
    }

    @Override
    public boolean hasArticles(Newspaper newspaper) {
        return !daoArticles.getAll(newspaper).isEmpty();
    }

}
