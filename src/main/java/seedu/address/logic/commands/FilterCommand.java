package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose details match the given predicate
 */
public class FilterCommand extends PredicateCommand {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Filters the current list of persons by the given parameters\n"
        + "Parameters: -interviewed (y/n/0/1)\n"
        + "Example: " + COMMAND_WORD + " -interviewed y";

    public FilterCommand(Predicate<Person> predicate) {
        super(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(this.getPredicate());
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return this.getPredicate().equals(otherFilterCommand.getPredicate());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", this.getPredicate())
                .toString();
    }
}
