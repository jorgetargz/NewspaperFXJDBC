package domain.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Article {
    private int id;
    private String name;
    private ArticleType articleType;
    private int newspaperId;

    public Article(int id, String name, ArticleType articleType, int newspaperId) {
        this.id = id;
        this.name = name;
        this.articleType = articleType;
        this.newspaperId = newspaperId;
    }

    public Article(String nameText, ArticleType articleType, int newspaperId) {
        this.name = nameText;
        this.articleType = articleType;
        this.newspaperId = newspaperId;
    }

    public String getArticleType() {
        return this.articleType.getDescription();
    }

    public int getArticleTypeId() {
        return this.articleType.getId();
    }
}
