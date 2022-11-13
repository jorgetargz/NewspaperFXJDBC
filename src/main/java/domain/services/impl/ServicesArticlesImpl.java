package domain.services.impl;

import common.Constantes;
import dao.ArticleTypesDao;
import dao.ArticlesDao;
import dao.NewspapersDao;
import domain.modelo.*;
import domain.services.ServicesArticles;
import io.vavr.control.Either;
import jakarta.inject.Inject;

import java.util.List;


public class ServicesArticlesImpl implements ServicesArticles {

    private final ArticlesDao daoArticles;
    private final NewspapersDao daoNewspapers;
    private final ArticleTypesDao daoArticleTypes;

    @Inject
    public ServicesArticlesImpl(ArticlesDao daoArticles, NewspapersDao daoNewspapers, ArticleTypesDao daoArticleTypes) {
        this.daoArticles = daoArticles;
        this.daoNewspapers = daoNewspapers;
        this.daoArticleTypes = daoArticleTypes;
    }

    @Override
    public Either<String, List<Article>> getArticles() {
        Either<Integer, List<Article>> result = daoArticles.getAll();
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.THERE_ARE_NO_ARTICLES);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }

    @Override
    public Either<String, List<Article>> getArticlesByType(ArticleType type) {
        Either<Integer, List<Article>> result = daoArticles.getAll(type);
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.THERE_ARE_NO_ARTICLES_OF_THIS_TYPE);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }

    @Override
    public Either<String, List<Article>> getArticlesAvailableForReader(Reader reader) {
        Either<Integer, List<Article>> result = daoArticles.getAll(reader);
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.NO_ARTICLES_AVAILABLE_FOR_READER);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }

    @Override
    public Either<String, List<ArticleType>> getArticleTypes() {
        Either<Integer, List<ArticleType>> result = daoArticleTypes.getAll();
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.NO_ARTICLE_TYPES_FOUND);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }

    @Override
    public Either<String, Boolean> saveArticle(Article article) {
        if (daoNewspapers.get(article.getNewspaperId()).get() != null) {
            if (daoArticleTypes.get(article.getArticleTypeId()).get() != null) {
                return switch (daoArticles.save(article)) {
                    case Constantes.ONE_ROW_AFFECTED -> Either.right(true);
                    case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                    default -> Either.left(Constantes.UNKNOWN_ERROR);
                };
            } else {
                return Either.left(Constantes.ARTICLE_TYPE_NOT_FOUND);
            }
        } else {
            return Either.left(Constantes.NEWSPAPER_NOT_FOUND);
        }
    }

    @Override
    public Either<String, ArticleQuery1> getArticleInfo(int id) {
        ArticleQuery1 articleQuery1 = daoArticles.getArticleQuery1(id);
        if (articleQuery1 != null) {
            return Either.right(articleQuery1);
        } else {
            return Either.left(Constantes.ARTICLE_NOT_FOUND);
        }
    }

    @Override
    public Either<String, List<ArticleQuery2>> getArticlesByTypeWithNewspaper(ArticleType articleType) {
        Either<Integer, List<ArticleQuery2>> result = daoArticles.getArticlesQuery2(articleType);
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.THERE_ARE_NO_ARTICLES_OF_THIS_TYPE);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }

    @Override
    public Either<String, List<ArticleQuery3>> getArticlesByNewspaperWithBadRatings(Newspaper newspaper) {
        Either<Integer, List<ArticleQuery3>> result = daoArticles.getArticlesQuery3(newspaper);
        if (result.isLeft()) {
            return switch (result.getLeft()) {
                case Constantes.NOT_FOUND_ERROR_CODE -> Either.left(Constantes.THERE_ARE_NO_ARTICLES_WITH_BAD_RATINGS);
                case Constantes.DATABASE_ERROR_CODE -> Either.left(Constantes.DATABASE_ERROR);
                default -> Either.left(Constantes.UNKNOWN_ERROR);
            };
        } else {
            return Either.right(result.get());
        }
    }
}

