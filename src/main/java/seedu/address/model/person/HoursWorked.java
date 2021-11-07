package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;


/**
 * Represents a Person's worked hours in the employee book.
 * Guarantees: immutable; is valid as declared in {@link #isValidHoursWorked(String)}
 */
public class HoursWorked {

    public static final int MIN_HOURS_WORKED = 0;
    public static final int MAX_HOURS_WORKED = 744; // 24 hours * 31 days
    public static final String MESSAGE_CONSTRAINTS =
            "Hours Worked should only contain an integer value between "
                    + MIN_HOURS_WORKED + " and " + MAX_HOURS_WORKED
                    + " (both inclusive), and it should not be blank.";

    public final int value;

    /**
     * Constructs a {@code HoursWorked}.
     *
     * @param amount A valid value in hours.
     */
    public HoursWorked(String amount) {
        requireNonNull(amount);
        checkArgument(isValidHoursWorked(amount), MESSAGE_CONSTRAINTS);
        this.value = Integer.parseInt(amount);
    }

    /**
     * Returns true if a given numerical string is non-negative
     * that is within bounds (between MIN_HOURS_WORKED and MAX_HOURS_WORKED, both inclusive).
     *
     * @param test The string input that is to be parsed into an integer.
     * @return True if the string is a non-negative integer and within bounds, false otherwise.
     */
    public static boolean isValidHoursWorked(String test) {
        requireNonNull(test);
        // Valid integer check
        if (!StringUtil.isNonNegativeInteger(test)) {
            return false;
        }
        // Within bounds check
        int amount = Integer.parseInt(test);
        return amount >= MIN_HOURS_WORKED && amount <= MAX_HOURS_WORKED;
    }

    /**
     * Returns an updated HoursWorked object with the specified number of hours worked added.
     *
     * @param hoursWorked The number of hours worked to be added.
     * @return An updated HoursWorked object.
     * @throws IllegalArgumentException if the amount of hours worked to be added
     * causes the total hours worked to exceed the maximum allowed hours worked .
     */
    public HoursWorked addHoursWorked(HoursWorked hoursWorked) {
        assert(hoursWorked.value > 0);
        int updatedValue = value + hoursWorked.value;
        if (updatedValue > MAX_HOURS_WORKED) {
            throw new IllegalArgumentException();
        }
        return new HoursWorked(String.valueOf(updatedValue));
    }

    /**
     * Returns an updated HoursWorked object with the specified number of hours worked removed.
     *
     * @param hoursWorked The number of hours worked to be removed.
     * @return An updated HoursWorked object.
     * @throws IllegalArgumentException if the number of hours worked to be removed
     * is greater than the current number of hours worked.
     */
    public HoursWorked removeHoursWorked(HoursWorked hoursWorked) {
        assert(hoursWorked.value > 0);
        int updatedValue = value - hoursWorked.value;
        if (updatedValue < MIN_HOURS_WORKED) {
            throw new IllegalArgumentException();
        }
        return new HoursWorked(String.valueOf(updatedValue));
    }

    /**
     * Returns an HoursWorked object that represents how many hours worked
     * can be added to this person without going over the hours worked limit.
     *
     * @return An HoursWorked object containing the remaining hours worked capacity of the Person.
     */
    public HoursWorked getRemainingHoursWorkedCapacity() {
        int hoursWorkedCapacity = MAX_HOURS_WORKED - value;
        assert(hoursWorkedCapacity >= 0);
        return new HoursWorked(String.valueOf(hoursWorkedCapacity));
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof HoursWorked // instanceof handles nulls
                && value == ((HoursWorked) other).value); // state check
    }
}
