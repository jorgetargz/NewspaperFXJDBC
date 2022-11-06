package domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    private Reader reader;
    private Newspaper newspaper;
    private LocalDate signingDate;
    private LocalDate cancellationDate;


    public int getIdReader() {
        return reader.getId();
    }

    public int getIdNewspaper() {
        return newspaper.getId();
    }

}
