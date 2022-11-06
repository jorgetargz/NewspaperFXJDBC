package gui.screens.readers_add;

import domain.modelo.Reader;
import gui.screens.common.BaseScreenController;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class ReaderAddController extends BaseScreenController {

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
    @FXML
    private MFXTextField nameTxt;
    @FXML
    private MFXDatePicker datePicker;
    @FXML
    private MFXPasswordField passwordTxt;
    @FXML
    private MFXTextField usernameTxt;

    private final ReaderAddViewModel readersAddViewModel;

    @Inject
    public ReaderAddController(ReaderAddViewModel readersAddViewModel) {
        this.readersAddViewModel = readersAddViewModel;
    }

    public void initialize() {
        title.setText("Add Reader");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        tableReaders.setItems(readersAddViewModel.getObservableReaders());

        readersAddViewModel.loadReaders();

        readersAddViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                readersAddViewModel.cleanState();
            }
        });
    }

    @FXML
    private void addReader() {
        readersAddViewModel.addReader(nameTxt.getText(), datePicker.getValue(), usernameTxt.getText(), passwordTxt.getText());
    }
}
