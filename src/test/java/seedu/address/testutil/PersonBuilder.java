package seedu.address.testutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_ROLE = "HeRon Developer";
    public static final String DEFAULT_LEAVES = "0";
    public static final List<LocalDate> DEFAULT_LEAVES_TAKEN = new ArrayList<>();
    public static final String DEFAULT_SALARY = "15";
    public static final String DEFAULT_HOURSWORKED = "70";
    public static final String DEFAULT_OVERTIME = "5";


    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Role role;
    private LeaveBalance leaveBalance;
    private LeavesTaken leavesTaken;
    private HourlySalary hourlySalary;
    private HoursWorked hoursWorked;
    private Overtime overtime;

    // When person initialized, always set pay to 0.
    private CalculatedPay calculatedPay = new CalculatedPay("0.00");

    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        role = new Role(DEFAULT_ROLE);
        leaveBalance = new LeaveBalance(DEFAULT_LEAVES);
        leavesTaken = new LeavesTaken(DEFAULT_LEAVES_TAKEN);
        hourlySalary = new HourlySalary(DEFAULT_SALARY);
        hoursWorked = new HoursWorked(DEFAULT_HOURSWORKED);
        overtime = new Overtime(DEFAULT_OVERTIME);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        role = personToCopy.getRole();
        leaveBalance = personToCopy.getLeaveBalance();
        leavesTaken = personToCopy.getLeavesTaken();
        hourlySalary = personToCopy.getSalary();
        hoursWorked = personToCopy.getHoursWorked();
        overtime = personToCopy.getOvertime();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Role} of the {@code Person} that we are building.
     */
    public PersonBuilder withRole(String role) {
        this.role = new Role(role);
        return this;
    }

    /**
     * Sets the {@code Leaves} of the {@code Person} that we are building.
     */
    public PersonBuilder withLeaves(String leaves) {
        this.leaveBalance = new LeaveBalance(leaves);
        return this;
    }

    /**
     * Sets the {@code LeavesTaken} of the {@code Person} that we are building.
     */
    public PersonBuilder withLeavesTaken(List<LocalDate> dates) {
        this.leavesTaken = new LeavesTaken(dates);
        return this;
    }

    /**
     * Sets the {@code CalculatedPay} of the {@code Person} that we are building.
     */
    public PersonBuilder withCalculatedPay(String pay) {
        this.calculatedPay = new CalculatedPay(pay);
        return this;
    }

    /**
     * Sets the {@code Salary} of the {@code Person} that we are building.
     */
    public PersonBuilder withHourlySalary(String salary) {
        this.hourlySalary = new HourlySalary(salary);
        return this;
    }

    /**
     * Sets the {@code HoursWorked} of the {@code Person} that we are building.
     */
    public PersonBuilder withHoursWorked(String hoursWorked) {
        this.hoursWorked = new HoursWorked(hoursWorked);
        return this;
    }

    /**
     * Sets the {@code Overtime} of the {@code Person} that we are building.
     */
    public PersonBuilder withOvertime(String overtime) {
        this.overtime = new Overtime(overtime);
        return this;
    }

    /**
     * Builds a {@code Person}.
     */
    public Person build() {
        return new Person(name, phone, email, address, role, leaveBalance, leavesTaken,
                hourlySalary, hoursWorked, overtime, calculatedPay, tags);
    }
}
