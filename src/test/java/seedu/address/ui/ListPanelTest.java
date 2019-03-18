package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalCards.getTypicalCards;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CARD;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplayEquals;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysCardObject;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.CardDisplayHandle;
import guitests.guihandles.ListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.deck.Card;

public class ListPanelTest extends GuiUnitTest {
    private static final ObservableList<Card> TYPICAL_CARDS =
            FXCollections.observableList(getTypicalCards());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Card> selectedPerson = new SimpleObjectProperty<>();
    private ListPanelHandle listPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_CARDS);

        for (int i = 0; i < TYPICAL_CARDS.size(); i++) {
            listPanelHandle.navigateToCard(TYPICAL_CARDS.get(i));
            Card expectedCard = TYPICAL_CARDS.get(i);
            CardDisplayHandle actualCard = listPanelHandle.getCardDiplayHandle(i);

            assertCardDisplaysCardObject(expectedCard, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedPersonChanged_selectionChanges() {
        initUi(TYPICAL_CARDS);
        Card secondCard = TYPICAL_CARDS.get(INDEX_SECOND_CARD.getZeroBased());
        guiRobot.interact(() -> selectedPerson.set(secondCard));
        guiRobot.pauseForHuman();

        CardDisplayHandle expectedPerson = listPanelHandle.getCardDiplayHandle(INDEX_SECOND_CARD.getZeroBased());
        CardDisplayHandle selectedPerson = listPanelHandle.getHandleToSelectedCard();
        assertCardDisplayEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of cards in {@code ListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Card> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of card cards exceeded time limit");
    }

    /**
     * Returns a list of persons containing {@code personCount} persons that is used to populate the
     * {@code ListPanel}.
     */
    private ObservableList<Card> createBackingList(int personCount) {
        ObservableList<Card> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < personCount; i++) {
            String question = i + " question";
            String answer = "ans";
            Card card = new Card(question, answer, Collections.emptySet());
            backingList.add(card);
        }
        return backingList;
    }

    /**
     * Initializes {@code listPanelHandle} with a {@code ListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code ListPanel}.
     */
    private void initUi(ObservableList<Card> backingList) {
        ListPanel listPanel =
                new ListPanel(backingList, selectedPerson);
        uiPartRule.setUiPart(listPanel);

        listPanelHandle = new ListPanelHandle(getChildNode(listPanel.getRoot(),
                ListPanelHandle.CARD_LIST_VIEW_ID));
    }
}
