package dao.impl;

import dao.ArticleTypesDao;
import dao.DBConnection;
import dao.utils.SQLQueries;
import domain.modelo.ArticleType;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
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
    public List<ArticleType> getAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.query(SQLQueries.SELECT_ARTICLE_TYPES_QUERY, BeanPropertyRowMapper.newInstance(ArticleType.class));
    }

    @Override
    public ArticleType get(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        List<ArticleType> list = jdbcTemplate.query(SQLQueries.SELECT_ARTICLE_TYPE_BY_ID_QUERY, BeanPropertyRowMapper.newInstance(ArticleType.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int save(ArticleType articleType) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.update(SQLQueries.INSERT_ARTICLE_TYPE_QUERY, articleType.getId(), articleType.getDescription());
    }

    @Override
    public int delete(ArticleType articleType) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnection.getDataSource());
        return jdbcTemplate.update(SQLQueries.DELETE_ARTICLE_TYPE_QUERY, articleType.getId());
    }
}
