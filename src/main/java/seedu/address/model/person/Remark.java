package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a remark about a Person in the address book.
 * Guarantees: immutable; is always valid
 */
public class Remark {

    public static final String MESSAGE_CONSTRAINTS = "Remark can take any values, and it should not be blank";

    public final String value;

    /**
     * Constructs an {@code Remark}.
     *
     * @param address A valid remark.
     */
    public Remark(String address) {
        requireNonNull(address);
        value = address;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}