package domain.services.impl;

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
    public List<Article> getArticles() {
        return daoArticles.getAll();
    }

    @Override
    public List<Article> getArticlesByType(int typeId) {
        return daoArticles.getAll().stream()
                .filter(article -> article.getArticleTypeId() == typeId)
                .toList();
    }

    @Override
    public List<Article> getArticlesAvailableForReader(Reader reader) {
        return daoArticles.getAll(reader);
    }

    @Override
    public List<ArticleType> getArticleTypes() {
        return daoArticleTypes.getAll();
    }

    @Override
    public Either<String, Boolean> saveArticle(Article article) {
        if (daoNewspapers.get(article.getNewspaperId()) != null) {
            if (daoArticleTypes.get(article.getArticleTypeId()) != null) {
                return daoArticles.save(article) == 1
                        ? Either.right(true)
                        : Either.left("Error al guardar el artículo");
            } else {
                return Either.left("Article Type does not exist");
            }
        } else {
            return Either.left("Newspaper does not exist");
        }
    }

    @Override
    public Either<String, ArticleQuery1> getArticleInfo(int id) {
        ArticleQuery1 articleQuery1 = daoArticles.getArticleQuery1(id);
        if (articleQuery1 != null) {
            return Either.right(articleQuery1);
        } else {
            return Either.left("Article does not exist");
        }
    }

    @Override
    public List<ArticleQuery2> getArticlesByTypeWithNewspaper(ArticleType articleType) {
        return daoArticles.getArticlesQuery2(articleType);
    }

    @Override
    public List<ArticleQuery3> getArticlesByNewspaperWithBadRatings(Newspaper newspaper) {
        return daoArticles.getArticlesQuery3(newspaper);
    }
}

