package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    private static final int MAX_LENGTH = 80;

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain English letters (A-Z, a-z) and spaces, "
            + "and it should not be blank. No digits or special characters are allowed."
            + "Name must be at most " + MAX_LENGTH + " characters long.";

    /*
     * The first character must be an English letter (A-Za-z).
     * Subsequent characters may be English letters or spaces.
     * Digits and special characters are not permitted.
     */
    public static final String VALIDATION_REGEX = "[A-Za-z][A-Za-z ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     * Name must match the alphanumeric regex and not exceed {@code MAX_LENGTH} characters.
     */
    public static boolean isValidName(String test) {
        requireNonNull(test);
        return test.matches(VALIDATION_REGEX) && test.length() <= MAX_LENGTH;
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    /**
     * Provides a case-insensitive comparison and is intended for scenarios
     * such as duplicate detection where names like "John Doe" and "john doe"
     * should be considered the same.
     *
     * @param other  The other {@code Name} to compare with.
     * @return {@code true} if both names are equal ignoring case, {@code false} otherwise.
     * @throws NullPointerException if {@code other} is null.
     */
    public boolean equalsIgnoreCase(Name other) {
        requireNonNull(other);
        return this.fullName.equalsIgnoreCase(other.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
