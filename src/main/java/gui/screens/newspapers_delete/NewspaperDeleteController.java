package gui.screens.newspapers_delete;

import domain.modelo.Newspaper;
import gui.screens.common.BaseScreenController;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Optional;

public class NewspaperDeleteController extends BaseScreenController {

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

    private final NewspaperDeleteViewModel newspaperDeleteViewModel;

    @Inject
    public NewspaperDeleteController(NewspaperDeleteViewModel newspaperDeleteViewModel) {
        this.newspaperDeleteViewModel = newspaperDeleteViewModel;
    }

    public void initialize() {
        title.setText("Delete newspaper");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name_newspaper"));
        columnPublishDate.setCellValueFactory(new PropertyValueFactory<>("release_date"));
        tableNewspapers.setItems(newspaperDeleteViewModel.getObservableNewspapers());

        newspaperDeleteViewModel.loadNewspapers();

        newspaperDeleteViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                newspaperDeleteViewModel.cleanState();
            }
            if (newState.isOnDeleteNewspaperWithArticles()) {
                Newspaper newspaper = tableNewspapers.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getButtonTypes().remove(ButtonType.OK);
                alert.getButtonTypes().add(ButtonType.CANCEL);
                alert.getButtonTypes().add(ButtonType.YES);
                alert.setTitle("Confirm delete");
                alert.setHeaderText("Confirm delete");
                alert.setContentText("This will delete the newspaper and all its articles. Are you sure you want to continue?");
                Optional<ButtonType> res = alert.showAndWait();
                res.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.YES) {
                        newspaperDeleteViewModel.deleteNewspaperConfirm(newspaper);
                    }
                });
                newspaperDeleteViewModel.cleanState();
            }
        });
    }

    @FXML
    private void deleteNewspaper() {
        Newspaper newspaper = tableNewspapers.getSelectionModel().getSelectedItem();
        newspaperDeleteViewModel.deleteNewspaper(newspaper);
    }

}
