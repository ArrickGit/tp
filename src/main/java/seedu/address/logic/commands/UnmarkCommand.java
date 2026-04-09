package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Unmarks a candidate as interviewed in the address book.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the candidate identified by the index number as interviewed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Unmarked [%1$d] %2$s (not interviewed)";
    public static final String MESSAGE_ALREADY_UNMARKED = "This candidate is already marked as not interviewed.";

    private final Index targetIndex;

    /**
     * Creates an UnmarkCommand to unmark the person at {@code targetIndex}.
     */
    public UnmarkCommand(Index targetIndex) {
        requireNonNull(targetIndex, "targetIndex must not be null");
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnmark = lastShownList.get(targetIndex.getZeroBased());
        if (!personToUnmark.isInterviewed()) {
            throw new CommandException(MESSAGE_ALREADY_UNMARKED);
        }

        Person editedPerson = new Person(
                personToUnmark.getName(),
                personToUnmark.getPhone(),
                personToUnmark.getEmail(),
                personToUnmark.getAddress(),
                personToUnmark.getTags(),
                false,
                personToUnmark.getRemark());

        model.setPerson(personToUnmark, editedPerson);
        return new CommandResult(String.format(
            MESSAGE_UNMARK_PERSON_SUCCESS,
            targetIndex.getOneBased(),
            editedPerson.getName().fullName
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return targetIndex.equals(otherUnmarkCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
