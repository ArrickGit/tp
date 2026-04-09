package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS =
            "Addresses must be at most 50 characters long and may only contain "
            + "English letters (A-Z, a-z), digits (0-9), spaces, and the following "
            + "punctuation: , . - # / '. The address must not be blank.";

    /*
     * Address rules:
     * - Must start with an English letter or digit (no leading spaces).
     * - May contain English letters, digits, spaces, commas, periods,
     *   hyphens, hash signs, and forward slashes.
     * - Maximum length is 50 characters.
     */
    public static final String VALIDATION_REGEX = "[A-Za-z0-9][A-Za-z0-9 ,.\\-#/\\']{0,49}";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidAddress(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
