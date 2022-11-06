package gui.screens.ratings_list;

import domain.modelo.ArticleRating;
import domain.services.ServicesRatings;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RatingsListViewModel {

    private final ServicesRatings servicesRatings;
    private final ObjectProperty<RatingsListState> state;
    private final ObservableList<ArticleRating> observableRatings;

    @Inject
    public RatingsListViewModel(ServicesRatings servicesRatings) {
        this.servicesRatings = servicesRatings;
        state = new SimpleObjectProperty<>(new RatingsListState(null));
        observableRatings = FXCollections.observableArrayList();
    }

    public ReadOnlyObjectProperty<RatingsListState> getState() {
        return state;
    }

    public ObservableList<ArticleRating> getObservableRatings() {
        return FXCollections.unmodifiableObservableList(observableRatings);
    }

    public void loadRatings() {
        List<ArticleRating> ratings = servicesRatings.getRatings();
        if (ratings.isEmpty()) {
            state.set(new RatingsListState("There are no ratings"));
        } else {
            observableRatings.clear();
            observableRatings.setAll(ratings);
        }
    }

    public void cleanState() {
        state.set(new RatingsListState(null));
    }
}
