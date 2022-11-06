package dao;

import domain.modelo.ArticleType;
import domain.modelo.Newspaper;
import domain.modelo.Reader;

import java.util.List;

public interface ReadersDao {
    List<Reader> getAll();

    List<Reader> getAll(Newspaper newspaper);

    List<Reader> getAll(ArticleType articleType);

    Reader get(int id);

    int save(Reader reader);

    int update(Reader reader);

    int delete(Reader reader);
}
