package dao.impl;

import dao.ArticlesDao;
import dao.DBConnection;
import common.Constantes;
import dao.impl.row_mapers.ArticleRowMaper;
import dao.utils.SQLQueries;
import domain.modelo.*;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.List;

@Log4j2
public class ArticlesDaoImpl implements ArticlesDao {

    private final DBConnection dbConnection;

    @Inject
    public ArticlesDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Either<Integer, List<Article>> getAll() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<Article> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_WITH_TYPE_QUERY, new ArticleRowMaper());
            if (list.isEmpty()) {
                log.info(Constantes.THERE_ARE_NO_ARTICLES);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(list);
            }
            //Check if the query throws SQLTransientConnectionException
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }

    @Override
    public Either<Integer, List<Article>> getAll(Newspaper newspaper) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<Article> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_NEWSPAPER_WITH_TYPE_QUERY, new ArticleRowMaper(), newspaper.getId());
            if (list.isEmpty()) {
                log.info(Constantes.NO_ARTICLES_OF_THIS_NEWSPAPER);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }

    @Override
    public Either<Integer, List<Article>> getAll(ArticleType articleType) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<Article> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_TYPE_WITH_TYPE_QUERY, new ArticleRowMaper(), articleType.getId());
            if (list.isEmpty()) {
                log.info(Constantes.NO_ARTICLES_OF_THIS_TYPE);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }

    @Override
    public Either<Integer, List<Article>> getAll(Reader reader) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<Article> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_READER_WITH_TYPE_FROM_SUSCRIBE_QUERY, new ArticleRowMaper(), reader.getId());
            if (list.isEmpty()) {
                log.info(Constantes.NO_ARTICLES_OF_THIS_READER);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }

    @Override
    public Either<Integer, Article> get(int id) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            Article article = jdbcTemplate.queryForObject(SQLQueries.SELECT_ARTICLE_BY_ID_WITH_TYPE_QUERY, new ArticleRowMaper(), id);
            if (article == null) {
                log.info(Constantes.ARTICLE_NOT_FOUND);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(article);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }

    @Override
    public int save(Article article) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            return jdbcTemplate.update(SQLQueries.INSERT_ARTICLE_QUERY, article.getName(), article.getArticleTypeId(), article.getNewspaperId());
        } catch (DataIntegrityViolationException e) {
            log.error(Constantes.ARTICLE_ALREADY_EXISTS);
            return Constantes.ALREADY_EXISTS_CODE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Constantes.DATABASE_ERROR_CODE;
        }
    }

    @Override
    public int delete(Article article) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            return jdbcTemplate.update(SQLQueries.DELETE_ARTICLE_QUERY, article.getId());
        } catch (DataIntegrityViolationException e) {
            log.error(Constantes.ARTICLE_IN_USE_OR_NOT_FOUND);
            return Constantes.NOT_FOUND_OR_IN_USE_CODE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Constantes.DATABASE_ERROR_CODE;
        }
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
    public Either<Integer, List<ArticleQuery2>> getArticlesQuery2(ArticleType articleType) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<ArticleQuery2> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_TYPE_WITH_NEWSPAPER_QUERY, new BeanPropertyRowMapper<>(ArticleQuery2.class), articleType.getId());
            if (list.isEmpty()) {
                log.info(Constantes.NO_ARTICLES_OF_THIS_TYPE);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }

    @Override
    public Either<Integer, List<ArticleQuery3>> getArticlesQuery3(Newspaper newspaper) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<ArticleQuery3> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLES_BY_NEWSPAPER_WITH_BAD_RATING_QUERY, new BeanPropertyRowMapper<>(ArticleQuery3.class), newspaper.getId());
            if (list.isEmpty()) {
                log.info(Constantes.NO_ARTICLES_OF_THIS_NEWSPAPER);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }
}
