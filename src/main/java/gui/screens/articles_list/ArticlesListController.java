package gui.screens.articles_list;

import domain.modelo.Article;
import domain.modelo.ArticleQuery1;
import domain.modelo.ArticleType;
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
import javafx.scene.input.MouseEvent;

public class ArticlesListController extends BaseScreenController {

    @FXML
    private Label infoLabel;
    @FXML
    private Label title;
    @FXML
    private TableView<Article> tableArticles;
    @FXML
    private TableColumn<Article, Integer> columnId;
    @FXML
    private TableColumn<Article, String> columnName;
    @FXML
    private TableColumn<Article, Integer> columnArticleType;
    @FXML
    private TableColumn<Article, Integer> columnNewspaperId;
    @FXML
    private MFXComboBox<ArticleType> cbArticleType;
    @FXML
    private MFXButton btnFilter;

    private final ArticlesListViewModel articlesListViewModel;

    @Inject
    public ArticlesListController(ArticlesListViewModel articlesListViewModel) {
        this.articlesListViewModel = articlesListViewModel;
    }

    public void initialize() {
        title.setText("List all Articles");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArticleType.setCellValueFactory(new PropertyValueFactory<>("articleType"));
        columnNewspaperId.setCellValueFactory(new PropertyValueFactory<>("newspaperId"));
        tableArticles.setItems(articlesListViewModel.getObservableArticles());
        cbArticleType.setItems(articlesListViewModel.getObservableArticleTypes());

        articlesListViewModel.loadArticles();
        articlesListViewModel.loadArticleTypes();

        articlesListViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                articlesListViewModel.cleanState();
            }
        });
    }

    @FXML
    private void filter() {
        articlesListViewModel.filter(cbArticleType.getValue());
    }

    @FXML
    private void updateInfoLabel(MouseEvent mouseEvent) {
        Article article = tableArticles.getSelectionModel().getSelectedItem();
        if (article != null) {
            ArticleQuery1 articleQuery1 = articlesListViewModel.getArticleQuery(article.getId());
            infoLabel.setText(" - Article name: " + article.getName() +
                    "\n - Article Id: " + articleQuery1.getId() +
                    "\n - Article Type: " + articleQuery1.getDescription() +
                    "\n - Number of readers: " + articleQuery1.getReaders());
        }
    }
}

