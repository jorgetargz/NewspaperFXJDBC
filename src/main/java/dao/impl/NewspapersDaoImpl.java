package dao.impl;

import dao.DBConnection;
import dao.NewspapersDao;
import common.Constantes;
import dao.utils.SQLQueries;
import domain.modelo.Newspaper;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Log4j2
public class NewspapersDaoImpl implements NewspapersDao {

    private final DBConnection dbConnection;

    @Inject
    public NewspapersDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Either<Integer, List<Newspaper>> getAll() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<Newspaper> list = jdbcTemplate.query(SQLQueries.SELECT_NEWSPAPERS_QUERY, new BeanPropertyRowMapper<>(Newspaper.class));
            if (list.isEmpty()) {
                log.error(Constantes.NO_NEWSPAPERS_FOUND);
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
    public Either<Integer, Newspaper> get(int id) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            List<Newspaper> list = jdbcTemplate.query(SQLQueries.SELECT_NEWSPAPER_BY_ID_QUERY, new BeanPropertyRowMapper<>(Newspaper.class), id);
            if (list.isEmpty()) {
                log.error(Constantes.NEWSPAPER_NOT_FOUND);
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
    public int save(Newspaper newspaper) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
            return jdbcTemplate.update(SQLQueries.INSERT_NEWSPAPER_QUERY, newspaper.getName_newspaper(), newspaper.getRelease_date());
        } catch (DataIntegrityViolationException e) {
            log.error(Constantes.NEWSPAPER_ALREADY_EXISTS);
            return Constantes.ALREADY_EXISTS_CODE;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            return Constantes.DATABASE_ERROR_CODE;
        }
    }

    @Override
    public int delete(Newspaper newspaper) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dbConnection.getDataSource());
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        int responseCode;
        try {
            JdbcTemplate jtm = new JdbcTemplate(dbConnection.getDataSource());
            jtm.update(SQLQueries.DELETE_RATINGS_BY_NEWSPAPER_QUERY, newspaper.getId());
            jtm.update(SQLQueries.DELETE_SUBSCRIPTIONS_BY_NEWSPAPER, newspaper.getId());
            jtm.update(SQLQueries.DELETE_ARTICLES_BY_NEWSPAPER_QUERY, newspaper.getId());
            jtm.update(SQLQueries.DELETE_NEWSPAPER_QUERY, newspaper.getId());
            responseCode = Constantes.SUCCESS;
            transactionManager.commit(transactionStatus);
        } catch (DataAccessException e) {
            if (e instanceof DataIntegrityViolationException) {
                responseCode = Constantes.NOT_FOUND_OR_IN_USE_CODE;
            } else {
                responseCode = Constantes.SQL_EXCEPTION;
            }
            transactionManager.rollback(transactionStatus);
            log.error(e.getMessage(), e);
        }
        return responseCode;
    }
}
