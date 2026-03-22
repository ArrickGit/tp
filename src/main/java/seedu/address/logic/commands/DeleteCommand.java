package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person(s) identified by the index numbers used in the displayed person list.\n"
            + "Parameters: INDEX [MORE_INDEXES]... (each must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted:\n%1$s";

    private final List<Index> targetIndexes;

    public DeleteCommand(List<Index> targetIndexes) {
        this.targetIndexes = targetIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate all indices first before deleting anything
        for (Index index : targetIndexes) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Sort descending so deletions don't shift subsequent indices
        List<Index> sortedIndexes = targetIndexes.stream()
                .sorted(Comparator.comparingInt(Index::getZeroBased).reversed())
                .collect(Collectors.toList());

        StringBuilder deletedPersons = new StringBuilder();
        for (Index index : sortedIndexes) {
            Person personToDelete = model.getFilteredPersonList().get(index.getZeroBased());
            model.deletePerson(personToDelete);
            deletedPersons
                    .append("[")
                    .append(index.getOneBased())
                    .append("] ")
                    .append(personToDelete.getName().fullName)
                    .append(" deleted")
                    .append("\n");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPersons.toString().trim()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteCommand)) {
            return false;
        }
        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndexes.equals(otherDeleteCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .toString();
    }
}
