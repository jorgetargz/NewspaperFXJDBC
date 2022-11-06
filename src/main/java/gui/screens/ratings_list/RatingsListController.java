package gui.screens.ratings_list;

import domain.modelo.ArticleRating;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RatingsListController extends BaseScreenController {

    @FXML
    private Label title;
    @FXML
    private TableView<ArticleRating> tableRatings;
    @FXML
    private TableColumn<ArticleRating, Integer> columnId;
    @FXML
    private TableColumn<ArticleRating, Integer> columnIdReader;
    @FXML
    private TableColumn<ArticleRating, Integer> columnIdArticle;
    @FXML
    private TableColumn<ArticleRating, Integer> columnRating;

    private final RatingsListViewModel ratingsListViewModel;

    @Inject
    public RatingsListController(RatingsListViewModel ratingsListViewModel) {
        this.ratingsListViewModel = ratingsListViewModel;
    }

    public void initialize() {
        title.setText("List of ratings");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnIdReader.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        columnIdArticle.setCellValueFactory(new PropertyValueFactory<>("idArticle"));
        columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tableRatings.setItems(ratingsListViewModel.getObservableRatings());

        ratingsListViewModel.loadRatings();

        ratingsListViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                ratingsListViewModel.cleanState();
            }
        });
    }
}
