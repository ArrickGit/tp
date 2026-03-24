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
 * Deletes person(s) identified by index numbers, or all persons in the current filtered list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String DELETE_ALL_KEYWORD = "all";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person(s) identified by the index numbers used in the displayed person list,"
            + " or deletes all currently displayed persons using 'all'.\n"
            + "Parameters: INDEX [MORE_INDEXES]... (each must be a positive integer) | all\n"
            + "Example: " + COMMAND_WORD + " 1 2 3\n"
            + "Example: " + COMMAND_WORD + " all";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted:\n%1$s";
    public static final String MESSAGE_DELETE_ALL_SUCCESS = "Deleted all %1$d displayed candidate(s).";
    public static final String MESSAGE_NO_PERSONS_TO_DELETE = "No candidates to delete — the list is empty.";

    private final List<Index> targetIndexes;
    private final boolean deleteAll;

    /** Constructor for index-based deletion. */
    public DeleteCommand(List<Index> targetIndexes) {
        assert targetIndexes != null : "targetIndexes must not be null";
        assert !targetIndexes.isEmpty() : "targetIndexes must not be empty";
        this.targetIndexes = targetIndexes;
        this.deleteAll = false;
    }

    /** Constructor for delete-all mode. */
    public DeleteCommand(boolean deleteAll) {
        assert deleteAll : "Use the index-based constructor for non-deleteAll cases";
        this.targetIndexes = List.of();
        this.deleteAll = true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (deleteAll) {
            return executeDeleteAll(model);
        } else {
            return executeDeleteByIndexes(model);
        }
    }

    private CommandResult executeDeleteAll(Model model) throws CommandException {
        assert deleteAll : "executeDeleteAll should only be called when deleteAll is true";

        List<Person> currentList = model.getFilteredPersonList();

        if (currentList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PERSONS_TO_DELETE);
        }

        int count = currentList.size();

        // Snapshot to avoid ConcurrentModificationException during deletion
        List<Person> toDelete = List.copyOf(currentList);
        assert toDelete.size() == count : "Snapshot size must match original list size";

        for (Person person : toDelete) {
            assert person != null : "Person in list must not be null";
            model.deletePerson(person);
        }

        return new CommandResult(String.format(MESSAGE_DELETE_ALL_SUCCESS, count));
    }

    private CommandResult executeDeleteByIndexes(Model model) throws CommandException {
        assert !targetIndexes.isEmpty() : "targetIndexes must not be empty for index-based deletion";

        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate all indices before deleting anything
        for (Index index : targetIndexes) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Sort descending so deletions don't shift subsequent indices
        List<Index> sortedIndexes = targetIndexes.stream()
                .sorted(Comparator.comparingInt(Index::getZeroBased).reversed())
                .collect(Collectors.toList());

        assert sortedIndexes.size() == targetIndexes.size() : "Sorted list must preserve all indexes";

        StringBuilder deletedPersons = new StringBuilder();
        for (Index index : sortedIndexes) {
            Person personToDelete = model.getFilteredPersonList().get(index.getZeroBased());
            assert personToDelete != null : "Person at valid index must not be null";
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
        return deleteAll == otherDeleteCommand.deleteAll
                && targetIndexes.equals(otherDeleteCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .add("deleteAll", deleteAll)
                .toString();
    }
}
