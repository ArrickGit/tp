package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_BOOLEAN = "True/False value is invalid. "
            + "Accepted values: y, n, 1, 0";

    private static final Logger logger = LogsCenter.getLogger(ParserUtil.class);

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param oneBasedIndex the string to parse as a 1-based index; must not be null
     * @return the parsed {@code Index}
     * @throws ParseException if the specified index is invalid (not a non-zero unsigned integer)
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        requireNonNull(oneBasedIndex, "Index string must not be null");

        String trimmedIndex = oneBasedIndex.trim();

        // Defensive: explicitly reject empty string before regex check
        if (trimmedIndex.isEmpty()) {
            logger.info("ParserUtil.parseIndex: received empty string");
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            logger.info("ParserUtil.parseIndex: invalid index string '" + trimmedIndex + "'");
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        int parsed = Integer.parseInt(trimmedIndex);

        // Assertion: value must be positive after passing isNonZeroUnsignedInteger
        assert parsed > 0 : "Parsed index value must be positive";

        Index index = Index.fromOneBased(parsed);
        assert index != null : "Index.fromOneBased must not return null";

        logger.finest("ParserUtil.parseIndex: parsed index " + parsed);
        return index;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param name the string to parse as a name; must not be null
     * @return the parsed {@code Name}
     * @throws ParseException if the given {@code name} is invalid
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name, "Name string must not be null");

        String trimmedName = name.trim();

        if (trimmedName.isEmpty()) {
            logger.info("ParserUtil.parseName: received empty name string");
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }

        if (!Name.isValidName(trimmedName)) {
            logger.info("ParserUtil.parseName: invalid name '" + trimmedName + "'");
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }

        Name parsedName = new Name(trimmedName);
        assert parsedName != null : "Constructed Name must not be null";

        return parsedName;
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param phone the string to parse as a phone number; must not be null
     * @return the parsed {@code Phone}
     * @throws ParseException if the given {@code phone} is invalid
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone, "Phone string must not be null");

        String trimmedPhone = phone.trim();

        if (trimmedPhone.isEmpty()) {
            logger.info("ParserUtil.parsePhone: received empty phone string");
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }

        if (!Phone.isValidPhone(trimmedPhone)) {
            logger.info("ParserUtil.parsePhone: invalid phone '" + trimmedPhone + "'");
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }

        Phone parsedPhone = new Phone(trimmedPhone);
        assert parsedPhone != null : "Constructed Phone must not be null";

        return parsedPhone;
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param address the string to parse as an address; must not be null
     * @return the parsed {@code Address}
     * @throws ParseException if the given {@code address} is invalid
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address, "Address string must not be null");

        String trimmedAddress = address.trim();

        if (trimmedAddress.isEmpty()) {
            logger.info("ParserUtil.parseAddress: received empty address string");
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }

        if (!Address.isValidAddress(trimmedAddress)) {
            logger.info("ParserUtil.parseAddress: invalid address '" + trimmedAddress + "'");
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }

        Address parsedAddress = new Address(trimmedAddress);
        assert parsedAddress != null : "Constructed Address must not be null";

        return parsedAddress;
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param email the string to parse as an email address; must not be null
     * @return the parsed {@code Email}
     * @throws ParseException if the given {@code email} is invalid
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email, "Email string must not be null");

        String trimmedEmail = email.trim();

        if (trimmedEmail.isEmpty()) {
            logger.info("ParserUtil.parseEmail: received empty email string");
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }

        if (!Email.isValidEmail(trimmedEmail)) {
            logger.info("ParserUtil.parseEmail: invalid email '" + trimmedEmail + "'");
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }

        Email parsedEmail = new Email(trimmedEmail);
        assert parsedEmail != null : "Constructed Email must not be null";

        return parsedEmail;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param tag the string to parse as a tag; must not be null
     * @return the parsed {@code Tag}
     * @throws ParseException if the given {@code tag} is invalid
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag, "Tag string must not be null");

        String trimmedTag = tag.trim();

        if (trimmedTag.isEmpty()) {
            logger.info("ParserUtil.parseTag: received empty tag string");
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }

        if (!Tag.isValidTagName(trimmedTag)) {
            logger.info("ParserUtil.parseTag: invalid tag '" + trimmedTag + "'");
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }

        Tag parsedTag = new Tag(trimmedTag);
        assert parsedTag != null : "Constructed Tag must not be null";

        return parsedTag;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     *
     * @param tags the collection of raw tag strings; must not be null
     * @return a set of parsed {@code Tag} objects
     * @throws ParseException if any individual tag string is invalid
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags, "Tags collection must not be null");

        logger.fine("ParserUtil.parseTags: parsing " + tags.size() + " tag(s)");

        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            // Defensive: guard against null elements in the collection
            if (tagName == null) {
                logger.warning("ParserUtil.parseTags: encountered null tag name in collection");
                throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
            }
            tagSet.add(parseTag(tagName));
        }

        assert tagSet != null : "Tag set must not be null after parsing";
        assert tagSet.size() <= tags.size()
                : "Tag set size must not exceed input size (duplicates are deduplicated)";

        logger.fine("ParserUtil.parseTags: successfully parsed "
                + tagSet.size() + " unique tag(s) from " + tags.size() + " input(s)");

        return tagSet;
    }

    /**
     * Parses a boolean string token into a {@code boolean}.
     * Accepted values: {@code y}, {@code 1} for true; {@code n}, {@code 0} for false.
     *
     * @param bool the string to parse as a boolean; must not be null
     * @return {@code true} if the input is {@code y} or {@code 1};
     *         {@code false} if the input is {@code n} or {@code 0}
     * @throws ParseException if the input is not a recognised boolean token
     */
    public static boolean parseBoolean(String bool) throws ParseException {
        requireNonNull(bool, "Boolean string must not be null");

        // Defensive: reject empty string before comparison
        if (bool.trim().isEmpty()) {
            logger.info("ParserUtil.parseBoolean: received empty boolean string");
            throw new ParseException(MESSAGE_INVALID_BOOLEAN);
        }

        logger.fine("ParserUtil.parseBoolean: parsing '" + bool + "'");

        switch (bool.trim()) {
        case "y":
        case "1":
            logger.finest("ParserUtil.parseBoolean: resolved to true");
            return true;
        case "n":
        case "0":
            logger.finest("ParserUtil.parseBoolean: resolved to false");
            return false;
        default:
            logger.info("ParserUtil.parseBoolean: unrecognised boolean token '" + bool + "'");
            throw new ParseException(MESSAGE_INVALID_BOOLEAN);
        }
    }
}
