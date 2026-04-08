package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERVIEWED;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.IsInterviewedPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final Logger logger = LogsCenter.getLogger(FilterCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @param args the raw argument string from user input; must not be null
     * @return a new {@code FilterCommand} using the parsed interviewed predicate
     * @throws ParseException if the {@code -interviewed} prefix is missing or its
     *     value is not a recognised boolean token
     */
    public FilterCommand parse(String args) throws ParseException {
        // Defensive: guard against null before any processing
        requireNonNull(args, "FilterCommandParser args must not be null");

        logger.fine("FilterCommandParser parsing: '" + args + "'");

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INTERVIEWED);

        // Defensive: reject unexpected text before first recognised prefix
        if (!argMultimap.getPreamble().isEmpty()) {
            logger.info("FilterCommandParser: unexpected preamble '" + argMultimap.getPreamble() + "'");
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        // Defensive: reject duplicate -interviewed prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INTERVIEWED);

        // Defensive: require the -interviewed prefix to be present
        if (!argMultimap.getValue(PREFIX_INTERVIEWED).isPresent()) {
            logger.info("FilterCommandParser: missing -interviewed prefix");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        String interviewedValue = argMultimap.getValue(PREFIX_INTERVIEWED).get().trim();

        // Defensive: reject empty value after the prefix
        if (interviewedValue.isEmpty()) {
            logger.info("FilterCommandParser: -interviewed prefix present but value is empty");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        logger.fine("FilterCommandParser: raw interviewed value = '" + interviewedValue + "'");

        boolean interviewedBoolean = ParserUtil.parseBoolean(interviewedValue);

        // Assertion: predicate is constructed with the same value we just parsed —
        // only checks pure boolean equality, no checked exception involved
        IsInterviewedPredicate predicate = new IsInterviewedPredicate(interviewedBoolean);
        assert predicate != null : "IsInterviewedPredicate must not be null";

        FilterCommand command = new FilterCommand(predicate);
        assert command != null : "FilterCommand must not be null after construction";
        assert command.getPredicate() != null : "FilterCommand predicate must not be null";

        logger.fine("FilterCommandParser: successfully created FilterCommand "
                + "with interviewed=" + interviewedBoolean);

        return command;
    }
}
