package seedu.address.storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
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
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String role;
    private final String leaves;
    private final List<LocalDate> leavesTaken;
    private final String salary;
    private final String hoursWorked;
    private final String overtime;
    private final String calculatedPay;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("role") String role, @JsonProperty("leaves") String leaves,
            @JsonProperty("leavesTaken") List<LocalDate> leavesTaken, @JsonProperty("salary") String salary,
            @JsonProperty("hoursWorked") String hoursWorked, @JsonProperty("overtime") String overtime,
            @JsonProperty("calculatedPay") String calculatedPay,
            @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.leaves = leaves;
        this.leavesTaken = leavesTaken;
        this.salary = salary;
        this.hoursWorked = hoursWorked;
        this.overtime = overtime;
        this.calculatedPay = calculatedPay;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        role = source.getRole().value;
        leaves = source.getLeaveBalance().toString();
        leavesTaken = source.getLeavesTaken().toList();
        salary = source.getSalary().toString();
        hoursWorked = source.getHoursWorked().toString();
        overtime = source.getOvertime().toString();
        calculatedPay = source.getCalculatedPay().toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (role == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Role.class.getSimpleName()));
        }
        if (!Role.isValidRole(role)) {
            throw new IllegalValueException(Role.MESSAGE_CONSTRAINTS);
        }
        final Role modelRole = new Role(role);

        if (leaves == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LeaveBalance.class.getSimpleName()));
        }
        if (!LeaveBalance.isValidLeaveBalance(leaves)) {
            throw new IllegalValueException(LeaveBalance.MESSAGE_CONSTRAINTS);
        }
        final LeaveBalance modelLeaveBalance = new LeaveBalance(leaves);

        if (leavesTaken == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LeavesTaken.class.getSimpleName()));
        }
        final LeavesTaken modelLeavesTaken = new LeavesTaken(leavesTaken);

        if (salary == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    HourlySalary.class.getSimpleName()));
        }
        if (!HourlySalary.isValidHourlySalary(salary)) {
            throw new IllegalValueException(HourlySalary.MESSAGE_CONSTRAINTS);
        }
        final HourlySalary modelHourlySalary = new HourlySalary(salary);

        if (hoursWorked == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    HoursWorked.class.getSimpleName()));
        }
        if (!HoursWorked.isValidHoursWorked(hoursWorked)) {
            throw new IllegalValueException(HoursWorked.MESSAGE_CONSTRAINTS);
        }
        final HoursWorked modelHoursWorked = new HoursWorked(hoursWorked);

        if (overtime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Overtime.class.getSimpleName()));
        }
        if (!Overtime.isValidOvertime(overtime)) {
            throw new IllegalValueException(Overtime.MESSAGE_CONSTRAINTS);
        }
        final Overtime modelOvertime = new Overtime(overtime);

        if (calculatedPay == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CalculatedPay.class.getSimpleName()));
        }
        if (!CalculatedPay.isValidCalculatedPay(calculatedPay)) {
            throw new IllegalValueException(CalculatedPay.MESSAGE_CONSTRAINTS);
        }
        final CalculatedPay modelCalculatedPay = new CalculatedPay(calculatedPay);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelRole, modelLeaveBalance,
                modelLeavesTaken, modelHourlySalary, modelHoursWorked, modelOvertime, modelCalculatedPay, modelTags);
    }

}
