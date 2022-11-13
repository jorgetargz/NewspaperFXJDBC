package dao;

import domain.modelo.Newspaper;
import io.vavr.control.Either;

import java.util.List;

public interface NewspapersDao {
    Either<Integer, List<Newspaper>> getAll();

    Either<Integer, Newspaper> get(int id);

    int save(Newspaper newspaper);

    int delete(Newspaper newspaper);
}
