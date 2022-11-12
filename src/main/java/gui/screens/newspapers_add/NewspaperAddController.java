package gui.screens.newspapers_add;

import domain.modelo.Newspaper;
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

public class NewspaperAddController extends BaseScreenController {

    private final NewspaperAddViewModel newspaperAddViewModel;
    @FXML
    private MFXTextField nameTxt;
    @FXML
    private MFXDatePicker releaseDatePicker;
    @FXML
    private Label title;
    @FXML
    private TableView<Newspaper> tableNewspapers;
    @FXML
    private TableColumn<Newspaper, Integer> columnId;
    @FXML
    private TableColumn<Newspaper, String> columnName;
    @FXML
    private TableColumn<Newspaper, LocalDate> columnPublishDate;

    @Inject
    public NewspaperAddController(NewspaperAddViewModel newspaperAddViewModel) {
        this.newspaperAddViewModel = newspaperAddViewModel;
    }

    public void initialize() {
        title.setText("Add newspapers");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name_newspaper"));
        columnPublishDate.setCellValueFactory(new PropertyValueFactory<>("release_date"));
        tableNewspapers.setItems(newspaperAddViewModel.getObservableNewspapers());

        newspaperAddViewModel.loadNewspapers();

        newspaperAddViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                newspaperAddViewModel.cleanState();
            }
            if (newState.isNewspaperAdded()) {
                this.getPrincipalController().showAlert(Alert.AlertType.INFORMATION, ScreenConstants.SUCCESS, "Newspaper added successfully");
                newspaperAddViewModel.cleanState();
            }
        });
    }

    @FXML
    private void addNewspaper() {
        String name = nameTxt.getText();
        LocalDate releaseDate = releaseDatePicker.getValue();
        if (name.isEmpty() || releaseDate == null) {
            this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, "Please fill all the fields");
        } else {
            newspaperAddViewModel.addNewspaper(name, releaseDate);
        }
    }
}
