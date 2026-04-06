package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private static final Logger logger = LogsCenter.getLogger(DeleteCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @param args the raw argument string from user input; must not be null
     * @return a new {@code DeleteCommand} with the parsed index list, or in delete-all mode
     * @throws ParseException if args is empty, contains invalid indexes, or contains duplicates
     */
    public DeleteCommand parse(String args) throws ParseException {
        // Defensive: guard against null input
        if (args == null) {
            logger.warning("DeleteCommandParser received null args — rejecting");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String trimmed = args.trim();
        logger.fine("DeleteCommandParser parsing: '" + trimmed + "'");

        // Defensive: reject empty input
        if (trimmed.isEmpty()) {
            logger.info("DeleteCommandParser: empty argument string provided");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Route to delete-all mode
        if (trimmed.equalsIgnoreCase(DeleteCommand.DELETE_ALL_KEYWORD)) {
            logger.info("DeleteCommandParser: delete-all mode selected");
            return new DeleteCommand(true);
        }

        String[] indexTokens = trimmed.split("\\s+");

        // Assertion: splitting a non-empty, non-blank string must yield tokens
        assert indexTokens.length > 0
                : "Splitting non-empty trimmed string must yield at least one token";

        logger.fine("DeleteCommandParser: attempting to parse "
                + indexTokens.length + " index token(s)");

        List<Index> indexes = new ArrayList<>();
        for (String token : indexTokens) {
            // Defensive: each token must be non-empty after split
            if (token.isEmpty()) {
                logger.warning("DeleteCommandParser: encountered unexpected empty token");
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            try {
                Index parsed = ParserUtil.parseIndex(token);
                assert parsed != null : "ParserUtil.parseIndex must not return null";
                indexes.add(parsed);
                logger.finest("DeleteCommandParser: parsed index " + parsed.getOneBased());
            } catch (ParseException pe) {
                logger.info("DeleteCommandParser: invalid index token '" + token
                        + "' — " + pe.getMessage());
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE),
                        pe);
            }
        }

        // Assertion: list must be non-empty after parsing all tokens
        assert !indexes.isEmpty()
                : "Index list must be non-empty after successfully parsing tokens";

        // Defensive: reject duplicate indices to prevent wrong-person deletion
        long uniqueCount = indexes.stream()
                .map(Index::getZeroBased)
                .distinct()
                .count();
        if (uniqueCount != indexes.size()) {
            logger.info("DeleteCommandParser: duplicate index detected among inputs");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Assertion: unique count cannot exceed total count
        assert uniqueCount <= indexes.size()
                : "Unique index count must not exceed total count";

        logger.fine("DeleteCommandParser: successfully parsed "
                + indexes.size() + " unique index(es)");

        return new DeleteCommand(indexes);
    }
}
