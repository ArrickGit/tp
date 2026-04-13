package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

/**
 * Parses input arguments and creates a new RemarkCommand object.
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    private static final Logger logger = LogsCenter.getLogger(RemarkCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns a RemarkCommand object for execution.
     *
     * @param args the raw argument string from user input; must not be null
     * @return a new {@code RemarkCommand} with the parsed index and remark
     * @throws ParseException if args is null, empty, contains an invalid index,
    *     or a remark that fails character/length validation
     */
    public RemarkCommand parse(String args) throws ParseException {
        // Defensive: guard against null before any processing
        requireNonNull(args, "RemarkCommandParser args must not be null");

        String trimmedArgs = args.trim();
        logger.fine("RemarkCommandParser parsing: '" + trimmedArgs + "'");

        // Defensive: at minimum an index must be provided
        if (trimmedArgs.isEmpty()) {
            logger.info("RemarkCommandParser: empty argument string provided");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        // Split into at most 2 parts: INDEX and optional REMARK
        String[] splitArgs = trimmedArgs.split("\\s+", 2);

        // Assertion: splitting non-empty trimmed string must yield at least one token
        assert splitArgs.length >= 1 : "Split must yield at least the index token";
        assert splitArgs.length <= 2 : "Split with limit 2 must yield at most 2 parts";

        Index index;
        try {
            index = ParserUtil.parseIndex(splitArgs[0]);
            assert index != null : "Parsed index must not be null";
            logger.fine("RemarkCommandParser: parsed index " + index.getOneBased());
        } catch (ParseException pe) {
            logger.info("RemarkCommandParser: invalid index token '"
                    + splitArgs[0] + "' — " + pe.getMessage());
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE),
                    pe);
        }

        // Remark is everything after the index, or empty string if none provided
        String remarkValue = (splitArgs.length == 2) ? splitArgs[1].trim() : "";

        assert remarkValue != null : "Remark value must not be null after extraction";
        logger.fine("RemarkCommandParser: raw remark value = '" + remarkValue + "'");

        // Defensive: validate remark content against allowed characters and length
        if (!RemarkCommand.isValidRemark(remarkValue)) {
            logger.info("RemarkCommandParser: remark failed validation — length="
                    + remarkValue.length() + ", value='" + remarkValue + "'");
            throw new ParseException(RemarkCommand.MESSAGE_CONSTRAINTS);
        }

        assert remarkValue.length() <= RemarkCommand.MAX_REMARK_LENGTH
                : "Remark length must not exceed maximum after validation";

        Remark remark = new Remark(remarkValue);
        assert remark != null : "Constructed Remark must not be null";

        RemarkCommand command = new RemarkCommand(index, remark);
        assert command != null : "Constructed RemarkCommand must not be null";

        logger.fine("RemarkCommandParser: successfully parsed remark command for index "
                + index.getOneBased() + ", remark is "
                + (remarkValue.isEmpty() ? "empty (clear)" : "'" + remarkValue + "'"));

        return command;
    }
}
