package gui.screens.ratings_add;

import domain.modelo.Article;
import domain.modelo.ArticleRating;
import domain.modelo.Reader;
import gui.screens.common.BaseScreenController;
import gui.screens.common.ScreenConstants;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RatingsAddController extends BaseScreenController {

    @FXML
    private Label title;
    @FXML
    private TableView<Article> tableArticles;
    @FXML
    private TableColumn<Article, Integer> columnId;
    @FXML
    private TableColumn<Article, String> columnName;
    @FXML
    private TableColumn<Article, String> columnArticleType;
    @FXML
    private TableColumn<Article, Integer> columnNewspaperId;
    @FXML
    private TableView<ArticleRating> tableRatings;
    @FXML
    private TableColumn<ArticleRating, Integer> columnIdRating;
    @FXML
    private TableColumn<ArticleRating, Integer> columnIdReader;
    @FXML
    private TableColumn<ArticleRating, Integer> columnIdArticle;
    @FXML
    private TableColumn<ArticleRating, Integer> columnRating;
    @FXML
    private MFXTextField idReader;
    @FXML
    private MFXTextField idArticle;
    @FXML
    private MFXTextField rating;

    private final RatingsAddViewModel ratingsAddViewModel;

    @Inject
    public RatingsAddController(RatingsAddViewModel ratingsAddViewModel) {
        this.ratingsAddViewModel = ratingsAddViewModel;
    }

    public void initialize() {
        title.setText("Add a rating");

        columnIdRating.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnIdReader.setCellValueFactory(new PropertyValueFactory<>("idReader"));
        columnIdArticle.setCellValueFactory(new PropertyValueFactory<>("idArticle"));
        columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        tableRatings.setItems(ratingsAddViewModel.getObservableRatings());

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArticleType.setCellValueFactory(new PropertyValueFactory<>("articleType"));
        columnNewspaperId.setCellValueFactory(new PropertyValueFactory<>("newspaperId"));
        tableArticles.setItems(ratingsAddViewModel.getObservableArticles());

        ratingsAddViewModel.getState().addListener((observableValue, oldState, newState) -> {
            if (newState.getError() != null) {
                this.getPrincipalController().showAlert(Alert.AlertType.ERROR, ScreenConstants.ERROR, newState.getError());
                ratingsAddViewModel.cleanState();
            }
            if (newState.isRatingAdded()) {
                ratingsAddViewModel.loadRatings(getPrincipalController().getReader());
                this.getPrincipalController().showAlert(Alert.AlertType.INFORMATION, ScreenConstants.SUCCESS, "Rating added successfully");
                ratingsAddViewModel.cleanState();
            }
        });
    }

    @Override
    public void principalCargado() {
        Reader reader = this.getPrincipalController().getReader();
        ratingsAddViewModel.loadRatings(reader);
        ratingsAddViewModel.loadArticles(reader);
        idReader.setText(String.valueOf(reader.getId()));
        idReader.setEditable(false);
        idReader.setDisable(true);
    }

    @FXML
    private void addRating() {
        ratingsAddViewModel.addRating(idReader.getText(), idArticle.getText(), rating.getText());
    }
}

