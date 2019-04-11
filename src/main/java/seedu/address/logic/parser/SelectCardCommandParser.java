package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SelectCardCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.CardsView;

/**
 * Parses input arguments and creates a new SelectCardCommand object
 */
public class SelectCardCommandParser implements Parser<SelectCommand> {

    private CardsView cardsView;

    public SelectCardCommandParser(CardsView cardsView) {
        this.cardsView = cardsView;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCardCommand
     * and returns a SelectCardCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SelectCardCommand(cardsView, index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCardCommand.MESSAGE_USAGE), pe);
        }
    }
}
