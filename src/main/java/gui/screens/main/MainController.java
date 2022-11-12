package gui.screens.main;


import domain.modelo.Reader;
import domain.services.ServicesCredentials;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import gui.screens.common.Screens;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Log4j2
public class MainController {

    final Instance<Object> instance;
    private final Alert alert;
    private final ServicesCredentials servicesCredentials;
    @FXML
    private MenuItem menuItemListSubscriptions;
    @FXML
    private MenuItem menuItemDeleteSubscriptions;
    @FXML
    private MenuItem menuItemListRatings;
    @FXML
    private MenuItem menuItemAddRatings;
    @FXML
    private Menu menuSesion;
    @FXML
    private Menu menuNewspapers;
    @FXML
    private Menu menuArticles;
    @FXML
    private Menu menuReaders;
    @FXML
    private Menu menuSubscriptions;
    @FXML
    private Menu menuRatings;
    @FXML
    private Menu menuHelp;
    private Stage primaryStage;
    private double xOffset;
    private double yOffset;
    @FXML
    private BorderPane root;
    @FXML
    private HBox windowHeader;
    @FXML
    private MFXFontIcon closeIcon;
    @FXML
    private MFXFontIcon minimizeIcon;
    @FXML
    private MFXFontIcon alwaysOnTopIcon;
    @FXML
    private MenuBar menuPrincipal;
    private Reader reader;


    @Inject
    public MainController(Instance<Object> instance, ServicesCredentials servicesCredentials) {
        this.instance = instance;
        this.servicesCredentials = servicesCredentials;
        alert = new Alert(Alert.AlertType.NONE);
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    public void initialize() {
        closeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showAlertConfirmClose());
        minimizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> ((Stage) root.getScene().getWindow()).setIconified(true));
        alwaysOnTopIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            boolean newVal = !primaryStage.isAlwaysOnTop();
            alwaysOnTopIcon.pseudoClassStateChanged(PseudoClass
                    .getPseudoClass(ScreenConstants.ALWAYS_ON_TOP), newVal);
            primaryStage.setAlwaysOnTop(newVal);
        });

        windowHeader.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        windowHeader.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
        menuPrincipal.setVisible(false);
        cargarPantalla(Screens.LOGIN);
    }

    public double getWidth() {
        return root.getScene().getWindow().getWidth();
    }

    private void showAlertConfirmClose() {
        Alert alertCerrar = new Alert(Alert.AlertType.WARNING);
        alertCerrar.getButtonTypes().remove(ButtonType.OK);
        alertCerrar.getButtonTypes().add(ButtonType.CANCEL);
        alertCerrar.getButtonTypes().add(ButtonType.YES);
        alertCerrar.setHeaderText(ScreenConstants.EXIT);
        alertCerrar.setTitle(ScreenConstants.EXIT);
        alertCerrar.setContentText(ScreenConstants.SURE_EXIT);
        alertCerrar.initOwner(primaryStage.getOwner());
        Optional<ButtonType> res = alertCerrar.showAndWait();
        res.ifPresent(buttonType -> {
            if (buttonType == ButtonType.YES) {
                //Cerrar el pool de conexiones
                servicesCredentials.scCloseApp();
                Platform.exit();
            }
        });
    }

    public void showAlert(Alert.AlertType alertType, String titulo, String mensaje) {
        alert.setAlertType(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarPantalla(Screens pantalla) {
        Pane panePantalla;
        ResourceBundle r = ResourceBundle.getBundle(ScreenConstants.I_18_N_TEXTS_UI, Locale.ENGLISH);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            fxmlLoader.setResources(r);
            panePantalla = fxmlLoader.load(getClass().getResourceAsStream(pantalla.getPath()));
            BaseScreenController pantallaController = fxmlLoader.getController();
            pantallaController.setPrincipalController(this);
            pantallaController.principalCargado();
            root.setCenter(panePantalla);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @FXML
    private void menuOnClick(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case ScreenConstants.MENU_ITEM_PANTALLA_INICIO -> cargarPantalla(Screens.WELCOME);
            case ScreenConstants.MENU_ITEM_LIST_NEWSPAPERS -> cargarPantalla(Screens.NEWSPAPER_LIST);
            case ScreenConstants.MENU_ITEM_ADD_NEWSPAPER -> cargarPantalla(Screens.NEWSPAPER_ADD);
            case ScreenConstants.MENU_ITEM_DELETE_NEWSPAPERS -> cargarPantalla(Screens.NEWSPAPER_DELETE);
            case ScreenConstants.MENU_ITEM_LIST_ARTICLES -> cargarPantalla(Screens.ARTICLE_LIST);
            case ScreenConstants.MENU_ITEM_LIST_ARTICLES_WITH_NEWSPAPER ->
                    cargarPantalla(Screens.ARTICLE_LIST_WITH_NEWSPAPER);
            case ScreenConstants.MENU_ITEM_LIST_ARTICLES_WITH_BAD_RATING ->
                    cargarPantalla(Screens.ARTICLE_LIST_WITH_BAD_RATING);
            case ScreenConstants.MENU_ITEM_ADD_ARTICLES -> cargarPantalla(Screens.ARTICLE_ADD);
            case ScreenConstants.MENU_ITEM_LIST_READERS -> cargarPantalla(Screens.READER_LIST);
            case ScreenConstants.MENU_ITEM_DELETE_READERS -> cargarPantalla(Screens.READER_DELETE);
            case ScreenConstants.MENU_ITEM_LIST_SUBSCRIPTIONS -> cargarPantalla(Screens.SUBSCRIPTION_LIST);
            case ScreenConstants.MENU_ITEM_LIST_RATINGS -> cargarPantalla(Screens.RATING_LIST);
            case ScreenConstants.MENU_ITEM_ADD_RATINGS -> cargarPantalla(Screens.RATING_ADD);
            case ScreenConstants.MENU_ITEM_ADD_READERS -> cargarPantalla(Screens.READERS_ADD);
            case ScreenConstants.MENU_ITEM_UPDATE_READERS -> cargarPantalla(Screens.READERS_UPDATE);
            case ScreenConstants.MENU_ITEM_ADD_SUBSCRIPTIONS -> cargarPantalla(Screens.SUBSCRIPTION_ADD);
            case ScreenConstants.MENU_ITEM_DELETE_SUBSCRIPTIONS -> cargarPantalla(Screens.SUBSCRIPTION_DELETE);
            case ScreenConstants.MENU_ITEM_CANCEL_SUBSCRIPTIONS -> cargarPantalla(Screens.SUBSCRIPTION_CANCEL);
            default -> cargarPantalla(Screens.LOGIN);
        }
    }

    @FXML
    private void cambiarcss() {
        if (primaryStage.getScene().getRoot().getStylesheets().stream().findFirst().isEmpty()
                || (primaryStage.getScene().getRoot().getStylesheets().stream().findFirst().isPresent()
                && primaryStage.getScene().getRoot().getStylesheets().stream().findFirst().get().contains(ScreenConstants.STYLE))) {
            try {
                primaryStage.getScene().getRoot().getStylesheets().clear();
                primaryStage.getScene().getRoot().getStylesheets().add(getClass().getResource(ScreenConstants.CSS_DARKMODE_CSS).toExternalForm());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            try {
                primaryStage.getScene().getRoot().getStylesheets().clear();
                primaryStage.getScene().getRoot().getStylesheets().add(getClass().getResource(ScreenConstants.CSS_STYLE_CSS).toExternalForm());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @FXML
    private void acercaDe() {
        showAlert(Alert.AlertType.INFORMATION, ScreenConstants.ABOUT, ScreenConstants.AUTHOR_DATA);
    }

    @FXML
    private void logout() {
        menuPrincipal.setVisible(false);
        cargarPantalla(Screens.LOGIN);
    }

    @FXML
    private void exit() {
        showAlertConfirmClose();
    }

    //events launched on other screens
    public void onLoginHecho() {
        if (reader.getId() > 0) {
            menuPrincipal.setVisible(true);
            menuSesion.setVisible(true);
            menuNewspapers.setVisible(false);
            menuArticles.setVisible(false);
            menuReaders.setVisible(false);
            menuSubscriptions.setVisible(true);
            menuItemDeleteSubscriptions.setVisible(false);
            menuItemListSubscriptions.setVisible(false);
            menuRatings.setVisible(true);
            menuItemListRatings.setVisible(false);
            menuItemAddRatings.setVisible(true);
            menuHelp.setVisible(true);
        } else {
            menuPrincipal.setVisible(true);
            menuSesion.setVisible(true);
            menuNewspapers.setVisible(true);
            menuArticles.setVisible(true);
            menuReaders.setVisible(true);
            menuSubscriptions.setVisible(true);
            menuItemDeleteSubscriptions.setVisible(true);
            menuItemListSubscriptions.setVisible(true);
            menuRatings.setVisible(true);
            menuItemListRatings.setVisible(true);
            menuItemAddRatings.setVisible(false);
            menuHelp.setVisible(true);
        }
        cargarPantalla(Screens.WELCOME);
    }


}
