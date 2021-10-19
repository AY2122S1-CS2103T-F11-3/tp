package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;



public class ImportCommandParserTest {
    private static ImportCommandParser parser = new ImportCommandParser();
    private static final String TEST_FILEPATH = "/Users/Owner/Desktop/Employees.csv";
    private static final String ANOTHER_TEST_FILEPATH = "/Users/Owner/Desktop/People.csv";

    // Displays error message if no file path is specified.
    @Test
    public void missing_file_path() {
        String input = ImportCommand.COMMAND_WORD;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE);
        assertParseFailure(parser, input, expectedMessage);
    }

    // Takes the first file path given if more than one file path is specified.
    @Test
    public void mutliple_file_path() {
        String input = ImportCommand.COMMAND_WORD + " " + TEST_FILEPATH + " " + ANOTHER_TEST_FILEPATH;
        assertParseSuccess(parser, input, new ImportCommand(TEST_FILEPATH));
    }
}
