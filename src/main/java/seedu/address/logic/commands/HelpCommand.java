package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Here are the available commands:\n\n"
            + "1. ADD\n"
            + "   Format : add -name NAME -phone PHONE_NUMBER -email EMAIL -address ADDRESS [-tag TAG]...\n"
            + "   Example: add -name James Ho -phone 22224444 -email jamesho@example.com "
            + "-address 123, Clementi Rd, 1234665 -tag friend -tag colleague\n\n"
            + "2. EDIT\n"
            + "   Format : edit INDEX [-name NAME] [-phone PHONE_NUMBER] [-email EMAIL] "
            + "[-address ADDRESS] [-tag TAG]...\n"
            + "   Example: edit 2 -name James Lee -email jameslee@example.com\n\n"
            + "3. FIND\n"
            + "   Format : find KEYWORD [MORE_KEYWORDS]\n"
            + "   Example: find James Jake\n\n"
            + "4. DELETE\n"
            + "   Format : delete INDEX\n"
            + "   Example: delete 3\n\n"
            + "5. MARK\n"
            + "   Format : mark INDEX\n"
            + "   Example: mark 1\n\n"
            + "6. REMARK\n"
            + "   Format : remark INDEX -remark REMARK\n"
            + "   Example: remark 1 -remark Strong in algorithms.\n\n"
            + "7. LIST\n"
            + "   Format : list\n\n"
            + "8. CLEAR\n"
            + "   Format : clear\n\n"
            + "9. HELP\n"
            + "   Format : help\n\n"
            + "For the full user guide, visit: https://ay2526s2-cs2103-f09-2.github.io/tp/UserGuide.html";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
