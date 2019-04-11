package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.DecksView;
import seedu.address.model.Model;
import seedu.address.model.deck.Deck;

/**
 * Selects a deck identified using its displayed index.
 */
public class SelectDeckCommand extends SelectCommand {

    private DecksView decksView;

    public SelectDeckCommand(DecksView decksView, Index targetIndex) {
        super(targetIndex);
        this.decksView = decksView;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<Deck> filteredDeckList = decksView.filteredDecks;

        if (targetIndex.getZeroBased() >= filteredDeckList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        decksView.setSelectedItem(filteredDeckList.get(targetIndex.getZeroBased()));
        return new CommandResult(String.format(MESSAGE_SELECT_SUCCESS, targetIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectDeckCommand // instanceof handles nulls
                && targetIndex.equals(((SelectDeckCommand) other).targetIndex)); // state check
    }
}
