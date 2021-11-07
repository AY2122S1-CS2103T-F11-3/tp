package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class HourlySalaryTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new HourlySalary(null));
    }

    @Test
    public void constructor_negativeSalary_throwsIllegalArgumentException() {
        String negativeSalary = "-3.12342563";
        assertThrows(IllegalArgumentException.class, () -> new HourlySalary(negativeSalary));
    }

    @Test
    public void constructor_negativeZeroSalary_throwsIllegalArgumentException() {
        String negativeZeroSalary = "-000";
        assertThrows(IllegalArgumentException.class, () -> new HourlySalary(negativeZeroSalary));
    }

    @Test
    public void constructor_alphanumericSalary_throwsIllegalArgumentException() {
        String alphanumericSalary = "3.1k";
        assertThrows(IllegalArgumentException.class, () -> new HourlySalary(alphanumericSalary));
    }

    @Test
    public void isValidHourlySalary() {
        // null hourlySalary
        assertThrows(NullPointerException.class, () -> HourlySalary.isValidHourlySalary(null));

        // invalid hourlySalary from formatting
        assertFalse(HourlySalary.isValidHourlySalary("")); // empty string
        assertFalse(HourlySalary.isValidHourlySalary(" ")); // spaces only
        assertFalse(HourlySalary.isValidHourlySalary("^")); // only non-alphanumeric characters
        assertFalse(HourlySalary.isValidHourlySalary("power-ranger")); // contains non-numeric characters only
        assertFalse(HourlySalary.isValidHourlySalary("1.123m")); // contains alphanumeric characters
        assertFalse(HourlySalary.isValidHourlySalary("-0.00")); // contains negative values
        assertFalse(HourlySalary.isValidHourlySalary("0.001")); // contains more than 2 decimal places

        // valid values in equivalence partition [0.00, 1000.00]
        assertTrue(HourlySalary.isValidHourlySalary("123")); // numeric characters as integers only
        assertTrue(HourlySalary.isValidHourlySalary("9.99")); // numeric characters as floating values only

        // boundary values for equivalence partition [0.00, 1000.00]
        assertFalse(HourlySalary.isValidHourlySalary("-0.01"));
        assertTrue(HourlySalary.isValidHourlySalary("0"));
        assertTrue(HourlySalary.isValidHourlySalary("1000"));
        assertFalse(HourlySalary.isValidHourlySalary("1000.01"));

        // invalid values in equivalence partitions [-MAX_DOUBLE, -0.01] and [1000.01, MAX_DOUBLE]
        assertFalse(HourlySalary.isValidHourlySalary("-500")); // Below lower bound
        assertFalse(HourlySalary.isValidHourlySalary("1500")); // Above upper bound
    }
}
