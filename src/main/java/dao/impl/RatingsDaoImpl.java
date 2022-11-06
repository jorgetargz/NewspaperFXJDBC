package dao.impl;

import dao.ArticlesDao;
import dao.DBConnection;
import dao.RatingsDao;
import dao.ReadersDao;
import dao.common.Constantes;
import dao.utils.SQLQueries;
import domain.modelo.ArticleRating;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class RatingsDaoImpl implements RatingsDao {

    private final DBConnection dbConnection;
    private final ArticlesDao articlesDao;
    private final ReadersDao readersDao;

    @Inject
    public RatingsDaoImpl(DBConnection dbConnection, ArticlesDao articlesDao, ReadersDao readersDao) {
        this.dbConnection = dbConnection;
        this.articlesDao = articlesDao;
        this.readersDao = readersDao;
    }

    @Override
    public List<ArticleRating> getAll() {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_RATINGS_QUERY)) {
            ResultSet rs = preparedStatement.executeQuery();
            return getArticleRatingsFromRS(rs);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ArticleRating> getAll(Reader reader) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_RATINGS_BY_READER_QUERY)) {
            preparedStatement.setInt(1, reader.getId());
            ResultSet rs = preparedStatement.executeQuery();
            return getArticleRatingsFromRS(rs);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public ArticleRating get(ArticleRating articleRating) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_RATING_BY_ID_QUERY)) {
            preparedStatement.setInt(1, articleRating.getArticle().getId());
            preparedStatement.setInt(2, articleRating.getReader().getId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                articleRating.setRating(rs.getInt("rating"));
                articleRating.setArticle(articlesDao.get(rs.getInt("id_article")));
                articleRating.setReader(readersDao.get(rs.getInt("id_reader")));
                return articleRating;
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public int save(int idReader, int idArticle, int rating) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.INSERT_RATING_QUERY)) {
            preparedStatement.setInt(1, idArticle);
            preparedStatement.setInt(2, idReader);
            preparedStatement.setInt(3, rating);
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                return Constantes.CONSTRAINT_VIOLATION;
            }
            return Constantes.SQL_EXCEPTION;
        }
    }

    @Override
    public int delete(ArticleRating articleRating) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.DELETE_RATING_QUERY)) {
            preparedStatement.setInt(1, articleRating.getArticle().getId());
            preparedStatement.setInt(2, articleRating.getReader().getId());
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    @Override
    public int deleteAll(Newspaper newspaper) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.DELETE_RATINGS_BY_NEWSPAPER_QUERY)) {
            preparedStatement.setInt(1, newspaper.getId());
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    private List<ArticleRating> getArticleRatingsFromRS(ResultSet rs) throws SQLException {
        List<ArticleRating> ratings = new ArrayList<>();
        while (rs.next()) {
            ArticleRating articleRating = new ArticleRating();
            articleRating.setId(rs.getInt("id"));
            articleRating.setArticle(articlesDao.get(rs.getInt("id_article")));
            articleRating.setReader(readersDao.get(rs.getInt("id_reader")));
            articleRating.setRating(rs.getInt("rating"));
            ratings.add(articleRating);
        }
        return ratings;
    }
}
