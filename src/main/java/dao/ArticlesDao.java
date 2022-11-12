package dao;

import domain.modelo.*;

import java.util.List;

public interface ArticlesDao {
    List<Article> getAll();

    List<Article> getAll(Newspaper newspaper);

    List<Article> getAll(ArticleType articleType);

    List<Article> getAll(Reader reader);

    Article get(int id);

    int save(Article article);

    int delete(Article article);

    ArticleQuery1 getArticleQuery1(int id);

    List<ArticleQuery2> getArticlesQuery2(ArticleType articleType);

    List<ArticleQuery3> getArticlesQuery3(Newspaper newspaper);
}
