package dao.impl.row_mapers;

import dao.common.Constantes;
import domain.modelo.ArticleQuery3;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleQuery3RowMaper implements RowMapper<ArticleQuery3> {

    @Override
    public ArticleQuery3 mapRow(ResultSet rs, int row) throws SQLException {
        ArticleQuery3 articlequery3 = new ArticleQuery3();
        articlequery3.setId(rs.getInt("id"));
        articlequery3.setNameArticle(rs.getString("name_article"));
        articlequery3.setIdType(rs.getInt("id_type"));
        articlequery3.setIdNewspaper(rs.getInt("id_newspaper"));
        articlequery3.setIdReader(rs.getInt("id_reader"));
        articlequery3.setRating(rs.getInt("rating"));
        articlequery3.setBadRatings(rs.getInt("bad_ratings"));
        articlequery3.setCritical(rs.getInt("bad_ratings") > Constantes.BAD_RATING_LIMIT);
        return articlequery3;
    }
}
