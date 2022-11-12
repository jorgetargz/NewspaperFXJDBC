package gui.screens.newspapers_list;

import domain.modelo.Newspaper;
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

public class NewspaperListController extends BaseScreenController {

    private final NewspaperListViewModel newspaperListViewModel;
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
    public NewspaperListController(NewspaperListViewModel newspaperListViewModel) {
        this.newspaperListViewModel = newspaperListViewModel;
    }

    public void initialize() {
        title.setText("List of newspapers");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name_newspaper"));
        columnPublishDate.setCellValueFactory(new PropertyValueFactory<>("release_date"));
        tableNewspapers.setItems(newspaperListViewModel.getObservableNewspapers());

        newspaperListViewModel.loadNewspapers();

        newspaperListViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                newspaperListViewModel.cleanState();
            }
        });
    }

}
