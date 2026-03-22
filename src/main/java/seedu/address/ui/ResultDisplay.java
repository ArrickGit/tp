package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextFlow resultDisplay;

    @FXML
    private ScrollPane commandScroll;

    public ResultDisplay() {
        super(FXML);
    }

    private void appendLine(String text, String style) {
        Text line = new Text(text + "\n");
        line.getStyleClass().add("command-text");

        if (style != null) {
            line.getStyleClass().add(style);
        }

        resultDisplay.getChildren().add(0, line);
        Platform.runLater(() -> commandScroll.setVvalue(0));
    }

    public void showCommand(String command) {
        appendLine("> " + command, "command-info");
    }

    public void showSuccess(String message) {
        appendLine(message, "command-success");
    }

    public void showError(String message) {
        appendLine("[ERROR] " + message, "command-error");
    }

    public void clear() {
        resultDisplay.getChildren().clear();
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        showSuccess(feedbackToUser);
    }

}
