package gui.screens.readers_delete;

import domain.modelo.Reader;
import gui.screens.common.BaseScreenController;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Optional;

public class ReadersDeleteController extends BaseScreenController {

    private final ReadersDeleteViewModel readersDeleteViewModel;
    @FXML
    private Label title;
    @FXML
    private TableView<Reader> tableReaders;
    @FXML
    private TableColumn<Reader, Integer> columnId;
    @FXML
    private TableColumn<Reader, String> columnName;
    @FXML
    private TableColumn<Reader, LocalDate> columnDateOfBirth;

    @Inject
    public ReadersDeleteController(ReadersDeleteViewModel readersDeleteViewModel) {
        this.readersDeleteViewModel = readersDeleteViewModel;
    }

    public void initialize() {
        title.setText("Delete a Reader");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        tableReaders.setItems(readersDeleteViewModel.getObservableReaders());

        readersDeleteViewModel.loadReaders();

        readersDeleteViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                readersDeleteViewModel.cleanState();
            }
            if (newState.isOnDeleteReader()) {
                Reader reader = tableReaders.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getButtonTypes().remove(ButtonType.OK);
                alert.getButtonTypes().add(ButtonType.CANCEL);
                alert.getButtonTypes().add(ButtonType.YES);
                alert.setTitle("Confirm delete");
                alert.setHeaderText("Confirm delete");
                alert.setContentText("This will delete the reader and all its ratings and subscriptions. Are you sure you want to continue?");
                Optional<ButtonType> res = alert.showAndWait();
                res.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.YES) {
                        readersDeleteViewModel.deleteReaderConfirm(reader);
                    }
                });
                readersDeleteViewModel.cleanState();
            }
        });
    }

    @FXML
    private void deleteReader() {
        readersDeleteViewModel.deleteReader(tableReaders.getSelectionModel().getSelectedItem());
    }
}
