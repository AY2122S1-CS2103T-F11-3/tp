package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.Serializable;

import seedu.address.commons.util.StringUtil;

/**
 * Represents a pay rate for overtime used in the employee payroll calculations.
 * Guarantees: immutable; is valid as declared in {@link #isValidOvertimePayRate(String)}
 */
public class OvertimePayRate implements Serializable {

    public static final double MIN_OVERTIME_PAY_RATE = 1;
    public static final double MAX_OVERTIME_PAY_RATE = 10;
    public static final double DEFAULT_OVERTIME_PAY_RATE = 1.5;
    public static final int MAX_DECIMAL_PLACES = 5;
    public static final String MESSAGE_CONSTRAINTS =
            "Overtime pay rate should only be a number between 1 to 10, "
                    + "with 5 or less decimal places and it should not be blank.";

    public final double value;

    /**
     * Constructs a {@code OvertimePayRate} with a default overtime pay rate of 1.5.
     */
    public OvertimePayRate() {
        this.value = DEFAULT_OVERTIME_PAY_RATE;
    }

    /**
     * Constructs a {@code OvertimePayRate}.
     *
     * @param rate A valid overtime pay rate.
     */
    public OvertimePayRate(String rate) {
        requireNonNull(rate);
        checkArgument(isValidOvertimePayRate(rate), MESSAGE_CONSTRAINTS);
        this.value = Double.parseDouble(rate);
    }

    /**
     * Returns true if a given string is a valid numerical string with
     * less than 5 decimal places and is within the bounds of overtime pay rate values.
     */
    public static boolean isValidOvertimePayRate(String test) {
        requireNonNull(test);
        boolean isValidUnsignedDouble = StringUtil.isNonNegativeUnsignedDouble(test);
        boolean hasFiveOrLessDecimalPlaces = false;
        boolean isWithinBounds = false;

        if (isValidUnsignedDouble) {
            hasFiveOrLessDecimalPlaces =
                    StringUtil.isDoubleWithDpWithinLimit(test, MAX_DECIMAL_PLACES);

            double testValue = Double.parseDouble(test);
            isWithinBounds = isOvertimePayRateWithinBounds(testValue);
        }

        return isValidUnsignedDouble && hasFiveOrLessDecimalPlaces && isWithinBounds;
    }

    /**
     * Returns true if a given double is within the bounds of
     * MIN_OVERTIME_PAY_RATE and MAX_OVERTIME_PAY_RATE.
     */
    private static boolean isOvertimePayRateWithinBounds(double testValue) {
        return (testValue >= MIN_OVERTIME_PAY_RATE) && (testValue <= MAX_OVERTIME_PAY_RATE);
    }

    @Override
    public String toString() {
        return String.format("%.5f", value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OvertimePayRate // instanceof handles nulls
                && value == ((OvertimePayRate) other).value); // state check
    }

}

