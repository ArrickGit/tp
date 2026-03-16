package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Abstract class
 * Finds and lists all persons in address book whose name matches the predicate.
 */
public abstract class FilterCommand extends Command {

    private final Predicate<Person> predicate;

    protected FilterCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    /**
     * Getter for Predicate
     * @return Predicate
     */
    protected Predicate<Person> getPredicate() {
        return this.predicate;
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
        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
