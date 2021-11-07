package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class OvertimePayRateTest {
    // Reused test cases from HourlySalaryTest by @boonhai
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OvertimePayRate(null));
    }

    @Test
    public void constructor_negativeOvertimePayRate_throwsIllegalArgumentException() {
        String negativeOvertimePayRate = "-3.123";
        assertThrows(IllegalArgumentException.class, () -> new OvertimePayRate(negativeOvertimePayRate));
    }

    @Test
    public void constructor_alphanumericOvertimePayRate_throwsIllegalArgumentException() {
        String alphanumericOvertimePayRate = "3.1k";
        assertThrows(IllegalArgumentException.class, () -> new OvertimePayRate(alphanumericOvertimePayRate));
    }

    // Over 5 decimal places (dp)
    @Test
    public void constructor_overFiveDpOvertimePayRate_throwsIllegalArgumentException() {
        String overFiveDpOvertimePayRate = "-3.1232314";
        assertThrows(IllegalArgumentException.class, () -> new OvertimePayRate(overFiveDpOvertimePayRate));
    }

    @Test
    public void isValidOvertimePayRate() {
        // null overtime pay rate
        assertThrows(NullPointerException.class, () -> OvertimePayRate.isValidOvertimePayRate(null));

        // invalid overtime pay rate from formatting
        assertFalse(OvertimePayRate.isValidOvertimePayRate("")); // empty string
        assertFalse(OvertimePayRate.isValidOvertimePayRate(" ")); // spaces only
        assertFalse(OvertimePayRate.isValidOvertimePayRate("^")); // only non-alphanumeric characters
        assertFalse(OvertimePayRate.isValidOvertimePayRate("power-ranger")); // contains non-numeric characters only
        assertFalse(OvertimePayRate.isValidOvertimePayRate("1.123m")); // contains alphanumeric characters
        assertFalse(OvertimePayRate.isValidOvertimePayRate("-3.00")); // contains negative values
        assertFalse(OvertimePayRate.isValidOvertimePayRate("0.000001")); // contains more than 5 decimal places

        // valid values in equivalence partition [1.00000, 10.00000]
        assertTrue(OvertimePayRate.isValidOvertimePayRate("5")); // numeric characters as integers only
        assertTrue(OvertimePayRate.isValidOvertimePayRate("5.99")); // numeric characters as floating values only

        // boundary values for equivalence partition [1.00000, 10.00000]
        assertFalse(OvertimePayRate.isValidOvertimePayRate("0.99999"));
        assertTrue(OvertimePayRate.isValidOvertimePayRate("1"));
        assertTrue(OvertimePayRate.isValidOvertimePayRate("10"));
        assertFalse(OvertimePayRate.isValidOvertimePayRate("10.00001"));

        // invalid values in equivalence partitions [-MAX_DOUBLE, 0.99999] and [10.00001, MAX_DOUBLE]
        assertFalse(OvertimePayRate.isValidOvertimePayRate("-50")); // Below lower bound
        assertFalse(OvertimePayRate.isValidOvertimePayRate("50")); // Above upper bound
    }
}
