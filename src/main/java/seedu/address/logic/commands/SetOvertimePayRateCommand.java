package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.OvertimePayRate;

/**
 * Sets the pay rate for overtime in future calculations for payroll.
 */
public class SetOvertimePayRateCommand extends Command {

    public static final String COMMAND_WORD = "setOvertimePayRate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the pay rate for overtime in future calculations for payroll.\n"
            + "Parameters: NEW_OVERTIME_PAY_RATE (must be a number from 1 to 10,"
            + " with at most 5 decimal places)\n"
            + "Example: " + COMMAND_WORD + " 1.75";

    public static final String MESSAGE_SET_RATE_SUCCESS = "New overtime pay rate: %1$s";

    private final OvertimePayRate newOvertimePayRate;

    public SetOvertimePayRateCommand(OvertimePayRate newOvertimePayRate) {
        this.newOvertimePayRate = newOvertimePayRate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        model.setOvertimePayRate(newOvertimePayRate);

        return new CommandResult(String.format(MESSAGE_SET_RATE_SUCCESS, newOvertimePayRate));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetOvertimePayRateCommand // instanceof handles nulls
                && newOvertimePayRate.equals(((SetOvertimePayRateCommand) other).newOvertimePayRate)); // state check
    }
}
