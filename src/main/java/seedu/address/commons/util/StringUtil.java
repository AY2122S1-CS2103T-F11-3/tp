package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            // "+1" is successfully parsed by Integer#parseInt(String)
            return value > 0 && !s.startsWith("+") && !s.contains("-");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Returns true if {@code s} represents a non-negative integer
     * e.g. 0, 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-0", "-1", " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonNegativeInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            // "+1" is successfully parsed by Integer#parseInt(String)
            return value >= 0 && !s.startsWith("+") && !s.contains("-");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Returns true if {@code s} represents a non-negative, unsigned double
     * e.g. 1.00, 2, 3.0, 6.69 <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-0.00" (has negative sign), "3 0" (contains whitespace),
     * "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonNegativeUnsignedDouble(String s) {
        boolean isValidDouble;
        boolean isUnsigned;

        try {
            double value = Double.parseDouble(s);
            isValidDouble = true;
        } catch (NumberFormatException nfe) {
            isValidDouble = false;
        }
        isUnsigned = !s.startsWith("+") && !s.startsWith("-");

        return isValidDouble && isUnsigned;
    }

    /**
     * Returns true if {@code s} represents a double with less than or
     * equals to {@code maxDecimalPlaces} decimal places (dp)
     * e.g. -1.00, 2, 3.0, 6.69 for {@code maxDecimalPlaces} = 2 <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "3 0" (contains whitespace), "1 a" (contains letters),
     * "6.699" for {@code maxDecimalPlaces} = 3
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isDoubleWithDpWithinLimit(String s, int maxDecimalPlaces) {
        requireNonNull(s);
        assert maxDecimalPlaces >= 0;

        boolean isValidDouble;
        boolean hasDpWithinLimit;

        // Check if s is a valid double
        try {
            double value = Double.parseDouble(s);
            isValidDouble = true;
        } catch (NumberFormatException nfe) {
            isValidDouble = false;
        }

        // Check if decimal places are within specified limit
        int indexOfLastDigit = s.length() - 1;
        int indexOfDecimalPoint = s.indexOf('.');
        int numberOfDecimalPlaces;

        // Check if there is no '.' in the number at all
        if (indexOfDecimalPoint == -1) {
            numberOfDecimalPlaces = 0;
        } else {
            numberOfDecimalPlaces = indexOfLastDigit - indexOfDecimalPoint;
        }
        hasDpWithinLimit = numberOfDecimalPlaces <= maxDecimalPlaces;

        return isValidDouble && hasDpWithinLimit;
    }
}
