package dao.impl;

import dao.ArticleTypesDao;
import dao.DBConnection;
import common.Constantes;
import dao.utils.SQLQueries;
import domain.modelo.ArticleType;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Log4j2
public class ArticleTypesDaoImpl implements ArticleTypesDao {

    private final DBConnection dbConnection;

    @Inject
    public ArticleTypesDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Either<Integer, List<ArticleType>> getAll() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<ArticleType> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLE_TYPES_QUERY, new BeanPropertyRowMapper<>(ArticleType.class));
            if (list.isEmpty()) {
                log.error(Constantes.NO_ARTICLE_TYPES_FOUND);
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
    public Either<Integer, ArticleType> get(int id) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<ArticleType> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLE_TYPE_BY_ID_QUERY, new BeanPropertyRowMapper<>(ArticleType.class), id);
            if (list.isEmpty()) {
                log.error(Constantes.ARTICLE_TYPE_NOT_FOUND);
                return Either.left(Constantes.NOT_FOUND_ERROR_CODE);
            } else {
                return Either.right(list.get(0));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(Constantes.DATABASE_ERROR_CODE);
        }
    }

    @Override
    public int save(ArticleType articleType) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            return jdbcTemplate.update(SQLQueries.INSERT_ARTICLE_TYPE_QUERY, articleType.getId(), articleType.getDescription());
        } catch (DataIntegrityViolationException e) {
            log.error(Constantes.ARTICLE_TYPE_ALREADY_EXISTS);
            return Constantes.ALREADY_EXISTS_CODE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Constantes.DATABASE_ERROR_CODE;
        }
    }

    @Override
    public int delete(ArticleType articleType) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            return jdbcTemplate.update(SQLQueries.DELETE_ARTICLE_TYPE_QUERY, articleType.getId());
        } catch (DataIntegrityViolationException e) {
            log.error(Constantes.ARTICLE_TYPE_IN_USE_OR_NOT_FOUND);
            return Constantes.NOT_FOUND_OR_IN_USE_CODE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Constantes.DATABASE_ERROR_CODE;
        }
    }
}
