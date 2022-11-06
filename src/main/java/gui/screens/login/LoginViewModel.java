package gui.screens.login;

import domain.modelo.Reader;
import domain.services.ServicesCredentials;
import gui.screens.common.ScreenConstants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LoginViewModel {

    private final ServicesCredentials servicesCredentials;
    private final ObjectProperty<LoginState> state;

    @Inject
    public LoginViewModel(ServicesCredentials servicesCredentials) {
        this.servicesCredentials = servicesCredentials;
        state = new SimpleObjectProperty<>(new LoginState(null, null));
    }

    public ReadOnlyObjectProperty<LoginState> getState() {
        return state;
    }

    public void doLogin(String username, String password) {
        Reader reader = servicesCredentials.scLogin(username, password);
        if (reader != null) {
            state.setValue(new LoginState(reader, null));
        } else {
            state.setValue(new LoginState(null, ScreenConstants.INVALID_CREDENTIALS));
        }
    }

    public void clenState() {
        state.setValue(new LoginState(null, null));
    }
}
