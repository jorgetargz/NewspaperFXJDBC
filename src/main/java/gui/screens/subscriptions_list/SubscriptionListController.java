package gui.screens.subscriptions_list;

import domain.modelo.Newspaper;
import domain.modelo.Subscription;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class SubscriptionListController extends BaseScreenController {

    @FXML
    private MFXComboBox<Newspaper> cbNewspapers;
    @FXML
    private Label title;
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

    private final SubscriptionListViewModel subscriptionListViewModel;

    @Inject
    public SubscriptionListController(SubscriptionListViewModel subscriptionListViewModel) {
        this.subscriptionListViewModel = subscriptionListViewModel;
    }

    public void initialize() {
        title.setText("List of subscriptions");

        columnIdReader.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        columnIdNewspaper.setCellValueFactory(new PropertyValueFactory<>("idNewspaper"));
        columnSigningDate.setCellValueFactory(new PropertyValueFactory<>("signingDate"));
        columnCancellationDate.setCellValueFactory(new PropertyValueFactory<>("cancellationDate"));
        tableSubscriptions.setItems(subscriptionListViewModel.getObservableSubscriptions());
        cbNewspapers.setItems(subscriptionListViewModel.getObservableNewspapers());

        subscriptionListViewModel.loadSubscriptions();
        subscriptionListViewModel.loadNewspapers();

        subscriptionListViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                subscriptionListViewModel.cleanState();
            }
        });
    }

    @FXML
    public void filter() {
        Newspaper newspaper = cbNewspapers.getSelectionModel().getSelectedItem();
        subscriptionListViewModel.loadSubscriptionsByNewspaper(newspaper);
    }

    @FXML
    public void filterOldest() {
        Newspaper newspaper = cbNewspapers.getSelectionModel().getSelectedItem();
        subscriptionListViewModel.loadOldestSubscriptionsByNewspaper(newspaper);
    }
}
