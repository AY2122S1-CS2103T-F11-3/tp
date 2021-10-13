package seedu.address.model.person;

import java.util.function.Predicate;

public class SalaryIsLessThanPredicate implements Predicate<Person> {
    private final float value;

    public SalaryIsLessThanPredicate(float value) {
        this.value = value;
    }

    /**
     * Tests if the person given has a salary strictly less than or equal the given value.
     * @param person The person whose salary is to be tested
     * @return true if the person's salary is strictly less than or equal the given value.
     */
    @Override
    public boolean test(Person person) {
        return person.getSalary().value < this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SalaryIsLessThanPredicate // instanceof handles nulls
                && value == ((SalaryIsLessThanPredicate) other).value); // state check
    }
}