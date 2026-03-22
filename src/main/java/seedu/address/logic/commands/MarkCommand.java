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
 * Marks a candidate as interviewed in the address book.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the candidate identified by the index number as interviewed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked [%1$d] %2$s as interviewed";
    public static final String MESSAGE_ALREADY_MARKED = "This candidate has already been marked as interviewed.";

    private final Index targetIndex;

    public MarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        // Check if person to mark is already marked
        Person personToMark = lastShownList.get(targetIndex.getZeroBased());
        if (personToMark.isInterviewed()) {
            throw new CommandException(MESSAGE_ALREADY_MARKED);
        }

        // since Person is immutable, create a new Person instance with same fields
        Person editedPerson = new Person(
            personToMark.getName(),
            personToMark.getPhone(),
            personToMark.getEmail(),
            personToMark.getAddress(),
            personToMark.getTags(),
            true,
            personToMark.getRemark()
        );
        model.setPerson(personToMark, editedPerson);
        return new CommandResult(String.format(
            MESSAGE_MARK_PERSON_SUCCESS,
            targetIndex.getOneBased(),
            editedPerson.getName().fullName
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return targetIndex.equals(otherMarkCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
