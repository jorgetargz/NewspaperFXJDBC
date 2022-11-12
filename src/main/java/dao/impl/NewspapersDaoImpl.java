package dao.impl;

import dao.DBConnection;
import dao.NewspapersDao;
import dao.common.Constantes;
import dao.utils.SQLQueries;
import domain.modelo.Newspaper;
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
    public List<Newspaper> getAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_NEWSPAPERS_QUERY, BeanPropertyRowMapper.newInstance(Newspaper.class));
    }

    @Override
    public Newspaper get(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        List<Newspaper> list = jdbcTemplate.query(SQLQueries.SELECT_NEWSPAPER_BY_ID_QUERY, BeanPropertyRowMapper.newInstance(Newspaper.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int save(Newspaper newspaper) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.update(SQLQueries.INSERT_NEWSPAPER_QUERY, newspaper.getName_newspaper(), newspaper.getRelease_date());
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
                responseCode = Constantes.CONSTRAINT_VIOLATION;
            } else {
                responseCode = Constantes.SQL_EXCEPTION;
            }
            transactionManager.rollback(transactionStatus);
            log.error(e.getMessage(), e);
        }
        return responseCode;
    }
}
