package gui.screens.articles_add;

import domain.modelo.Article;
import domain.modelo.ArticleType;
import domain.modelo.Newspaper;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArticlesAddController extends BaseScreenController {

    private final ArticlesAddViewModel articlesAddViewModel;
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
    private MFXTextField inputName;
    @FXML
    private MFXComboBox<ArticleType> comboType;
    @FXML
    private MFXComboBox<Newspaper> comboNewspaper;

    @Inject
    public ArticlesAddController(ArticlesAddViewModel articlesAddViewModel) {
        this.articlesAddViewModel = articlesAddViewModel;
    }

    public void initialize() {
        title.setText("Append new Article");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArticleType.setCellValueFactory(new PropertyValueFactory<>("articleType"));
        columnNewspaperId.setCellValueFactory(new PropertyValueFactory<>("newspaperId"));
        tableArticles.setItems(articlesAddViewModel.getObservableArticles());
        comboType.setItems(articlesAddViewModel.getObservableArticleTypes());
        comboNewspaper.setItems(articlesAddViewModel.getObservableNewspapers());

        articlesAddViewModel.loadArticles();
        articlesAddViewModel.loadArticleTypes();
        articlesAddViewModel.loadNewspapers();

        articlesAddViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                getPrincipalController().showAlert(Alert.AlertType.ERROR, "Error", newState.getError());
                articlesAddViewModel.cleanState();
            }
            if (newState.isArticleAdded()) {
                getPrincipalController().showAlert(Alert.AlertType.INFORMATION, ScreenConstants.SUCCESS, "Article added successfully");
                articlesAddViewModel.cleanState();
            }
        });
    }


    public void addArticle() {
        articlesAddViewModel.addArticle(inputName.getText(),
                comboType.getValue(), comboNewspaper.getValue());
    }

}

