package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class TopDeckParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern
            .compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @param viewStateParser the state-specific parser
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput, ViewStateParser viewStateParser) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches() && !userInput.equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        String commandWord = "";
        String arguments = "";
        if (!userInput.equals("")) {
            commandWord = matcher.group("commandWord");
            arguments = matcher.group("arguments");
        }
        try {
            return viewStateParser.parse(commandWord, arguments);
        } catch (ParseException e) {
            switch (commandWord) {
                case HistoryCommand.COMMAND_WORD:
                    return new HistoryCommand();
                case ExitCommand.COMMAND_WORD:
                    return new ExitCommand();
                case HelpCommand.COMMAND_WORD:
                    return new HelpCommand();
                default:
                    throw e;
            }
        }
    }

}
