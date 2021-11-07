package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.OvertimePayRate;
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
 * Calculates the payroll for all employees in the address book according to the
 * how much work has been done at the current time.
 * After that, marks all employees as awaiting payment of the calculated amount
 * and resets their hours worked and overtime to zero.
 */
public class StartPayrollCommand extends Command {

    public static final String COMMAND_WORD = "startPayroll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Calculates the payroll for all employees.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_NOT_PAID = "There are employees who still have payment due: %1$s\n"
            + "Please pay all employees first before starting a new payroll.";

    public static final String MESSAGE_START_PAYROLL_SUCCESS = "Payroll done.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        // Create a deep copy of the filtered list for viewing
        List<Person> personList = new ArrayList<>(model.getFilteredPersonList());
        // Create a second list for storing changed persons
        List<Person> calculatedPersonsList = new ArrayList<>();

        // First, check if there are any unpaid employees
        for (Person personToCalculatePay: personList) {
            /*
             * An exception is thrown if the employee to be calculated for still
             * has a previous calculated pay that has not been paid yet.
             */
            if (!personToCalculatePay.isPaid()) {
                throw new CommandException(String.format(MESSAGE_NOT_PAID, personToCalculatePay.getName()));
            }
        }

        // Get the current set pay rate for overtime
        OvertimePayRate overtimePayRate = model.getOvertimePayRate();
        assert overtimePayRate != null;

        // If there are no unpaid employees, proceed with calculating payroll
        for (Person personToCalculatePay: personList) {
            HourlySalary salary = personToCalculatePay.getSalary();
            HoursWorked hoursWorked = personToCalculatePay.getHoursWorked();
            Overtime overtime = personToCalculatePay.getOvertime();
            CalculatedPay calculatedPay = calculatePay(salary, hoursWorked, overtime, overtimePayRate);

            // Set the employee to be owed the calculated pay
            Person personWithCalculatedPay = createPersonWithCalculatedPay(personToCalculatePay, calculatedPay);
            // and reset their hours worked and overtime to zero
            Person personWithPayrollDone = createPersonWithZeroHoursWorkedAndOvertime(personWithCalculatedPay);
            // Store the changed person in a deep copy list first rather than
            // immediately replacing into the model
            calculatedPersonsList.add(personWithPayrollDone);
        }

        assert personList.size() == calculatedPersonsList.size();

        // After all calculations have been successfully done,
        // replace the persons with calculated payroll into the model
        for (int i = 0; i < personList.size(); i++) {
            Person personToCalculatePay = personList.get(i);
            Person personWithPayrollDone = calculatedPersonsList.get(i);
            model.setPerson(personToCalculatePay, personWithPayrollDone);
        }

        // View the first person in the whole list of employees
        model.setViewingPerson(model.getFilteredPersonList().get(0));

        return new CommandResult(String.format(MESSAGE_START_PAYROLL_SUCCESS));
    }


    private CalculatedPay calculatePay(HourlySalary salary, HoursWorked hoursWorked, Overtime overtime,
                                       OvertimePayRate overtimePayRate) {
        assert salary != null;
        assert hoursWorked != null;
        assert overtime != null;
        assert overtimePayRate != null;

        double normalPay = salary.value * hoursWorked.value;
        double overtimePay = overtimePayRate.value * salary.value * overtime.value;

        // Check that the pay should not be negative
        assert normalPay >= 0;
        assert overtimePay >= 0;

        // Ensure that the total pay is rounded to 2 decimal places.
        String totalRoundedPay = String.format("%.2f", normalPay + overtimePay);

        return new CalculatedPay(totalRoundedPay);
    }

    private Person createPersonWithCalculatedPay(Person person, CalculatedPay newCalculatedPay) {
        assert person != null;
        assert newCalculatedPay != null;

        Name name = person.getName();
        Phone phone = person.getPhone();
        Email email = person.getEmail();
        Address address = person.getAddress();
        Role role = person.getRole();
        LeaveBalance leaves = person.getLeaveBalance();
        LeavesTaken leavesTaken = person.getLeavesTaken();
        HourlySalary hourlySalary = person.getSalary();
        HoursWorked hoursWorked = person.getHoursWorked();
        Overtime overtime = person.getOvertime();
        // New calculatedPay taken from input parameter
        Set<Tag> tags = person.getTags();

        return new Person(name, phone, email, address, role, leaves, leavesTaken, hourlySalary,
                hoursWorked, overtime, newCalculatedPay, tags);
    }

    private Person createPersonWithZeroHoursWorkedAndOvertime(Person person) {
        assert person != null;

        Name name = person.getName();
        Phone phone = person.getPhone();
        Email email = person.getEmail();
        Address address = person.getAddress();
        Role role = person.getRole();
        LeaveBalance leaves = person.getLeaveBalance();
        LeavesTaken leavesTaken = person.getLeavesTaken();
        HourlySalary hourlySalary = person.getSalary();

        // reset hours worked and overtime to zero
        HoursWorked zeroHours = new HoursWorked("0");
        Overtime zeroOvertime = new Overtime("0");

        CalculatedPay calculatedPay = person.getCalculatedPay();
        Set<Tag> tags = person.getTags();

        return new Person(name, phone, email, address, role, leaves, leavesTaken, hourlySalary,
                zeroHours, zeroOvertime, calculatedPay, tags);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartPayrollCommand); // instanceof handles nulls;
    }
}
