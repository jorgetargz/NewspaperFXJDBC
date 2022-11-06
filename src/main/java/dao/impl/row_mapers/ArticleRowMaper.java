package dao.impl.row_mapers;

import domain.modelo.Article;
import domain.modelo.ArticleType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleRowMaper implements RowMapper<Article> {

    @Override
    public Article mapRow(ResultSet rs, int row) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setName(rs.getString("name_article"));
        ArticleType articleType = new ArticleType(rs.getInt("id_type"), rs.getString("description"));
        article.setArticleType(articleType);
        article.setNewspaperId(rs.getInt("id_newspaper"));
        return article;
    }
}
