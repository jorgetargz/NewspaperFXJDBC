package domain.modelo;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleQuery1 {
    private int id;
    private String description;
    private int readers;
}
