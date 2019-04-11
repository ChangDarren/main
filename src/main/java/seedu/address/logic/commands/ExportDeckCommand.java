package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.DecksView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.deck.Deck;

/**
 * Exports a deck identified using it's displayed index from TopDeck.
 */
public class ExportDeckCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports the deck identified by the index number used in the displayed deck list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_EXPORT_DECK_SUCCESS = "Successfully Exported Deck: %1$s to %2$s";
    public static final String DEFAULT_INDEX = "1";
    public static final String AUTOCOMPLETE_TEXT = COMMAND_WORD + " " + DEFAULT_INDEX;

    private final Index targetIndex;

    public ExportDeckCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        DecksView decksView = (DecksView) model.getViewState();
        List<Deck> currentDeckList = decksView.getFilteredList();

        if (targetIndex.getZeroBased() >= currentDeckList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DECK_DISPLAYED_INDEX);
        }

        Deck deckToExport = currentDeckList.get(targetIndex.getZeroBased());
        String exportPath = model.exportDeck(deckToExport);
        System.out.println(exportPath);
        model.commitTopDeck();
        return new CommandResult(String.format(MESSAGE_EXPORT_DECK_SUCCESS, deckToExport, exportPath));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExportDeckCommand // instanceof handles nulls
                && targetIndex.equals(((ExportDeckCommand) other).targetIndex)); // state check
    }
}

