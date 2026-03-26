package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AVA;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.IsInterviewedPredicate;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code FilterCommand}.
 */
public class FilterCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // ──────────────────────────────────────────────
    // equals() tests
    // ──────────────────────────────────────────────

    @Test
    public void equals_sameObject_returnsTrue() {
        FilterCommand command = new FilterCommand(new IsInterviewedPredicate(true));
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_samePredicate_returnsTrue() {
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(true);
        FilterCommand command1 = new FilterCommand(predicate);
        FilterCommand command2 = new FilterCommand(predicate);
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentPredicate_returnsFalse() {
        FilterCommand command1 = new FilterCommand(new IsInterviewedPredicate(true));
        FilterCommand command2 = new FilterCommand(new IsInterviewedPredicate(false));
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_symmetry() {
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(false);
        FilterCommand command1 = new FilterCommand(predicate);
        FilterCommand command2 = new FilterCommand(predicate);
        assertTrue(command1.equals(command2));
        assertTrue(command2.equals(command1));
    }

    @Test
    public void equals_null_returnsFalse() {
        FilterCommand command = new FilterCommand(new IsInterviewedPredicate(true));
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_int_returnsFalse() {
        FilterCommand command = new FilterCommand(new IsInterviewedPredicate(true));
        assertFalse(command.equals(42));
    }

    @Test
    public void equals_differentType_string_returnsFalse() {
        FilterCommand command = new FilterCommand(new IsInterviewedPredicate(true));
        assertFalse(command.equals("filter"));
    }

    // ──────────────────────────────────────────────
    // execute() tests
    // All 8 typical persons default to isInterviewed = false.
    // ──────────────────────────────────────────────

    @Test
    public void execute_filterInterviewedTrue_noPersonFound() {
        // No typical person has isInterviewed = true
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(true);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_filterInterviewedFalse_allPersonsFound() {
        // All 8 typical persons have isInterviewed = false
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 8);
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(false);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, AVA, GEORGE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_filterInterviewedFalse_listNotEmpty() {
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(false);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 8), expectedModel);
        assertFalse(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_filterInterviewedTrue_listEmpty() {
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(true);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0), expectedModel);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        FilterCommand command = new FilterCommand(new IsInterviewedPredicate(false));
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_calledTwiceWithDifferentPredicates_listUpdatesEachTime() {
        // First call: interviewed = true → empty list
        new FilterCommand(new IsInterviewedPredicate(true)).execute(model);
        assertTrue(model.getFilteredPersonList().isEmpty());

        // Second call: interviewed = false → all 8 persons
        new FilterCommand(new IsInterviewedPredicate(false)).execute(model);
        assertEquals(8, model.getFilteredPersonList().size());
    }

    // ──────────────────────────────────────────────
    // toString() tests
    // ──────────────────────────────────────────────

    @Test
    public void toString_notNull() {
        FilterCommand command = new FilterCommand(new IsInterviewedPredicate(true));
        assertNotNull(command.toString());
    }

    @Test
    public void toString_containsPredicateField() {
        FilterCommand command = new FilterCommand(new IsInterviewedPredicate(true));
        assertTrue(command.toString().contains("predicate"));
    }

    @Test
    public void toString_matchesExpectedFormat() {
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(true);
        FilterCommand command = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void toString_twoCommandsSamePredicate_equal() {
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(false);
        FilterCommand command1 = new FilterCommand(predicate);
        FilterCommand command2 = new FilterCommand(predicate);
        assertEquals(command1.toString(), command2.toString());
    }
}
