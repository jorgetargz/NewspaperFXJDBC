package domain.services;

import domain.modelo.ArticleType;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import io.vavr.control.Either;

import java.util.List;

public interface ServicesReaders {
    List<Reader> getReaders();

    List<Reader> getReadersByNewspaper(Newspaper newspaper);

    List<Reader> getReadersByArticleType(ArticleType articleType);

    Either<String, Boolean> saveReader(Reader reader);

    Either<String, Boolean> updateReader(Reader reader, String password);

    Either<String, Boolean> deleteReader(Reader reader);

}
