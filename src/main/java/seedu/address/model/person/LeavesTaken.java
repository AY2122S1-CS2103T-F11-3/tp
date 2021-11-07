package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Represents a Person's taken leaves in the employee book.
 * Guarantees: immutable;
 */
public class LeavesTaken {

    public static final String MESSAGE_CONSTRAINTS =
            "Leaves should only contain non-negative integers, and it should not be blank.";

    // Dates are sorted in ascending order
    public final PriorityQueue<LocalDate> value;


    /**
     * Constructs a {@code LeavesTaken} object.
     */
    public LeavesTaken() {
        this.value = new PriorityQueue<>(LocalDate::compareTo);
    }

    /**
     * Constructs a {@code LeavesTaken} object.
     *
     * @param dates A list representing a sequence of dates.
     */
    public LeavesTaken(List<LocalDate> dates) {
        requireNonNull(dates);
        this.value = new PriorityQueue<>(LocalDate::compareTo);
        value.addAll(dates);
    }

    /**
     * Returns an updated LeavesTaken object with the specified date added.
     *
     * @param date The date that is to be added.
     * @return An updated LeavesTaken object.
     * @throws IllegalArgumentException if the input date has already been
     *                                  assigned to this employee
     */
    public LeavesTaken addDate(LocalDate date) {
        List<LocalDate> dates = new ArrayList<>(value);
        if (dates.contains(date)) {
            throw new IllegalArgumentException();
        }
        dates.add(date);
        return new LeavesTaken(dates);
    }

    /**
     * Returns an updated LeavesTaken object with all dates before
     * the input date removed, inclusive of the date itself.
     * E.g. if 2021-10-20 is the input, all dates before 2021-10-20
     * as well as 2021-10-20 itself will be removed.
     *
     * @param beforeDate The date that is to be used.
     * @return An updated LeavesTaken object.
     */
    public LeavesTaken removeDatesBefore(LocalDate beforeDate) {
        List<LocalDate> dates = new ArrayList<>(value);
        List<LocalDate> newDates = new ArrayList<>();
        for (LocalDate date : dates) {
            // Only keep dates that are greater than the input date
            if (date.compareTo(beforeDate) > 0) {
                newDates.add(date);
            }
        }
        return new LeavesTaken(newDates);
    }

    /**
     * Checks if the input date is contained within a given LeavesTaken object.
     *
     * @param date The date that is to be checked.
     * @return True if the input date is contained, false otherwise.
     */
    public boolean containsDate(LocalDate date) {
        List<LocalDate> dates = new ArrayList<>(value);
        return dates.contains(date);
    }

    /**
     * Checks if the LeavesTaken object contains at least one date within
     * a given date range. (start and end dates inclusive)
     *
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return True if a date is contained within the input date range, false otherwise.
     */
    public boolean containsDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>(value);
        for (LocalDate date : dates) {
            // Check if the date is within the range
            if (date.compareTo(startDate) >= 0
                    && date.compareTo(endDate) <= 0) {
                return true;
            }
        }
        return false;
    }

    public List<LocalDate> toList() {
        return new ArrayList<>(value);
    }

    @Override
    public String toString() {
        List<LocalDate> dates = new ArrayList<>(value);
        StringBuilder datesString = new StringBuilder();
        for (LocalDate date : dates) {
            datesString.append(date.toString()).append(" ");
        }
        return datesString.toString().trim();
    }

    /**
     * Builds a string to list the dates of the leaves taken better formatted to display to the user.
     * Numbers the dates in order and adds newline per date.
     *
     * @return String to be displayed
     */
    public String toDisplayString() {
        List<LocalDate> dates = new ArrayList<>(value);
        if (dates.isEmpty()) {
            return "No leaves assigned yet!";
        }

        StringBuilder displayString = new StringBuilder();
        displayString.append("Applied Leave Dates:\n");
        for (int index = 1; index <= dates.size(); index++) {
            displayString.append(index).append(". ");
            displayString.append(dates.get(index - 1).toString()).append("\n");
        }
        return displayString.toString().trim();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LeavesTaken) // instanceof handles nulls
                && Arrays.equals(value.toArray(), ((LeavesTaken) other).value.toArray()); // state check
    }
}
