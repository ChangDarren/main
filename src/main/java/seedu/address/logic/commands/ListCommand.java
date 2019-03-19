package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CARDS;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

/**
 * Lists all decks to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all decks";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredList(PREDICATE_SHOW_ALL_CARDS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
