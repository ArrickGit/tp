package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.IsInterviewedPredicate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    // ──────────────────────────────────────────────
    // Invalid input — should throw ParseException
    // ──────────────────────────────────────────────

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        // value provided but without the -interviewed prefix
        assertParseFailure(parser, "y",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_wrongPrefix_throwsParseException() {
        // unrecognised prefix
        assertParseFailure(parser, "-name y",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unexpectedPreamble_throwsParseException() {
        assertParseFailure(parser, "oops -interviewed y",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateInterviewedPrefix_throwsParseException() {
        assertParseFailure(parser, "-interviewed y -interviewed n",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_prefixPresentButNoValue_throwsParseException() {
        // prefix with no value after it
        assertParseFailure(parser, "-interviewed",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    // ──────────────────────────────────────────────
    // Valid input — "interviewed" truthy values
    // ──────────────────────────────────────────────

    @Test
    public void parse_interviewedY_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(new IsInterviewedPredicate(true));
        assertParseSuccess(parser, " -interviewed y", expected);
    }

    @Test
    public void parse_interviewedOne_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(new IsInterviewedPredicate(true));
        assertParseSuccess(parser, " -interviewed 1", expected);
    }

    // ──────────────────────────────────────────────
    // Valid input — "not interviewed" falsy values
    // ──────────────────────────────────────────────

    @Test
    public void parse_interviewedN_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(new IsInterviewedPredicate(false));
        assertParseSuccess(parser, " -interviewed n", expected);
    }

    @Test
    public void parse_interviewedZero_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(new IsInterviewedPredicate(false));
        assertParseSuccess(parser, " -interviewed 0", expected);
    }

    // ──────────────────────────────────────────────
    // Whitespace handling
    // ──────────────────────────────────────────────

    @Test
    public void parse_extraWhitespaceAroundValue_returnsFilterCommand() {
        // leading/trailing spaces around the value should be trimmed
        FilterCommand expected = new FilterCommand(new IsInterviewedPredicate(true));
        assertParseSuccess(parser, " -interviewed   y  ", expected);
    }

    @Test
    public void parse_leadingAndTrailingWhitespaceInArgs_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(new IsInterviewedPredicate(false));
        assertParseSuccess(parser, "   -interviewed 0   ", expected);
    }

    @Test
    public void parse_leadingAndNewlineInArgs_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(new IsInterviewedPredicate(false));
        assertParseSuccess(parser, "\n -interviewed 0 \n", expected);
    }
}
