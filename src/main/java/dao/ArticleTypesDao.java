package dao;

import domain.modelo.ArticleType;

import java.util.List;

public interface ArticleTypesDao {
    List<ArticleType> getAll();

    ArticleType get(int id);

    int save(ArticleType articleType);

    int delete(ArticleType articleType);
}
