package gui.screens.readers_list;

import domain.modelo.ArticleType;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import gui.screens.common.BaseScreenController;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class ReadersListController extends BaseScreenController {


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
    private MFXComboBox<ArticleType> cbArticleType;
    @FXML
    private MFXComboBox<Newspaper> cbNewspaper;

    private final ReadersListViewModel readersListViewModel;

    @Inject
    public ReadersListController(ReadersListViewModel readersListViewModel) {
        this.readersListViewModel = readersListViewModel;
    }

    public void initialize() {
        title.setText("List all Readers");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        tableReaders.setItems(readersListViewModel.getObservableReaders());
        cbArticleType.setItems(readersListViewModel.getObservableArticleTypes());
        cbNewspaper.setItems(readersListViewModel.getObservableNewspapers());

        readersListViewModel.loadReaders();
        readersListViewModel.loadArticleTypes();
        readersListViewModel.loadNewspapers();

        readersListViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                readersListViewModel.cleanState();
            }
        });
    }

    public void filterByArticleType() {
        ArticleType articleType = cbArticleType.getSelectionModel().getSelectedItem();
        readersListViewModel.filterByArticleType(articleType);
    }

    public void filterByNewspaper() {
        Newspaper newspaper = cbNewspaper.getSelectionModel().getSelectedItem();
        readersListViewModel.filterByNewspaper(newspaper);
    }
}
