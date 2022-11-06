package domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Login {
    private final String username;
    private String password;
    private int idReader;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
