package gui.screens.login;

import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class LoginController extends BaseScreenController {

    private final LoginViewModel loginViewModel;
    @FXML
    public ImageView logo;

    @FXML
    private MFXTextField txtUsername;
    @FXML
    private MFXPasswordField txtPass;

    @Inject
    public LoginController(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

    public void initialize() {
        try (var inputStream = getClass().getResourceAsStream(ScreenConstants.MEDIA_LOGO_PNG)) {
            assert inputStream != null;
            Image logoImage = new Image(inputStream);
            logo.setImage(logoImage);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        loginViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                loginViewModel.clenState();
            }
            if (newState.getReader() != null) {
                this.getPrincipalController().setReader(newState.getReader());
                this.getPrincipalController().onLoginHecho();
            }
        });
    }

    @FXML
    private void doLogin() {
        String username = txtUsername.getText();
        String password = txtPass.getText();
        loginViewModel.doLogin(username, password);
    }
}
