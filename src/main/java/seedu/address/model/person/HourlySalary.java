package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

/**
 * Represents a Person's Hourly Salary in HeRon.
 * Guarantees: immutable; is valid as declared in {@link #isValidHourlySalary(String)}
 */
public class HourlySalary {

    public static final double MIN_HOURLY_SALARY = 0;
    public static final double MAX_HOURLY_SALARY = 1000;
    public static final String MESSAGE_CONSTRAINTS =
            "Hourly Salary should only contain non-negative numbers with two or less decimal places "
                    + "with a value between 0 to 1000, and it should not be blank.";

    public final double value;

    /**
     * Constructs a {@code HourlySalary}.
     *
     * @param amount A valid hourly salary amount.
     */
    public HourlySalary(String amount) {
        requireNonNull(amount);
        checkArgument(isValidHourlySalary(amount), MESSAGE_CONSTRAINTS);
        this.value = Double.parseDouble(amount);
    }

    /**
     * Returns true if a given numerical string is non-negative, has
     * two or less decimal places and has a value within the bounds for hourly salary values.
     */
    public static boolean isValidHourlySalary(String test) {
        requireNonNull(test);
        boolean isValidDouble = StringUtil.isNonNegativeDoubleWithTwoOrLessDecimalPlaces(test);
        boolean isWithinBounds = false;
        if (isValidDouble) {
            double testValue = Double.parseDouble(test);
            isWithinBounds = isHourlySalaryWithinBounds(testValue);
        }
        return isValidDouble && isWithinBounds;
    }

    /**
     * Returns true if a given double is within the bounds of
     * MIN_HOURLY_SALARY and MAX_HOURLY_SALARY.
     */
    private static boolean isHourlySalaryWithinBounds(double testValue) {
        return (testValue >= MIN_HOURLY_SALARY) && (testValue <= MAX_HOURLY_SALARY);
    }

    @Override
    public String toString() {
        return String.format("%.2f", value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof HourlySalary // instanceof handles nulls
                && value == ((HourlySalary) other).value); // state check
    }

}

