package gui.screens.articles_list_with_bad_rating;

import domain.modelo.ArticleQuery3;
import domain.modelo.Newspaper;
import gui.screens.common.BaseScreenController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArticlesListWithBadRatingController extends BaseScreenController {


    @FXML
    private Label title;
    @FXML
    private TableView<ArticleQuery3> tableArticles;
    @FXML
    private TableColumn<ArticleQuery3, Integer> columnId;
    @FXML
    private TableColumn<ArticleQuery3, String> columnName;
    @FXML
    private TableColumn<ArticleQuery3, Integer> columnArticleTypeId;
    @FXML
    private TableColumn<ArticleQuery3, Integer> columnNewspaperId;
    @FXML
    private TableColumn<ArticleQuery3, Integer> columnReaderId;
    @FXML
    private TableColumn<ArticleQuery3, Integer> columnRating;
    @FXML
    private TableColumn<ArticleQuery3, String> columnIsCritical;
    @FXML
    private MFXComboBox<Newspaper> cbNewspapers;
    @FXML
    private MFXButton btnFilter;

    private final ArticlesListWithBadRatingViewModel articlesListWithBadRatingViewModel;

    @Inject
    public ArticlesListWithBadRatingController(ArticlesListWithBadRatingViewModel articlesListWithBadRatingViewModel) {
        this.articlesListWithBadRatingViewModel = articlesListWithBadRatingViewModel;
    }

    public void initialize() {
        title.setText("List Articles by newspaper with bad ratings");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("nameArticle"));
        columnArticleTypeId.setCellValueFactory(new PropertyValueFactory<>("idType"));
        columnNewspaperId.setCellValueFactory(new PropertyValueFactory<>("idNewspaper"));
        columnReaderId.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        columnIsCritical.setCellValueFactory(new PropertyValueFactory<>("critical"));
        tableArticles.setItems(articlesListWithBadRatingViewModel.getObservableArticles());
        cbNewspapers.setItems(articlesListWithBadRatingViewModel.getObservableNewspapers());

        articlesListWithBadRatingViewModel.loadNewspapers();

        articlesListWithBadRatingViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                articlesListWithBadRatingViewModel.cleanState();
            }
        });
    }

    @FXML
    private void filter() {
        articlesListWithBadRatingViewModel.filter(cbNewspapers.getValue());
    }

}

