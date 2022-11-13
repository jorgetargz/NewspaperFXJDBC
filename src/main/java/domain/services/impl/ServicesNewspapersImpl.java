package domain.services.impl;

import common.Constantes;
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
    public Either<String, List<Newspaper>> getNewspapers() {
        Either<Integer, List<Newspaper>> result = daoNewspapers.getAll();
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.NO_NEWSPAPERS_FOUND);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }

    @Override
    public Either<String, Newspaper> get(int id) {
        Either<Integer, Newspaper> result = daoNewspapers.get(id);
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.NEWSPAPER_NOT_FOUND);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }

    @Override
    public Either<String, Boolean> saveNewspaper(Newspaper newspaper) {
        return switch (daoNewspapers.save(newspaper)) {
            case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
            case Constantes.ALREADY_EXISTS_CODE -> Either.left(Constantes.NEWSPAPER_ALREADY_EXISTS);
            case Constantes.ONE_ROW_AFFECTED -> Either.right(true);
            default -> Either.left(Constantes.UNKNOWN_ERROR);
        };
    }

    @Override
    public Either<String, Boolean> deleteNewspaper(Newspaper newspaper) {
        return switch (daoNewspapers.delete(newspaper)) {
            case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
            case Constantes.NOT_FOUND_OR_IN_USE_CODE -> Either.left(Constantes.NEWSPAPER_NOT_FOUND);
            case Constantes.ONE_ROW_AFFECTED -> Either.right(true);
            default -> Either.left(Constantes.UNKNOWN_ERROR);
        };
    }

    @Override
    public boolean hasArticles(Newspaper newspaper) {
        return !daoArticles.getAll(newspaper).isEmpty();
    }

}
