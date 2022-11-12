package gui.screens.articles_list_with_newspaper;

import domain.modelo.ArticleQuery2;
import domain.modelo.ArticleType;
import gui.screens.common.BaseScreenController;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArticlesListWithNewspaperController extends BaseScreenController {

    private final ArticlesListWithNewspaperViewModel articlesListWithNewspaperViewModel;
    @FXML
    private Label title;
    @FXML
    private TableView<ArticleQuery2> tableArticles;
    @FXML
    private TableColumn<ArticleQuery2, Integer> columnId;
    @FXML
    private TableColumn<ArticleQuery2, String> columnName;
    @FXML
    private TableColumn<ArticleQuery2, Integer> columnArticleTypeId;
    @FXML
    private TableColumn<ArticleQuery2, Integer> columnNewspaperId;
    @FXML
    private TableColumn<ArticleQuery2, String> columnArticleTypeDescription;
    @FXML
    private TableColumn<ArticleQuery2, String> columnNewspaperName;
    @FXML
    private MFXComboBox<ArticleType> cbArticleType;


    @Inject
    public ArticlesListWithNewspaperController(ArticlesListWithNewspaperViewModel articlesListWithNewspaperViewModel) {
        this.articlesListWithNewspaperViewModel = articlesListWithNewspaperViewModel;
    }

    public void initialize() {
        title.setText("List Articles by type with newspaper");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name_article"));
        columnArticleTypeId.setCellValueFactory(new PropertyValueFactory<>("id_type"));
        columnNewspaperId.setCellValueFactory(new PropertyValueFactory<>("id_newspaper"));
        columnArticleTypeDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnNewspaperName.setCellValueFactory(new PropertyValueFactory<>("name_newspaper"));
        tableArticles.setItems(articlesListWithNewspaperViewModel.getObservableArticles());
        cbArticleType.setItems(articlesListWithNewspaperViewModel.getObservableArticleTypes());

        articlesListWithNewspaperViewModel.loadArticleTypes();

        articlesListWithNewspaperViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                articlesListWithNewspaperViewModel.cleanState();
            }
        });
    }

    @FXML
    private void filter() {
        articlesListWithNewspaperViewModel.filter(cbArticleType.getValue());
    }

}

