package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = processName(name);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Capitalizes the given name and removes excess whitespaces.
     * @param name The name given as an argument
     * @return The processed name, which is capitalized and has excess whitespaces removed.
     */
    public static String processName(String name) {
        String[] words = name.split(" ");
        String word = words[0];
        word = StringUtils.capitalize(word);
        StringBuffer processedName = new StringBuffer(word);
        for (int i = 1; i < words.length; i++) {
            if (words[i].equals("")) {
                continue;
            }
            word = words[i];
            word = word.trim();
            word = StringUtils.capitalize(word);
            processedName = processedName.append(" ").append(word);
        }
        return processedName.toString();
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                && fullName.equalsIgnoreCase(((Name) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
