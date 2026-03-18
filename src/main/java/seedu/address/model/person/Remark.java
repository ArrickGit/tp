package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid.
 */
public class Remark {

    public static final String DEFAULT_REMARK = "";

    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark Any string value.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark;
    }

    /**
     * Returns the value of the remark
     * <p>
     * If the remarks is cleared, this will return an empty string
     * @return the remark as a string, never null
     */
    public boolean isEmpty() {
        return value.equals(DEFAULT_REMARK);
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
        if (!(other instanceof Remark)) {
            return false;
        }

        Remark otherRemark = (Remark) other;
        return value.equals(otherRemark.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
