package dao.impl;

import dao.ArticlesDao;
import dao.DBConnection;
import dao.impl.row_mapers.ArticleQuery3RowMaper;
import dao.impl.row_mapers.ArticleRowMaper;
import dao.utils.SQLQueries;
import domain.modelo.*;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Log4j2
public class ArticlesDaoImpl implements ArticlesDao {

    private final DBConnection dbConnection;

    @Inject
    public ArticlesDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Article> getAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_WITH_TYPE_QUERY, new ArticleRowMaper());
    }

    @Override
    public List<Article> getAll(Newspaper newspaper) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_NEWSPAPER_WITH_TYPE_QUERY, new ArticleRowMaper(), newspaper.getId());
    }

    @Override
    public List<Article> getAll(ArticleType articleType) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_TYPE_WITH_TYPE_QUERY, new ArticleRowMaper(), articleType.getId());
    }

    @Override
    public List<Article> getAll(Reader reader) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_READER_WITH_TYPE_FROM_SUSCRIBE_QUERY, new ArticleRowMaper(), reader.getId());
    }

    @Override
    public Article get(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.queryForObject(SQLQueries.SELECT_ARTICLE_BY_ID_WITH_TYPE_QUERY, new ArticleRowMaper(), id);
    }

    @Override
    public int save(Article article) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.update(SQLQueries.INSERT_ARTICLE_QUERY, article.getName(), article.getArticleTypeId(), article.getNewspaperId());
    }

    @Override
    public int delete(Article article) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.update(SQLQueries.DELETE_ARTICLE_QUERY, article.getId());
    }

    @Override
    public int deleteAll(Newspaper newspaper) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.update(SQLQueries.DELETE_ARTICLES_BY_NEWSPAPER_QUERY, newspaper.getId());
    }

    @Override
    public ArticleQuery1 getArticleQuery1(int id) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_ARTICLE_QUERY)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new ArticleQuery1(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getInt("readers")
                );
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public List<ArticleQuery2> getArticlesQuery2(ArticleType articleType) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_TYPE_WITH_NEWSPAPER_QUERY, new BeanPropertyRowMapper<>(ArticleQuery2.class), articleType.getId());
    }

    @Override
    public List<ArticleQuery3> getArticlesQuery3(Newspaper newspaper) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_NEWSPAPER_WITH_BAD_RATING_QUERY, new ArticleQuery3RowMaper(), newspaper.getId());
    }
}
