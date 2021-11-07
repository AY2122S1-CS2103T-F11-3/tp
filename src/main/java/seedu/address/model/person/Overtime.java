package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

/**
 * Represents a Person's overtime in the employee book.
 * Guarantees: immutable; is valid as declared in {@link #isValidOvertime(String)}
 */
public class Overtime {

    public static final int MIN_OVERTIME = 0;
    public static final int MAX_OVERTIME = 744; // 24 hours * 31 days
    public static final String MESSAGE_CONSTRAINTS =
            "Overtime should only contain an integer value between "
                    + MIN_OVERTIME + " and " + MAX_OVERTIME
                    + " (both inclusive), and it should not be blank.";

    public final int value;

    /**
     * Constructs an {@code Overtime} object.
     *
     * @param amount A valid value in hours.
     */
    public Overtime(String amount) {
        requireNonNull(amount);
        checkArgument(isValidOvertime(amount), MESSAGE_CONSTRAINTS);
        this.value = Integer.parseInt(amount);
    }

    /**
     * Returns true if a given numerical string is non-negative
     * that is within bounds (between MIN_OVERTIME and MAX_OVERTIME, both inclusive).
     *
     * @param test The string input that is to be parsed into an integer.
     * @return True if the string is a non-negative integer and within bounds, false otherwise.
     */
    public static boolean isValidOvertime(String test) {
        requireNonNull(test);
        // Valid integer check
        if (!StringUtil.isNonNegativeInteger(test)) {
            return false;
        }
        // Within bounds check
        int amount = Integer.parseInt(test);
        return amount >= MIN_OVERTIME && amount <= MAX_OVERTIME;
    }

    /**
     * Returns an updated Overtime object with the specified number of overtime hours added.
     *
     * @param overtime The number of overtime hours to be added.
     * @return An updated Overtime object.
     * @throws IllegalArgumentException if the amount of overtime to be added
     * causes the total overtime to exceed the maximum allowed overtime.
     */
    public Overtime addOvertime(Overtime overtime) {
        assert(overtime.value > 0);
        int updatedValue = value + overtime.value;
        if (updatedValue > MAX_OVERTIME) {
            throw new IllegalArgumentException();
        }
        return new Overtime(String.valueOf(updatedValue));
    }

    /**
     * Returns an updated Overtime object with the specified number of overtime hours removed.
     *
     * @param overtime The number of overtime hours to be removed.
     * @return An updated Overtime object.
     * @throws IllegalArgumentException if the number of overtime hours to be removed
     * is greater than the current number of overtime hours.
     */
    public Overtime removeOvertime(Overtime overtime) {
        assert(overtime.value > 0);
        int updatedValue = value - overtime.value;
        if (updatedValue < MIN_OVERTIME) {
            throw new IllegalArgumentException();
        }
        return new Overtime(String.valueOf(updatedValue));
    }

    /**
     * Returns an Overtime object that represents how many overtime worked
     * can be added to this person without going over the overtime hours worked limit.
     *
     * @return An Overtime object containing the remaining overtime hours worked capacity of the Person.
     */
    public Overtime getRemainingOvertimeCapacity() {
        int overtimeWorkedCapacity = MAX_OVERTIME - value;
        assert(overtimeWorkedCapacity >= 0);
        return new Overtime(String.valueOf(overtimeWorkedCapacity));
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Overtime // instanceof handles nulls
                && value == ((Overtime) other).value); // state check
    }
}
