package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

public class CalculatedPay {

    public static final int MAX_DECIMAL_PLACES = 2;
    public static final String MESSAGE_CONSTRAINTS =
            "Calculated pay should only contain non-negative numbers with two or less decimal places.";

    public final double value;

    /**
     * Constructs a {@code CalculatedPay}.
     *
     * @param amount A valid calculated pay.
     */
    public CalculatedPay(String amount) {
        requireNonNull(amount);
        checkArgument(isValidCalculatedPay(amount), MESSAGE_CONSTRAINTS);
        this.value = Double.parseDouble(amount);
    }

    /**
     * Returns true if a given numerical string is unsigned, non-negative and has
     * two or less decimal places.
     */
    public static boolean isValidCalculatedPay(String test) {
        requireNonNull(test);

        boolean isNonNegativeUnsignedDouble = StringUtil.isNonNegativeUnsignedDouble(test);
        boolean hasTwoOrLessDecimalPlaces =
                    StringUtil.isDoubleWithDpWithinLimit(test, MAX_DECIMAL_PLACES);

        return isNonNegativeUnsignedDouble && hasTwoOrLessDecimalPlaces;
    }

    @Override
    public String toString() {
        return String.format("%.2f", value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CalculatedPay // instanceof handles nulls
                && value == ((CalculatedPay) other).value); // state check
    }
}
