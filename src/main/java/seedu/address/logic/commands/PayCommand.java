package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.HoursWorked;
import seedu.address.model.person.Leaves;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.Salary;
import seedu.address.model.tag.Tag;

/**
 * Pays a person identified using it's displayed index from the address book.
 */
public class PayCommand extends Command {

    public static final String COMMAND_WORD = "pay";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": marks the employee identified by the index number used in the displayed person list as paid.\n"
            + "This also removes the calculated pay from being shown on the employee.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_PAY_PERSON_SUCCESS = "Successfully marked as paid: %1$s";

    private final Index targetIndex;

    public PayCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToPay = lastShownList.get(targetIndex.getZeroBased());
        Person paidPerson = createPaidPerson(personToPay);
        model.setPerson(personToPay, paidPerson);
        return new CommandResult(String.format(MESSAGE_PAY_PERSON_SUCCESS, personToPay));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToPay}
     * with their calculated pay being set to 0.
     * Essentially the same as paying the person.
     *
     * @param personToPay Employee to be paid
     */
    private static Person createPaidPerson(Person personToPay) {
        assert personToPay != null;

        Name name = personToPay.getName();
        Phone phone = personToPay.getPhone();
        Email email = personToPay.getEmail();
        Address address = personToPay.getAddress();
        Role role = personToPay.getRole();
        Leaves leaves = personToPay.getLeaves();

        // Create new salary with same value, with CalculatedPay initialized at 0
        int value = personToPay.getSalary().value;
        Salary paidSalary = new Salary(Integer.toString(value));

        HoursWorked hours = personToPay.getHoursWorked();
        Set<Tag> tags = personToPay.getTags();

        return new Person(name, phone, email, address, role, leaves, paidSalary, hours, tags);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PayCommand // instanceof handles nulls
                && targetIndex.equals(((PayCommand) other).targetIndex)); // state check
    }
}
