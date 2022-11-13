package domain.services;

import domain.modelo.Newspaper;
import io.vavr.control.Either;

import java.util.List;

public interface ServicesNewspapers {

    Either<String, List<Newspaper>> getNewspapers();

    Either<String, Newspaper> get(int id);

    Either<String, Boolean> saveNewspaper(Newspaper newspaper);

    Either<String, Boolean> deleteNewspaper(Newspaper newspaper);

    boolean hasArticles(Newspaper newspaper);
}
