package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {

    private RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_validArgs_returnsRemarkCommand() {
        assertParseSuccess(parser, "1 Strong in algorithms.",
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Strong in algorithms.")));
    }

    @Test
    public void parse_deleteRemark_returnsRemarkCommand() {
        assertParseSuccess(parser, "1",
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark("")));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "a Strong in algorithms.",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deprecatedPrefix_throwsParseException() {
        assertParseFailure(parser, "1 -remark Strong in algorithms.",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tooLongRemark_throwsParseException() {
        String longRemark = String.join("", Collections.nCopies(RemarkCommand.MAX_REMARK_LENGTH + 1, "a"));
        assertParseFailure(parser, "1 " + longRemark, RemarkCommand.MESSAGE_CONSTRAINTS);
    }
}
