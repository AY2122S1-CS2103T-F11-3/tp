package seedu.address.model.person.predicates;

import java.util.function.Predicate;

import seedu.address.model.person.Person;

public class SalaryIsMoreThanEqualPredicate implements Predicate<Person> {
    private final float value;

    public SalaryIsMoreThanEqualPredicate(float value) {
        this.value = value;
    }

    /**
     * Tests if the person given has a salary more than or equal the given value.
     * @param person The person whose salary is to be tested
     * @return true if the person's salary is greater than or equal the given value.
     */
    @Override
    public boolean test(Person person) {
        return person.getSalary().value >= this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SalaryIsMoreThanEqualPredicate // instanceof handles nulls
                && value == ((SalaryIsMoreThanEqualPredicate) other).value); // state check
    }
}