package gui.screens.subscriptions_add;

import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.modelo.Subscription;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class SubscriptionAddController extends BaseScreenController {

    private final SubscriptionAddViewModel subscriptionAddViewModel;
    @FXML
    private Label title;
    @FXML
    private MFXTextField idReaderTxt;
    @FXML
    private MFXTextField idNewspaperTxt;
    @FXML
    private MFXDatePicker signingDatePicker;
    @FXML
    private TableView<Newspaper> tableNewspapers;
    @FXML
    private TableColumn<Newspaper, Integer> columnId;
    @FXML
    private TableColumn<Newspaper, String> columnName;
    @FXML
    private TableColumn<Newspaper, LocalDate> columnPublishDate;
    @FXML
    private TableView<Subscription> tableSubscriptions;
    @FXML
    private TableColumn<Subscription, Integer> columnIdReader;
    @FXML
    private TableColumn<Subscription, Integer> columnIdNewspaper;
    @FXML
    private TableColumn<Subscription, LocalDate> columnSigningDate;
    @FXML
    private TableColumn<Subscription, LocalDate> columnCancellationDate;

    @Inject
    public SubscriptionAddController(SubscriptionAddViewModel subscriptionAddViewModel) {
        this.subscriptionAddViewModel = subscriptionAddViewModel;
    }

    public void initialize() {
        title.setText("Add subscriptions");

        columnIdReader.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        columnIdNewspaper.setCellValueFactory(new PropertyValueFactory<>("idNewspaper"));
        columnSigningDate.setCellValueFactory(new PropertyValueFactory<>("signingDate"));
        columnCancellationDate.setCellValueFactory(new PropertyValueFactory<>("cancellationDate"));
        tableSubscriptions.setItems(subscriptionAddViewModel.getObservableSubscriptions());

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name_newspaper"));
        columnPublishDate.setCellValueFactory(new PropertyValueFactory<>("release_date"));
        tableNewspapers.setItems(subscriptionAddViewModel.getObservableNewspapers());

        subscriptionAddViewModel.loadNewspapers();

        subscriptionAddViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                subscriptionAddViewModel.cleanState();
            }
            if (newState.isSubscriptionAdded()) {
                this.getPrincipalController().showAlert(Alert.AlertType.INFORMATION, ScreenConstants.SUCCESS, "Subscription added successfully");
                subscriptionAddViewModel.cleanState();
            }
        });
    }

    @Override
    public void principalCargado() {
        Reader reader = this.getPrincipalController().getReader();
        if (reader.getId() > 0) {
            idReaderTxt.setText(String.valueOf(reader.getId()));
            idReaderTxt.setDisable(true);
            subscriptionAddViewModel.loadSubscriptions(reader);
        } else {
            subscriptionAddViewModel.loadSubscriptions();
        }
    }

    @FXML
    private void addSubscription() {
        int idReader = Integer.parseInt(idReaderTxt.getText());
        int idNewspaper = Integer.parseInt(idNewspaperTxt.getText());
        LocalDate signingDate = signingDatePicker.getValue();
        subscriptionAddViewModel.addSubscription(idReader, idNewspaper, signingDate);
    }
}
