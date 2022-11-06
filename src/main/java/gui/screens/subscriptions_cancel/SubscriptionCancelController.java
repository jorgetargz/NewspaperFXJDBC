package gui.screens.subscriptions_cancel;

import domain.modelo.Reader;
import domain.modelo.Subscription;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class SubscriptionCancelController extends BaseScreenController {

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

    private final SubscriptionCancelViewModel subscriptionCancelViewModel;

    @Inject
    public SubscriptionCancelController(SubscriptionCancelViewModel subscriptionCancelViewModel) {
        this.subscriptionCancelViewModel = subscriptionCancelViewModel;
    }

    public void initialize() {
        title.setText("Cancel subscriptions");

        columnIdReader.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        columnIdNewspaper.setCellValueFactory(new PropertyValueFactory<>("idNewspaper"));
        columnSigningDate.setCellValueFactory(new PropertyValueFactory<>("signingDate"));
        columnCancellationDate.setCellValueFactory(new PropertyValueFactory<>("cancellationDate"));
        tableSubscriptions.setItems(subscriptionCancelViewModel.getObservableSubscriptions());

        subscriptionCancelViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                subscriptionCancelViewModel.cleanState();
            }
            if (newState.isSubscriptionCancelled()) {
                this.getPrincipalController().showAlert(Alert.AlertType.INFORMATION, ScreenConstants.SUCCESS, "Subscription cancelled successfully");
                subscriptionCancelViewModel.cleanState();
            }
        });
    }

    @Override
    public void principalCargado() {
        Reader reader = this.getPrincipalController().getReader();
        if (reader.getId() > 0) {
            subscriptionCancelViewModel.loadSubscriptions(reader);
        } else {
            subscriptionCancelViewModel.loadSubscriptions();
        }
    }

    @FXML
    private void cancelSubscription(ActionEvent actionEvent) {
        Subscription subscription = tableSubscriptions.getSelectionModel().getSelectedItem();
        if (subscription != null) {
            subscriptionCancelViewModel.cancelSubscription(subscription);
        }
    }
}
