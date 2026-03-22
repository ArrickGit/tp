package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * Right panel showing selected candidate details.
 */
public class CandidateDetailsPanel extends UiPart<Region> {

    private static final String FXML = "CandidateDetailsPanel.fxml";

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label tagsLabel;

    @FXML
    private Label remarkLabel;

    public CandidateDetailsPanel() {
        super(FXML);
    }

    /**
     * Updates panel with selected person.
     */
    public void setPerson(Person person) {
        if (person == null) {
            clear();
            return;
        }

        nameLabel.setText(person.getName().fullName);
        emailLabel.setText(person.getEmail().value);
        phoneLabel.setText(person.getPhone().value);
        addressLabel.setText(person.getAddress().value);
        tagsLabel.setText(person.getTags().toString());
        remarkLabel.setText(person.getRemark().value);
    }

    /**
     * Clears the panel
     */
    private void clear() {
        nameLabel.setText("Select a candidate");
        emailLabel.setText("");
        phoneLabel.setText("");
        addressLabel.setText("");
        tagsLabel.setText("");
        remarkLabel.setText("");
    }
}
