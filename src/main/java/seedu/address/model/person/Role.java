package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Role in the employee book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRole(String)}
 */
public class Role {

    public static final String MESSAGE_CONSTRAINTS =
            "Roles must start with an capital/non-capital alphabet, followed by any number of alphanumeric"
                    + " characters, and it should not be blank."
                    + "Special characters such as brackets, hyphens, ampersands and slashes are allowed.";

    /*
     * Role must start with an alphabet, which can be followed by any number of alphanumeric characters.
     * Special characters such as brackets, hyphens, ampersands and slashes are allowed.
     * Brackets are only allowed to have a nested depth of 2.
     */
    public static final String VALIDATION_REGEX =
            "^[a-zA-Z][a-zA-Z0-9&/\\-, ]*[\\[(]?[a-zA-Z0-9&/\\-, ]*[\\[\\]()&]?[a-zA-Z0-9/&\\- ]*[)\\]]{0,2}";

    public final String value;

    /**
     * Constructs a {@code Role}.
     *
     * @param roleName A valid Role Name.
     */
    public Role(String roleName) {
        requireNonNull(roleName);
        checkArgument(isValidRole(roleName), MESSAGE_CONSTRAINTS);
        this.value = roleName;
    }

    /**
     * Returns true if a given string is a valid name for a role.
     */
    public static boolean isValidRole(String test) {
        requireNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Role // instanceof handles nulls
                && value.equals(((Role) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
