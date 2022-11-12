package gui.screens.readers_update;

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

public class ReadersUpdateController extends BaseScreenController {

    private final ReadersUpdateViewModel readersUpdateViewModel;
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
    private MFXTextField idTxt;
    @FXML
    private MFXTextField nameTxt;
    @FXML
    private MFXDatePicker datePicker;
    @FXML
    private MFXPasswordField paswordTxt;

    @Inject
    public ReadersUpdateController(ReadersUpdateViewModel readersUpdateViewModel) {
        this.readersUpdateViewModel = readersUpdateViewModel;
    }

    public void initialize() {
        title.setText("Update Reader");
        idTxt.setEditable(false);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        tableReaders.setItems(readersUpdateViewModel.getObservableReaders());

        readersUpdateViewModel.loadReaders();

        readersUpdateViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                readersUpdateViewModel.cleanState();
            }
        });
    }

    @FXML
    private void updateReader() {
        Reader reader = tableReaders.getSelectionModel().getSelectedItem();
        readersUpdateViewModel.updateReader(reader, nameTxt.getText(), datePicker.getValue(), paswordTxt.getText());
    }

    @FXML
    private void updateFields() {
        Reader reader = tableReaders.getSelectionModel().getSelectedItem();
        if (reader != null) {
            idTxt.setText(String.valueOf(reader.getId()));
            nameTxt.setText(reader.getName());
            datePicker.setValue(reader.getDateOfBirth());
        }
    }

}
