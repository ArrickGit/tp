package seedu.address.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private TableView<Person> personTableView;
    @FXML
    private TableColumn<Person, Person> indexColumn;
    @FXML
    private TableColumn<Person, Person> nameColumn;
    @FXML
    private TableColumn<Person, String> emailColumn;
    @FXML
    private TableColumn<Person, String> phoneColumn;
    @FXML
    private TableColumn<Person, String> addressColumn;
    @FXML
    private TableColumn<Person, String> remarkColumn;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        personTableView.setFixedCellSize(64);

        indexColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        indexColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Person person, boolean empty) {
                super.updateItem(person, empty);
                if (empty || person == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        nameColumn.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(cellData.getValue()));
        emailColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getEmail().value));
        phoneColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getPhone().value));
        addressColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getAddress().value));
        remarkColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getRemark().isEmpty() ? "-" : cellData.getValue().getRemark().value));

        emailColumn.setCellFactory(createWrappedTextCellFactory());
        phoneColumn.setCellFactory(createWrappedTextCellFactory());
        addressColumn.setCellFactory(createWrappedTextCellFactory());
        remarkColumn.setCellFactory(createWrappedTextCellFactory());

        nameColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Person person, boolean empty) {
                super.updateItem(person, empty);
                setText(null);
                setGraphic(null);

                if (empty || person == null) {
                    return;
                }

                Label nameLabel = new Label(person.getName().fullName);
                nameLabel.getStyleClass().add("name-main");
                nameLabel.setWrapText(true);
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(nameLabel, Priority.ALWAYS);

                Label interviewedBadge = new Label(person.isInterviewed() ? "Interviewed" : "Not Interviewed");
                interviewedBadge.getStyleClass().add("interview-badge");
                interviewedBadge.getStyleClass().add(person.isInterviewed() ? "interview-badge-yes" : "interview-badge-no");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox topRow = new HBox(8, nameLabel, spacer, interviewedBadge);
                topRow.setAlignment(Pos.CENTER_LEFT);

                FlowPane tagsPane = new FlowPane();
                tagsPane.setHgap(6);
                tagsPane.setVgap(4);

                if (person.getTags().isEmpty()) {
                    Label noTagsLabel = new Label("No tags");
                    noTagsLabel.getStyleClass().add("name-tags");
                    noTagsLabel.setWrapText(true);
                    tagsPane.getChildren().add(noTagsLabel);
                } else {
                    person.getTags().stream()
                            .sorted(Comparator.comparing(Tag::toString))
                            .map(tag -> {
                                Label tagLabel = new Label(tag.tagName);
                                tagLabel.getStyleClass().addAll("name-tags", "tag-chip");
                                tagLabel.setWrapText(true);
                                return tagLabel;
                            })
                            .forEach(tagsPane.getChildren()::add);
                }

                VBox container = new VBox(1, topRow, tagsPane);
                container.getStyleClass().add("name-cell-container");
                setGraphic(container);
            }
        });

        personTableView.setItems(personList);
    }

    private Callback<TableColumn<Person, String>, TableCell<Person, String>> createWrappedTextCellFactory() {
        return column -> new TableCell<>() {
            private final Label wrappedLabel = new Label();

            {
                wrappedLabel.setWrapText(true);
                wrappedLabel.maxWidthProperty().bind(column.widthProperty().subtract(16));
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(wrappedLabel);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    wrappedLabel.setText(null);
                    setGraphic(null);
                } else {
                    wrappedLabel.setText(item);
                    setGraphic(wrappedLabel);
                }
            }
        };
    }
}
