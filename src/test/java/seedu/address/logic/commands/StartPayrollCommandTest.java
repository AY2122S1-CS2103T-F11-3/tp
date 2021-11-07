package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.OvertimePayRate;
import seedu.address.model.UserPrefs;
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
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code StartPayrollCommand}.
 */
public class StartPayrollCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private CalculatedPay calculatePay(HourlySalary salary, HoursWorked hoursWorked, Overtime overtime,
                                       OvertimePayRate overtimePayRate) {
        double normalPay = salary.value * hoursWorked.value;
        double overtimePay = overtimePayRate.value * salary.value * overtime.value;
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
        // new calculatedPay taken from input parameter
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

    @Test
    public void execute_unfilteredList_success() {
        StartPayrollCommand startPayrollCommand = new StartPayrollCommand();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        List<Person> personList = model.getFilteredPersonList();
        OvertimePayRate overtimePayRate = model.getOvertimePayRate();
        for (Person personToCalculatePay: personList) {
            HourlySalary salary = personToCalculatePay.getSalary();
            HoursWorked hoursWorked = personToCalculatePay.getHoursWorked();
            Overtime overtime = personToCalculatePay.getOvertime();
            CalculatedPay calculatedPay = calculatePay(salary, hoursWorked, overtime, overtimePayRate);

            Person personWithCalculatedPay = createPersonWithCalculatedPay(personToCalculatePay, calculatedPay);
            Person personWithPayrollDone = createPersonWithZeroHoursWorkedAndOvertime(personWithCalculatedPay);
            expectedModel.setPerson(personToCalculatePay, personWithPayrollDone);
        }

        String expectedMessage =
                String.format(StartPayrollCommand.MESSAGE_START_PAYROLL_SUCCESS);

        assertCommandSuccess(startPayrollCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        StartPayrollCommand startPayrollCommand = new StartPayrollCommand();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        List<Person> personList = expectedModel.getFilteredPersonList();
        OvertimePayRate overtimePayRate = model.getOvertimePayRate();
        for (Person personToCalculatePay: personList) {
            HourlySalary salary = personToCalculatePay.getSalary();
            HoursWorked hoursWorked = personToCalculatePay.getHoursWorked();
            Overtime overtime = personToCalculatePay.getOvertime();
            CalculatedPay calculatedPay = calculatePay(salary, hoursWorked, overtime, overtimePayRate);

            Person personWithCalculatedPay = createPersonWithCalculatedPay(personToCalculatePay, calculatedPay);
            Person personWithPayrollDone = createPersonWithZeroHoursWorkedAndOvertime(personWithCalculatedPay);
            expectedModel.setPerson(personToCalculatePay, personWithPayrollDone);
        }

        String expectedMessage =
                String.format(StartPayrollCommand.MESSAGE_START_PAYROLL_SUCCESS);

        assertCommandSuccess(startPayrollCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personIsNotPaid_throwsCommandException() {
        Person personToCalculatePay = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        StartPayrollCommand startPayrollCommand = new StartPayrollCommand();

        CalculatedPay calculatedPay = new CalculatedPay("500.00");
        Person personWithCalculatedPay = createPersonWithCalculatedPay(personToCalculatePay, calculatedPay);

        model.setPerson(personToCalculatePay, personWithCalculatedPay);

        String expectedMessage =
                String.format(StartPayrollCommand.MESSAGE_NOT_PAID, personToCalculatePay.getName());

        assertCommandFailure(startPayrollCommand, model, expectedMessage);
    }
}
