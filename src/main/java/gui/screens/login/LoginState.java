package gui.screens.login;

import domain.modelo.Reader;
import lombok.Data;

@Data
public class LoginState {

    private final Reader reader;
    private final String error;

}
