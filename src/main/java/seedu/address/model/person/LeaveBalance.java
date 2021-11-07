package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

/**
 * Represents a Person's remaining leaves in the employee book.
 * Guarantees: immutable; is valid as declared in {@link #isValidLeaveBalance(String)}
 */
public class LeaveBalance {

    public static final int MIN_LEAVES = 0;
    public static final int MAX_LEAVES = 365;
    public static final String MESSAGE_CONSTRAINTS =
            "Leave Balance should only contain an integer value between "
                    + MIN_LEAVES + " and " + MAX_LEAVES
                    + " (both inclusive), and it should not be blank.";

    public final int value;

    /**
     * Constructs a {@code LeaveBalance} object.
     *
     * @param amount A valid leave amount in days.
     */
    public LeaveBalance(String amount) {
        requireNonNull(amount);
        checkArgument(isValidLeaveBalance(amount), MESSAGE_CONSTRAINTS);
        this.value = Integer.parseInt(amount);
    }

    /**
     * Returns true if a given numerical string is non-negative
     * that is within bounds (between MIN_LEAVES and MAX_LEAVES, both inclusive).
     *
     * @param test The string input that is to be parsed into an integer.
     * @return True if the string is a non-negative integer and within bounds, false otherwise.
     */
    public static boolean isValidLeaveBalance(String test) {
        requireNonNull(test);
        // Valid integer check
        if (!StringUtil.isNonNegativeInteger(test)) {
            return false;
        }
        // Within bounds check
        int amount = Integer.parseInt(test);
        return amount >= MIN_LEAVES && amount <= MAX_LEAVES;
    }

    /**
     * Returns an updated LeaveBalance object with the specified number of leaves added.
     *
     * @param leaveBalance The amount of leaves to be added.
     * @return An updated LeaveBalance object.
     * @throws IllegalArgumentException if the amount of leaves to be added
     * causes the total leaves to exceed the maximum allowed number of leaves.
     */
    public LeaveBalance addLeaves(LeaveBalance leaveBalance) {
        assert(leaveBalance.value > 0);
        int updatedValue = value + leaveBalance.value;
        if (updatedValue > MAX_LEAVES) {
            throw new IllegalArgumentException();
        }
        return new LeaveBalance(String.valueOf(updatedValue));
    }

    /**
     * Returns an updated LeaveBalance object with the specified number of leaves removed.
     *
     * @param leaveBalance The amount of leaves to be removed.
     * @return An updated LeaveBalance object.
     * @throws IllegalArgumentException if the amount of leaves to be removed
     * is greater than the current amount of leaves.
     */
    public LeaveBalance removeLeaves(LeaveBalance leaveBalance) {
        assert(leaveBalance.value > 0);
        int updatedValue = value - leaveBalance.value;
        if (updatedValue < MIN_LEAVES) {
            throw new IllegalArgumentException();
        }
        return new LeaveBalance(String.valueOf(updatedValue));
    }

    /**
     * Returns a LeaveBalance object that represents how many leaves
     * can be added to this person without going over the leave balance limit.
     *
     * @return A LeaveBalance object containing the remaining leave capacity of the Person.
     */
    public LeaveBalance getRemainingLeaveCapacity() {
        int leaveCapacity = MAX_LEAVES - value;
        assert(leaveCapacity >= 0);
        return new LeaveBalance(String.valueOf(leaveCapacity));
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LeaveBalance // instanceof handles nulls
                && value == ((LeaveBalance) other).value); // state check
    }
}
