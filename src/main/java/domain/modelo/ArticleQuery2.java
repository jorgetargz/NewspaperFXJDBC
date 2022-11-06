package domain.modelo;

import lombok.Data;

@Data
public class ArticleQuery2 {
    private int id;
    private String name_article;
    private int id_type;
    private String description;
    private int id_newspaper;
    private String name_newspaper;
}
