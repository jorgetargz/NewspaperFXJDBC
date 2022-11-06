package domain.services.impl;


import dao.ReadersDao;
import domain.modelo.ArticleType;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.services.ServicesReaders;
import io.vavr.control.Either;
import jakarta.inject.Inject;

import java.util.List;

public class ServicesReadersImpl implements ServicesReaders {

    private final ReadersDao daoReaders;

    @Inject
    public ServicesReadersImpl(ReadersDao daoReaders) {
        this.daoReaders = daoReaders;
    }

    @Override
    public List<Reader> getReaders() {
        return daoReaders.getAll();
    }

    @Override
    public Either<String, Boolean> saveReader(Reader reader) {
        int result = daoReaders.save(reader);
        return result == 0 ? Either.right(true) : Either.left("Error saving the reader");
    }

    @Override
    public Either<String, Boolean> updateReader(Reader reader, String password) {
        if (password != null && !password.isEmpty()) {
            reader.getLogin().setPassword(password);
        }
        int result = daoReaders.update(reader);
        return result == 0 ? Either.right(true) : Either.left("Error updating the reader");
    }

    @Override
    public Either<String, Boolean> deleteReader(Reader reader) {
        int result = daoReaders.delete(reader);
        return switch (result) {
            case 0 -> Either.right(true);
            case -1 -> Either.left("SQL error");
            default -> Either.left("Unknown error");
        };
    }

    @Override
    public List<Reader> getReadersByNewspaper(Newspaper newspaper) {
        return daoReaders.getAll(newspaper);
    }

    @Override
    public List<Reader> getReadersByArticleType(ArticleType articleType) {
        return daoReaders.getAll(articleType);
    }
}