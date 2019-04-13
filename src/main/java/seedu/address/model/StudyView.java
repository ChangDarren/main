package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import seedu.address.logic.DeckShuffler;
import seedu.address.logic.commands.BackCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.OpenDeckCommand;
import seedu.address.logic.commands.ShowAnswerCommand;
import seedu.address.logic.parser.GenerateQuestionCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.deck.Card;
import seedu.address.model.deck.Deck;
import seedu.address.model.deck.exceptions.EmptyDeckException;
import seedu.address.ui.StudyPanel;
import seedu.address.ui.UiPart;

/**
 * ViewState of the Application during a Study session.
 */
public class StudyView implements ViewState {
    private final Deck activeDeck;
    private final SimpleObjectProperty<StudyState> currentStudyState = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<String> textShown = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<String> userAnswer = new SimpleObjectProperty<>();
    private Card currentCard;
    private DeckShuffler deckShuffler;

    public StudyView(Deck deck) throws EmptyDeckException {
        if (deck.isEmpty()) {
            throw new EmptyDeckException("Unable to create study view with empty deck");
        }
        this.activeDeck = deck;
        this.deckShuffler = new DeckShuffler(activeDeck);
        generateCard();
        setCurrentStudyState(StudyState.QUESTION);
    }

    public StudyView(StudyView studyView) {
        this.activeDeck = studyView.getActiveDeck();
        this.deckShuffler = new DeckShuffler(studyView.getDeckShuffler());
        this.setCurrentCard(studyView.getCurrentCard());
        this.setCurrentStudyState(studyView.getCurrentStudyState());
    }

    @Override
    public Command parse(String commandWord, String arguments) throws ParseException {
        switch (commandWord) {
            case OpenDeckCommand.COMMAND_WORD:
                return new OpenDeckCommand(activeDeck);
            case BackCommand.COMMAND_WORD:
                return new BackCommand();
            case ExitCommand.COMMAND_WORD:
                return new ExitCommand();
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand();
            case HistoryCommand.COMMAND_WORD:
                return new HistoryCommand();
            default:
                if (getCurrentStudyState() == StudyState.QUESTION) {
                    return new ShowAnswerCommand(commandWord + arguments);
                } else {
                    return new GenerateQuestionCommandParser(this).parse(commandWord);
                }
        }
    }

    public DeckShuffler getDeckShuffler() {
        return deckShuffler;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    /**
     * Sets the current card to be studied.
     */
    public void setCurrentCard(Card card) {
        requireNonNull(card);
        currentCard = card;
        updateTextShown();
    }

    /**
     * Generates the next card to be studied.
     */
    public void generateCard() {
        Card card = deckShuffler.generateCard();
        setCurrentCard(card);
    }

    public StudyState getCurrentStudyState() {
        return currentStudyState.getValue();
    }

    public void setCurrentStudyState(StudyState state) {
        requireNonNull(state);
        currentStudyState.setValue(state);
        updateTextShown();
    }

    /**
     * Updates the text shown in the UI.
     */
    private void updateTextShown() {
        String text = (getCurrentStudyState() == StudyState.QUESTION) ? currentCard
                .getQuestion() : currentCard.getAnswer();
        textShown.setValue(text);
    }

    /**
     * Returns the current textShown
     */
    public ReadOnlyProperty<String> textShownProperty() {
        updateTextShown();
        return textShown;
    }

    public String getUserAnswer() {
        return userAnswer.getValue();
    }

    public void setUserAnswer(String answer) {
        requireNonNull(answer);
        userAnswer.setValue(answer);
    }

    public UiPart<Region> getPanel() {
        return new StudyPanel(textShown, currentStudyState, userAnswer);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof StudyView)) {
            return false;
        }
        // state check
        StudyView other = (StudyView) obj;
        return Objects.equals(currentStudyState.getValue(), other.currentStudyState.getValue()) && Objects
                .equals(deckShuffler, other.deckShuffler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentStudyState.getValue(), deckShuffler);
    }

    /**
     * The type of possible states that the study view can have.
     */
    public enum StudyState { QUESTION, ANSWER }


}
