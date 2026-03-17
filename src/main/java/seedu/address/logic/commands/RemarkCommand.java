package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Adds or edits a remark for a candidate in the address book.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds or edits the remark of the candidate identified "
            + "by the index number used in the displayed candidate list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + " REMARK\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Strong in algorithms.";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to candidate: %1$s";

    private final Index targetIndex;
    private final Remark remark;

    /**
     * Creates a RemarkCommand to add or edit the remark of the person at {@code targetIndex}.
     */
    public RemarkCommand(Index targetIndex, Remark remark) {
        requireNonNull(targetIndex);
        requireNonNull(remark);
        this.targetIndex = targetIndex;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(),
                personToEdit.isInterviewed(), remark);

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_ADD_REMARK_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand otherRemarkCommand = (RemarkCommand) other;
        return targetIndex.equals(otherRemarkCommand.targetIndex)
                && remark.equals(otherRemarkCommand.remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("remark", remark)
                .toString();
    }
}

