package seedu.address.logic;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import seedu.address.logic.commands.CloseDeckCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.deck.Deck;

public class StudyView implements ListViewState {
    private Model model;
    public final FilteredList<Deck> filteredDecks;
    private final SimpleObjectProperty<Deck> selectedDeck = new SimpleObjectProperty<>();

    public StudyView(Model model, FilteredList<Deck> deckList) {
        this.model = model;
        filteredDecks = deckList;
        filteredDecks.addListener(this::ensureSelectedItemIsValid);
    }

    @Override
    public Command parse(String commandWord, String arguments) throws ParseException {
        switch (commandWord) {
            case CloseDeckCommand.COMMAND_WORD:
                return new CloseDeckCommand();
            default:
                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Ensures {@code selectedItem} is a valid card in {@code filteredItems}.
     */
//    private void ensureSelectedItemIsValid(ListChangeListener.Change<? extends Deck> change) {
//        while (change.next()) {
//            if (selectedDeck.getValue() == null) {
//                // null is always a valid selected card, so we do not need to check that it is valid anymore.
//                return;
//            }
//
//            boolean wasSelectedItemReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
//                    && change.getRemoved().contains(selectedDeck.getValue());
//            if (wasSelectedItemReplaced) {
//                // Update selectedDeck to its new value.
//                int index = change.getRemoved().indexOf(selectedDeck.getValue());
//                selectedDeck.setValue(change.getAddedSubList().get(index));
//                continue;
//            }
//
//            boolean wasSelectedItemRemoved = change.getRemoved().stream()
//                    .anyMatch(removedItem -> selectedDeck.getValue().equals(removedItem));
//            if (wasSelectedItemRemoved) {
//                // Select the card that came before it in the list,
//                // or clear the selection if there is no such card.
//                selectedDeck.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
//            }
//        }
//    }
}
