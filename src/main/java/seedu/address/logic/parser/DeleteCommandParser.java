package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmed = args.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Handle delete all keyword
        if (trimmed.equalsIgnoreCase(DeleteCommand.DELETE_ALL_KEYWORD)) {
            return new DeleteCommand(true);
        }

        String[] indexTokens = trimmed.split("\\s+");
        assert indexTokens.length > 0 : "Splitting a non-empty string must yield at least one token";

        List<Index> indexes = new ArrayList<>();
        for (String token : indexTokens) {
            try {
                indexes.add(ParserUtil.parseIndex(token));
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        // Reject duplicate indices
        long uniqueCount = indexes.stream().map(Index::getZeroBased).distinct().count();
        if (uniqueCount != indexes.size()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        assert !indexes.isEmpty() : "Parsed index list must not be empty after successful parsing";
        return new DeleteCommand(indexes);
    }
}
