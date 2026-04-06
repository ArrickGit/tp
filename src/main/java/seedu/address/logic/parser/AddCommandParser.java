package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object.
 */
public class AddCommandParser implements Parser<AddCommand> {

    private static final Logger logger = LogsCenter.getLogger(AddCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @param args the raw argument string from user input; must not be null
     * @return a new {@code AddCommand} constructed from the parsed arguments
     * @throws ParseException if the user input does not conform to the expected format,
     *     or if any individual field fails validation
     */
    public AddCommand parse(String args) throws ParseException {
        // Defensive: reject null input before any processing
        if (args == null) {
            logger.warning("AddCommandParser received null args — rejecting");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        logger.fine("AddCommandParser parsing args: " + args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        // Defensive: check all required prefixes are present and no unexpected preamble
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            logger.info("AddCommandParser: missing required prefix(es) or unexpected preamble — "
                    + "preamble was: '" + argMultimap.getPreamble() + "'");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        // Defensive: reject duplicate prefixes for non-repeatable fields
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        // Assertions: internal invariants after parsing
        assert name != null : "Parsed name must not be null";
        assert phone != null : "Parsed phone must not be null";
        assert email != null : "Parsed email must not be null";
        assert address != null : "Parsed address must not be null";
        assert tagList != null : "Parsed tag list must not be null";

        // New person always starts with no remark and not interviewed
        Person person = new Person(name, phone, email, address, tagList, false, new Remark(""));

        assert person != null : "Constructed person must not be null";
        logger.fine("AddCommandParser successfully parsed person: " + name.fullName);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     *
     * @param argumentMultimap the multimap to check; must not be null
     * @param prefixes         the prefixes that must all be present
     * @return true if all specified prefixes are present in the multimap
     */
    private static boolean arePrefixesPresent(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        assert argumentMultimap != null : "ArgumentMultimap must not be null";
        assert prefixes != null : "Prefixes array must not be null";
        assert prefixes.length > 0 : "At least one prefix must be specified";

        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
