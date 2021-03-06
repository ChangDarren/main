package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_DECKS;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CardsView;
import seedu.address.model.DecksView;
import seedu.address.model.Model;
import seedu.address.model.ViewState;
import seedu.address.model.deck.Deck;
import seedu.address.model.deck.exceptions.DeckNotFoundException;

/**
 * Reverts the {@code model}'s address book to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";
    private final ViewState viewState;

    public UndoCommand(ViewState viewState) {
        this.viewState = viewState;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoTopDeck()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoTopDeck();
        if (model.isAtDecksView()) {
            DecksView decksView = (DecksView) viewState;
            decksView.updateFilteredList(PREDICATE_SHOW_ALL_DECKS);
        } else if (model.isAtCardsView()) {
            CardsView currentView = (CardsView) viewState;
            Deck currentActiveDeck = currentView.getActiveDeck();

            try {
                Deck newActiveDeck = model.getDeck(currentActiveDeck);
                model.changeDeck(newActiveDeck);
            } catch (DeckNotFoundException exception) {
                model.goToDecksView();
            }

        }

        return new UpdatePanelCommandResult(MESSAGE_SUCCESS);
    }
}
