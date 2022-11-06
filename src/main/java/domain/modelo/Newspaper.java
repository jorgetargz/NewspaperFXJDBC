package domain.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Newspaper {
    private int id;
    private String name_newspaper;
    private LocalDate release_date;

    public Newspaper(String name, LocalDate releaseDate) {
        this.name_newspaper = name;
        this.release_date = releaseDate;
    }

    @Override
    public String toString() {
        return this.name_newspaper;
    }
}
