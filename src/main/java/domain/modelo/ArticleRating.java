package domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRating {
    private int id;
    private Reader reader;
    private Article article;
    private int rating;

    public int getIdReader() {
        return reader.getId();
    }

    public int getIdArticle() {
        return article.getId();
    }
}