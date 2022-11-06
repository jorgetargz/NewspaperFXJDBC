package gui.screens.newspapers_delete;

import lombok.Data;

@Data
public class NewspaperDeleteState {
    private final String error;
    private final boolean onDeleteNewspaperWithArticles;
}
