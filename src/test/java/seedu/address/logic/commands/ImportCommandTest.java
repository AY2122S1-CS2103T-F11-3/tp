package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_FIELD_INVALID_ERROR;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_FIELD_MISSING_ERROR;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_IMPORT_FAILURE;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_IMPORT_FORMAT_ERROR;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_IMPORT_MISSING_FILE;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_IMPORT_SUCCESS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.OvertimePayRate;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.ObservablePerson;
import seedu.address.model.person.Person;

public class ImportCommandTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "CsvImportTest");
    private static final Path ALL_COLUMNS_PRESENT_FILE = TEST_DATA_FOLDER.resolve("AllColumnsPresent.csv");
    private static final Path MIXED_COLUMN_ORDERING_FILE = TEST_DATA_FOLDER.resolve("MixedColumnOrdering.csv");
    private static final Path WRONG_HEADERS_FILE = TEST_DATA_FOLDER.resolve("WrongHeaders.csv");
    private static final Path DUPLICATE_PERSONS_FILE = TEST_DATA_FOLDER.resolve("DuplicatePersons.csv");
    private static final Path DUPLICATE_EMAILS_FILE = TEST_DATA_FOLDER.resolve("DuplicateEmails.csv");
    private static final Path DUPLICATE_PHONE_NUMBERS_FILE = TEST_DATA_FOLDER.resolve("DuplicatePhoneNumbers.csv");
    private static final Path MISSING_NAME_COLUMN_FILE = TEST_DATA_FOLDER.resolve("MissingNameColumn.csv");
    private static final Path EMPTY_ENTRY_FILE = TEST_DATA_FOLDER.resolve("EmptyColumn.csv");
    private static final Path ENTRY_WITH_MISSING_NAME_FILE = TEST_DATA_FOLDER.resolve("EntryWithMissingName.csv");
    private static final Path INVALID_FIELD_PRESENT_FILE = TEST_DATA_FOLDER.resolve("InvalidDataPresent.csv");
    private static final Path MISSING_LEAVES_SALARY_HOURSWORKED_TAGS_FILE =
            TEST_DATA_FOLDER.resolve("MissingLeavesSalaryHoursWorkedTags.csv");
    private Model modelWithDefaultAddressBook;
    private Model model;

    @BeforeEach
    public void setUp() {
        modelWithDefaultAddressBook = new ModelStubWithDefaultAddressBook();
        model = new ModelStubWithDefaultAddressBook();
    }

    // Invalid file path.
    @Test
    public void import_invalidFilePath_failure() {
        assertThrows(CommandException.class, MESSAGE_IMPORT_MISSING_FILE, () ->
            new ImportCommand("notAValidPath.csv").execute(model));
    }

    // Files with all fields, following the order of declaration of fields in Person.
    @Test
    public void import_csvAllColumnsPresent_success() throws Exception {
        String filePathString = ALL_COLUMNS_PRESENT_FILE.toString();
        CommandResult result = new ImportCommand(filePathString).importData(model);

        boolean isDifferent = false;
        ObservableList<Person> original = model.getAddressBook().getPersonList();
        ObservableList<Person> imported = modelWithDefaultAddressBook.getAddressBook().getPersonList();

        for (Person person : original) {
            if (!imported.contains(person)) {
                isDifferent = true;
            }
        }
        assertEquals(MESSAGE_IMPORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(isDifferent);
    }

    // File with all fields, in mixed order.
    @Test
    public void import_csvMixedColumnOrdering_success() throws Exception {
        String filePathString = MIXED_COLUMN_ORDERING_FILE.toString();
        CommandResult result = new ImportCommand(filePathString).importData(model);

        boolean isDifferent = false;
        ObservableList<Person> original = model.getAddressBook().getPersonList();
        ObservableList<Person> imported = modelWithDefaultAddressBook.getAddressBook().getPersonList();

        for (Person person : original) {
            if (!imported.contains(person)) {
                isDifferent = true;
            }
        }

        assertEquals(MESSAGE_IMPORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(isDifferent);
    }

    // File missing non-compulsory fields.
    @Test
    public void import_csvMissingLeavesSalaryHoursWorkedTags_success() throws Exception {
        String filePathString = MISSING_LEAVES_SALARY_HOURSWORKED_TAGS_FILE.toString();
        CommandResult result = new ImportCommand(filePathString).importData(model);

        boolean isDifferent = false;
        ObservableList<Person> original = model.getAddressBook().getPersonList();
        ObservableList<Person> imported = modelWithDefaultAddressBook.getAddressBook().getPersonList();

        for (Person person : original) {
            if (!imported.contains(person)) {
                isDifferent = true;
            }
        }

        assertEquals(MESSAGE_IMPORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(isDifferent);
    }

    // File with wrong headers.
    @Test
    public void import_csvWithWrongHeaders_failure() throws Exception {
        String filePathString = WRONG_HEADERS_FILE.toString();
        String expectedMessage = MESSAGE_IMPORT_FORMAT_ERROR;
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    // File with duplicate persons.
    @Test
    public void import_csvWithDuplicatePersons_failure() throws Exception {
        String filePathString = DUPLICATE_PERSONS_FILE.toString();
        String expectedMessage = MESSAGE_IMPORT_FAILURE + "Operation would result in duplicate persons";
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    // File with duplicate emails.
    @Test
    public void import_csvWithDuplicateEmails_failure() throws Exception {
        String filePathString = DUPLICATE_EMAILS_FILE.toString();
        String expectedMessage = MESSAGE_IMPORT_FAILURE + "Duplicate emails detected in Rows 1 and 8.";
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    // File with duplicate phone numbers.
    @Test
    public void import_csvWithDuplicatePhoneNumbers_failure() throws Exception {
        String filePathString = DUPLICATE_PHONE_NUMBERS_FILE.toString();
        String expectedMessage = MESSAGE_IMPORT_FAILURE + "Duplicate phone numbers detected in Rows 1 and 8.";
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    // File with missing name column.
    @Test
    public void import_csvWithMissingNameColumn_failure() throws Exception {
        String filePathString = MISSING_NAME_COLUMN_FILE.toString();
        String expectedMessage = MESSAGE_IMPORT_FORMAT_ERROR;
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    // File with an empty entry
    @Test
    public void import_csvWithEmptyEntry_failure() throws Exception {
        String filePathString = EMPTY_ENTRY_FILE.toString();
        String expectedMessage = MESSAGE_IMPORT_FORMAT_ERROR;
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    // File with an invalid entry
    @Test
    public void import_csvWithInvalidData_failure() throws Exception {
        String filePathString = INVALID_FIELD_PRESENT_FILE.toString();
        String expectedMessage = String.format(MESSAGE_FIELD_INVALID_ERROR, 1, Name.MESSAGE_CONSTRAINTS);
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    // File with an entry missing name input.
    @Test
    public void import_csvWithEntryMissingName_failure() throws Exception {
        String filePathString = ENTRY_WITH_MISSING_NAME_FILE.toString();
        String expectedMessage = String.format(MESSAGE_FIELD_MISSING_ERROR, 1, "Name");
        Command command = new ImportCommand(filePathString);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public OvertimePayRate getOvertimePayRate() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setOvertimePayRate(OvertimePayRate overtimePayRate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasDuplicatePhone(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasDuplicateEmail(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservablePerson getViewingPerson() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setViewingPerson(Person p) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isFilteredPersonListEmpty() {
            throw new AssertionError("This method should not be called.");
        }

    }

    /**
     * A Model stub that contains the default address book.
     */
    private class ModelStubWithDefaultAddressBook extends ModelStub {
        private ReadOnlyAddressBook addressBook;

        ModelStubWithDefaultAddressBook() {
            this.addressBook = getTypicalAddressBook();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return this.addressBook;
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            this.addressBook = newData;
        }
    }


}
