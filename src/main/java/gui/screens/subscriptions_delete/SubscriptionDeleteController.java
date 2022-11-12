package gui.screens.subscriptions_delete;

import domain.modelo.Subscription;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class SubscriptionDeleteController extends BaseScreenController {

    private final SubscriptionDeleteViewModel subscriptionDeleteViewModel;
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

    @Inject
    public SubscriptionDeleteController(SubscriptionDeleteViewModel subscriptionDeleteViewModel) {
        this.subscriptionDeleteViewModel = subscriptionDeleteViewModel;
    }

    public void initialize() {
        title.setText("Delete subscriptions");

        columnIdReader.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        columnIdNewspaper.setCellValueFactory(new PropertyValueFactory<>("idNewspaper"));
        columnSigningDate.setCellValueFactory(new PropertyValueFactory<>("signingDate"));
        columnCancellationDate.setCellValueFactory(new PropertyValueFactory<>("cancellationDate"));
        tableSubscriptions.setItems(subscriptionDeleteViewModel.getObservableSubscriptions());

        subscriptionDeleteViewModel.loadSubscriptions();

        subscriptionDeleteViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                subscriptionDeleteViewModel.cleanState();
            }
            if (newState.isSubscriptionDeleted()) {
                this.getPrincipalController().showAlert(Alert.AlertType.INFORMATION, ScreenConstants.SUCCESS, "Subscription deleted successfully");
                subscriptionDeleteViewModel.cleanState();
            }
        });
    }

    @FXML
    private void deleteSubscription() {
        Subscription subscription = tableSubscriptions.getSelectionModel().getSelectedItem();
        if (subscription != null) {
            subscriptionDeleteViewModel.deleteSubscription(subscription);
        }
    }
}
