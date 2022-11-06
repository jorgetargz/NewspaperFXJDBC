package dao;

import domain.modelo.Newspaper;

import java.util.List;

public interface NewspapersDao {
    List<Newspaper> getAll();

    Newspaper get(int id);

    int save(Newspaper newspaper);

    int delete(Newspaper newspaper);
}
