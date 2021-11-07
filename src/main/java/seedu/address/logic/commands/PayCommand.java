package seedu.address.logic.commands;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.CalculatedPay;
import seedu.address.model.person.Email;
import seedu.address.model.person.HourlySalary;
import seedu.address.model.person.HoursWorked;
import seedu.address.model.person.LeaveBalance;
import seedu.address.model.person.LeavesTaken;
import seedu.address.model.person.Name;
import seedu.address.model.person.Overtime;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Pays a person identified using their displayed index, or all persons in the current list from the address book.
 */
public class PayCommand extends Command {

    public static final String COMMAND_WORD = "pay";

    public static final String SPECIAL_COMMAND_PHRASE = "all";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Pays the employee identified by the index number used "
            + "OR all employees in the displayed employee list.\n"
            + "Parameters: INDEX (must be a positive integer) OR \"all\"\n"
            + "Example 1: " + COMMAND_WORD + " 1\n"
            + "Example 2: " + COMMAND_WORD + " " + SPECIAL_COMMAND_PHRASE;

    public static final String MESSAGE_PAY_PERSON_SUCCESS = "Successfully paid $%1$s to employee: %2$s";

    public static final String MESSAGE_PAY_ALL_SUCCESS = "Successfully paid all in the list.";

    public static final String MESSAGE_ALREADY_PAID = "The employee: %1$s,\n"
            + "is already paid.";

    public static final String MESSAGE_SKIP_ALREADY_PAID = "These employees have already been paid:";

    public static final String MESSAGE_NO_ONE_TO_BE_PAID = "All employees in the list have already been paid.";

    // Target index of the person to pay
    // If empty, the command pays all persons in the current list
    private final Optional<Index> targetIndex;

    /**
     * Constructs a {@code PayCommand} for paying all persons in the list.
     */
    public PayCommand() {
        this.targetIndex = Optional.empty();
    }

    /**
     * Constructs a {@code PayCommand} for paying a specific person in the list.
     * @param targetIndex The index of the person to be paid.
     */
    public PayCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = Optional.of(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        CommandResult finalResult;

        if (targetIndex.isEmpty()) {
            finalResult = executePayAll(model);
        } else {
            finalResult = executePayByIndex(model);
        }

        return finalResult;
    }

    private CommandResult executePayByIndex(Model model) throws CommandException {
        assert nonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Check if the index is valid
        if (targetIndex.get().getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToPay = lastShownList.get(targetIndex.get().getZeroBased());

        // Check if the person has already been paid
        if (personToPay.isPaid()) {
            throw new CommandException(String.format(MESSAGE_ALREADY_PAID, personToPay));
        }

        // Pay the person of the specified index
        Person paidPerson = createPaidPerson(personToPay);
        model.setPerson(personToPay, paidPerson);

        // View the person that has been paid
        model.setViewingPerson(paidPerson);

        return new CommandResult(String.format(MESSAGE_PAY_PERSON_SUCCESS,
                personToPay.getCalculatedPay().toString(),
                paidPerson.getName().toString()));
    }

    private CommandResult executePayAll(Model model) throws CommandException {
        assert nonNull(model);
        // Create a deep copy of the filtered list for viewing
        List<Person> lastShownList = new ArrayList<>(model.getFilteredPersonList());
        List<Person> personsToBePaidList = new ArrayList<>();
        List<Person> paidPersonsList = new ArrayList<>();
        List<Person> personsSkippedList = new ArrayList<>();

        // If all employees in the list are already paid, throw a CommandException
        for (Person person: lastShownList) {
            if (!person.isPaid()) {
                personsToBePaidList.add(person);
            } else {
                personsSkippedList.add(person);
            }
        }

        if (personsToBePaidList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_ONE_TO_BE_PAID);
        }

        // Pay all persons in the list only if they have not been paid
        for (Person personToPay: personsToBePaidList) {
            Person paidPerson = createPaidPerson(personToPay);
            // Add changed persons to separate list first instead of
            // immediately replacing into the model
            paidPersonsList.add(paidPerson);
        }

        assert personsToBePaidList.size() == paidPersonsList.size();

        // After all payment is done successfully, replace the
        // changed persons into the model
        for (int i = 0; i < personsToBePaidList.size(); i++) {
            Person personToPay = personsToBePaidList.get(i);
            Person paidPerson = paidPersonsList.get(i);
            model.setPerson(personToPay, paidPerson);
        }

        // View the first person in the list that has been paid
        model.setViewingPerson(paidPersonsList.get(0));

        // If there were persons who were already paid and was skipped,
        // add notifications on the persons skipped in the command result message
        String commandSuccessMessage;
        if (personsSkippedList.isEmpty()) {
            commandSuccessMessage = MESSAGE_PAY_ALL_SUCCESS;
        } else {
            StringBuilder messageBuilder = new StringBuilder(MESSAGE_PAY_ALL_SUCCESS).append("\n")
                    .append(MESSAGE_SKIP_ALREADY_PAID).append("\n");
            for (Person personSkipped: personsSkippedList) {
                messageBuilder.append(String.format("Skipped employee: %s\n", personSkipped.getName().toString()));
            }
            commandSuccessMessage = messageBuilder.toString().trim();
        }
        return new CommandResult(commandSuccessMessage);
    }

    private static Person createPaidPerson(Person personToPay) {
        assert personToPay != null;

        Name name = personToPay.getName();
        Phone phone = personToPay.getPhone();
        Email email = personToPay.getEmail();
        Address address = personToPay.getAddress();
        Role role = personToPay.getRole();
        LeaveBalance leaveBalance = personToPay.getLeaveBalance();
        LeavesTaken leavesTaken = personToPay.getLeavesTaken();
        HourlySalary hourlySalary = personToPay.getSalary();
        HoursWorked hoursWorked = personToPay.getHoursWorked();
        Overtime overtime = personToPay.getOvertime();

        // set calcPay to 0 to represent as paid
        CalculatedPay newCalcPay = new CalculatedPay("0.0");

        Set<Tag> tags = personToPay.getTags();

        return new Person(name, phone, email, address, role, leaveBalance, leavesTaken, hourlySalary,
                hoursWorked, overtime, newCalcPay, tags);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PayCommand // instanceof handles nulls
                && targetIndex.equals(((PayCommand) other).targetIndex)); // state check
    }
}
