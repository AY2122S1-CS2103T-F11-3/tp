package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Salary in the employee book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSalary(String)}
 */
public class Salary {

    public static final String MESSAGE_CONSTRAINTS =
            "Salary should only contain positive integers, and it should not be blank";

    public static final String CALC_PAY_ERROR =
            "Calculated pay should only contain non-negative values!";

    public final int value;
    public final int calculatedPay;

    /**
     * Constructs a {@code Salary}.
     *
     * @param amount A valid salary amount.
     */
    public Salary(String amount) {
        requireNonNull(amount);
        checkArgument(isValidSalary(amount), MESSAGE_CONSTRAINTS);
        this.value = Integer.parseInt(amount);
        // calculatedPay initialized to 0
        this.calculatedPay = 0;
    }

    /**
     * Overloaded method to take into account calculated pay
     * @param amount A valid salary amount.
     * @param calculatedPay Calculated pay based on calculate function, 0 if paid.
     */
    public Salary(String amount, int calculatedPay) {
        requireNonNull(amount);
        checkArgument(isValidSalary(amount), MESSAGE_CONSTRAINTS);
        checkArgument(isValidCalculatedPay(calculatedPay), CALC_PAY_ERROR);
        this.value = Integer.parseInt(amount);
        this.calculatedPay = calculatedPay;
    }

    /**
     * Returns true if a given numerical string is non-negative.
     */
    public static boolean isValidSalary(String test) {
        try {
            int amount = Integer.parseInt(test);
            return amount >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if calculated pay is non-negative.
     */
    public static boolean isValidCalculatedPay(int test) {
        return test >= 0;
    }

    public int getCalculatedPay() {
        return this.calculatedPay;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Salary // instanceof handles nulls
                && value == ((Salary) other).value)
                && calculatedPay == ((Salary) other).getCalculatedPay(); // state checks
    }

}

