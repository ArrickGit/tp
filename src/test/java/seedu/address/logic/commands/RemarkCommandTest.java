package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    private static final Remark REMARK_STUB = new Remark("Strong in algorithms.");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withRemark(REMARK_STUB.value).build();
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, REMARK_STUB);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, REMARK_STUB);

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RemarkCommand remarkFirstCommand = new RemarkCommand(INDEX_FIRST_PERSON, REMARK_STUB);
        RemarkCommand remarkSecondCommand = new RemarkCommand(INDEX_SECOND_PERSON, REMARK_STUB);

        // same object -> returns true
        assertTrue(remarkFirstCommand.equals(remarkFirstCommand));

        // same values -> returns true
        RemarkCommand remarkFirstCommandCopy = new RemarkCommand(INDEX_FIRST_PERSON, REMARK_STUB);
        assertTrue(remarkFirstCommand.equals(remarkFirstCommandCopy));

        // different types -> returns false
        assertFalse(remarkFirstCommand.equals(1));

        // null -> returns false
        assertFalse(remarkFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(remarkFirstCommand.equals(remarkSecondCommand));

        // different remark -> returns false
        assertFalse(remarkFirstCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark(""))));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        RemarkCommand remarkCommand = new RemarkCommand(targetIndex, REMARK_STUB);
        String expected = RemarkCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", remark=" + REMARK_STUB + "}";
        assertEquals(expected, remarkCommand.toString());
    }
}
