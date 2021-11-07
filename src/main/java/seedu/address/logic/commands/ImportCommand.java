package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.opencsv.bean.CsvToBeanBuilder;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
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
import seedu.address.model.person.PersonInput;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.exceptions.DuplicateEmailException;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.DuplicatePhoneException;
import seedu.address.model.tag.Tag;

public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": imports a current existing CSV file into HeRon.\n"
            + "Parameters: Absolute/Relative Path leading to desired CSV file.\n"
            + "Example: " + COMMAND_WORD + " /Users/Owner/Desktop/toBeImported.csv";

    public static final String MESSAGE_IMPORT_SUCCESS = "File was successfully imported.";
    public static final String MESSAGE_IMPORT_FAILURE = "Error occurred while importing the file.\n";
    public static final String MESSAGE_IMPORT_MISSING_FILE = MESSAGE_IMPORT_FAILURE
            + "Please check the filepath and try again.";
    public static final String MESSAGE_IMPORT_FORMAT_ERROR = MESSAGE_IMPORT_FAILURE
            + "Please check the formatting of the data.\n"
            + "Check that all required columns are present, and there is at least one entry.\n"
            + "Ensure that there are no entries with all empty fields in the CSV file "
            + "and the number of header columns match the number of columns in all employee entries.";
    public static final String MESSAGE_FIELD_MISSING_ERROR = MESSAGE_IMPORT_FAILURE
            + "Row %1$d: Missing '%2$s' field.";
    public static final String MESSAGE_FIELD_INVALID_ERROR = MESSAGE_IMPORT_FAILURE
            + "Invalid Input in Row %1$d: %2$s";

    public static final String INPUT_ANNOTATION_NAME_FIELD = "Name";
    public static final String INPUT_ANNOTATION_PHONE_FIELD = "Contact Number";
    public static final String INPUT_ANNOTATION_ADDRESS_FIELD = "Residential Address";
    public static final String INPUT_ANNOTATION_EMAIL_FIELD = "Email";
    public static final String INPUT_ANNOTATION_ROLE_FIELD = "Role";

    private final String filepathString;

    /**
     * Constructor for the ImportCommand
     * @param filepath The string representation of the filepath of the desired file.
     */
    public ImportCommand(String filepath) {
        requireNonNull(filepath);
        this.filepathString = filepath;
    }

    /**
     * Executes the specific command.
     * @param model {@code Model} which the command should operate on.
     * @return The feedback of the command to be returned to the user
     * @throws CommandException If an error occurs during the import.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        CommandResult result = importData(model);
        try {
            Index firstEntryIndex = ParserUtil.parseIndex("1");
            new ViewCommand(firstEntryIndex).execute(model);
        } catch (ParseException e) {
            // Should not happen since "1" is a valid index, and imported file must have at least 1 entry.
            assert(false);
            throw new CommandException(e.getMessage());
        }

        return result;
    }

    /**
     * Imports the data from the csv into the existing model. Abstracted out from #execute for testing purposes.
     * @param model {@code Model} which the command should operate on.
     * @return The result of the import command
     * @throws CommandException If an error occurs while importing the data
     */
    public CommandResult importData(Model model) throws CommandException {
        try {
            List<Person> newPersonList = processCsv(this.filepathString);
            AddressBook newAddressBook = new AddressBook();
            newAddressBook.setPersons(newPersonList);
            model.setAddressBook(newAddressBook);
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        } catch (DuplicatePersonException | DuplicateEmailException | DuplicatePhoneException e) {
            throw new CommandException(MESSAGE_IMPORT_FAILURE + e.getMessage());
        }
        return new CommandResult(MESSAGE_IMPORT_SUCCESS);
    }

    /**
     * Parses and creates the new address book from the data in the CSV file.
     * @param filepath The string representation of the filepath to the desired csv file.
     * @return A list of Person objects to be replaced into the address book.
     * @throws CommandException If an error occurs during the processing of the CSV file into Person objects.
     */
    public List<Person> processCsv(String filepath) throws CommandException {
        try {
            FileReader fileReader = getFileReader(filepath);
            List<PersonInput> inputDataList = parseCsv(fileReader);
            List<Person> personList = createPersonsFromInput(inputDataList);
            return personList;
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Parses the given csv file and creates a list of PersonInput objects.
     * @param fileReader The fileReader object of the csv file.
     * @return A list of PersonInput objects used to create the Person objects.
     * @throws CommandException If there are missing fields or formatting errors in the CSV file.
     */
    @SuppressWarnings("unchecked")
    // List<PersonInput> is the only return type of the method, or else an exception would be thrown.
    public List<PersonInput> parseCsv(FileReader fileReader) throws CommandException {
        int rowNumber = 1; // The row number of the data entry as shown in Excel.
        List<PersonInput> newPersonInputList = new ArrayList<>();

        try {
            Iterator<PersonInput> inputIterator = new CsvToBeanBuilder(fileReader).withType(PersonInput.class)
                    .build().iterator();
            while (inputIterator.hasNext()) {
                rowNumber++;
                PersonInput input = inputIterator.next();
                newPersonInputList.add(input);
            }
            return newPersonInputList;
        } catch (RuntimeException e) {
            String[] errorDescriptorArr = e.getMessage().split(":");
            String errorMessage;
            if (errorDescriptorArr.length == 1) {
                errorMessage = MESSAGE_IMPORT_FORMAT_ERROR;
                throw new CommandException(errorMessage);
            }

            String[] descriptorArr = errorDescriptorArr[1].split("\\'");
            if (descriptorArr.length == 1) {
                errorMessage = MESSAGE_IMPORT_FORMAT_ERROR;
            } else {
                String annotationField = descriptorArr[1].trim();
                String field = getColumnTitle(annotationField); // Field naming as per header naming convention.
                errorMessage = String.format(MESSAGE_FIELD_MISSING_ERROR, rowNumber, field);
            }
            throw new CommandException(errorMessage);
        }
    }

    /**
     * Creates a FileReader object for a specified csv file.
     * @param filepath The string representation of the filepath to the desired csv file.
     * @return The FileReader object for the file in the specified filepath.
     * @throws CommandException If the specified file with the filepath does not exist.
     */
    public FileReader getFileReader(String filepath) throws CommandException {
        try {
            FileReader fileReader = new FileReader(filepath);
            return fileReader;
        } catch (FileNotFoundException e) {
            throw new CommandException(MESSAGE_IMPORT_MISSING_FILE);
        }
    }

    /**
     * Creates a List of Person objects to replace the existing employee list.
     * @param newPersonInputList The list of PersonInput objects to create the corresponding Persons.
     * @return A list of person objects.
     * @throws CommandException If the given inputs are invalid, and a Person object cannot be created.
     */
    public List<Person> createPersonsFromInput(List<PersonInput> newPersonInputList) throws CommandException {
        List<Person> newPersonList = new ArrayList<>();
        int rowNumber = 1;
        for (PersonInput input : newPersonInputList) {
            try {
                // Required Fields
                Name name = ParserUtil.parseName(input.getName());
                Phone phone = ParserUtil.parsePhone(input.getPhone());
                Email email = ParserUtil.parseEmail(input.getEmail());
                Address address = ParserUtil.parseAddress(input.getAddress());
                Role role = ParserUtil.parseRole(input.getRole());

                // Optional Fields, default value of 0/empty set is used if input is not provided.
                LeaveBalance leaves = buildLeave(input);
                HourlySalary hourlySalary = buildSalary(input);
                HoursWorked hoursWorked = buildHoursWorked(input);
                Overtime overtime = buildOvertime(input);
                Set<Tag> tagList = buildTags(input);

                newPersonList.add(new Person(name, phone, email, address, role, leaves, new LeavesTaken(), hourlySalary,
                        hoursWorked, overtime, new CalculatedPay("0"), tagList));
                rowNumber++;
            } catch (ParseException e) {
                throw new CommandException(String.format(MESSAGE_FIELD_INVALID_ERROR, rowNumber, e.getMessage()));
            }
        }
        return newPersonList;
    }

    /**
     * Creates a LeaveBalance object with the corresponding input, or a default value of 0.
     * @param input PersonInput object created by the bean.
     * @return A LeaveBalance object for the Person constructor.
     * @throws ParseException If an error occurs while parsing the String input.
     */
    private LeaveBalance buildLeave(PersonInput input) throws ParseException {
        return input.getLeaves() == null
                ? new LeaveBalance("0")
                : ParserUtil.parseLeaveBalance(input.getLeaves());
    }

    /**
     * Creates a HourlySalary object with the corresponding input, or a default value of 0.
     * @param input PersonInput object created by the bean.
     * @return An HourlySalary object for the Person constructor.
     * @throws ParseException If an error occurs while parsing the String input.
     */
    private HourlySalary buildSalary(PersonInput input) throws ParseException {
        return input.getSalary() == null
                ? new HourlySalary("0")
                : ParserUtil.parseSalary(input.getSalary());
    }

    /**
     * Creates a HoursWorked object with the corresponding input, or a default value of 0.
     * @param input PersonInput object created by the bean.
     * @return An HoursWorked object for the Person constructor
     * @throws ParseException If an error occurs while parsing the String input.
     */
    private HoursWorked buildHoursWorked(PersonInput input) throws ParseException {
        return input.getHoursWorked() == null
                ? new HoursWorked("0")
                : ParserUtil.parseHoursWorked(input.getHoursWorked());
    }

    /**
     * Creates an Overtime object with the corresponding input, or a default value of 0.
     * @param input PersonInput object created by the bean.
     * @return An Overtime object for the Person constructor
     * @throws ParseException If an error occurs while parsing the String input.
     */
    private Overtime buildOvertime(PersonInput input) throws ParseException {
        return input.getOvertime() == null
                ? new Overtime("0")
                : ParserUtil.parseOvertime(input.getOvertime());
    }

    /**
     * Creates a Set object containing all the corresponding tags for the entry, or a default value of an empty set.
     * @param input PersonInput object created by the bean.
     * @return A Set object for the Person constructor.
     * @throws ParseException If an error occurs while parsing the String input.
     */
    private Set<Tag> buildTags(PersonInput input) throws ParseException {
        return (input.getTags() == null || input.getTags().equals(""))
                ? new HashSet<>()
                : ParserUtil.parseTags(Arrays.asList(input.getTags().split("/")));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && filepathString.equals(((ImportCommand) other).filepathString));
    }

    public String getFilepathString() {
        return this.filepathString;
    }

    /**
     * Gets the header naming convention of the specified field.
     * @param field The field of PersonInput object.
     * @return The naming convention being used in the imported csv file.
     */
    public String getColumnTitle(String field) {
        switch (field) {
        case "name":
            return INPUT_ANNOTATION_NAME_FIELD;
        case "phone":
            return INPUT_ANNOTATION_PHONE_FIELD;
        case "address":
            return INPUT_ANNOTATION_ADDRESS_FIELD;
        case "email":
            return INPUT_ANNOTATION_EMAIL_FIELD;
        case "role":
            return INPUT_ANNOTATION_ROLE_FIELD;
        default:
            assert(false);
            return null; // Should not reach here since there are only 5 compulsory fields for import.
        }
    }



}
