package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalCards.getTypicalCards;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.deck.Card;

public class CardListPanelTest extends GuiUnitTest {
    private static final ObservableList<Card> TYPICAL_CARDS =
            FXCollections.observableList(getTypicalCards());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Card> selectedPerson = new SimpleObjectProperty<>();
    private PersonListPanelHandle personListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_CARDS);

        for (int i = 0; i < TYPICAL_CARDS.size(); i++) {
            personListPanelHandle.navigateToCard(TYPICAL_CARDS.get(i));
            Card expectedCard = TYPICAL_CARDS.get(i);
            PersonCardHandle actualCard = personListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysPerson(expectedCard, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedPersonChanged_selectionChanges() {
        initUi(TYPICAL_CARDS);
        Card secondCard = TYPICAL_CARDS.get(INDEX_SECOND_PERSON.getZeroBased());
        guiRobot.interact(() -> selectedPerson.set(secondCard));
        guiRobot.pauseForHuman();

        PersonCardHandle expectedPerson = personListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        PersonCardHandle selectedPerson = personListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of persons in {@code CardListPanel} requires lesser than
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
     * {@code CardListPanel}.
     */
    private ObservableList<Card> createBackingList(int personCount) {
        ObservableList<Card> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < personCount; i++) {
            Name name = new Name(i + "a");
            Phone phone = new Phone("000");
            Email email = new Email("a@aa");
            Address address = new Address("a");
            Card card = new Card(name, phone, email, address, Collections.emptySet());
            backingList.add(card);
        }
        return backingList;
    }

    /**
     * Initializes {@code personListPanelHandle} with a {@code CardListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code CardListPanel}.
     */
    private void initUi(ObservableList<Card> backingList) {
        CardListPanel cardListPanel =
                new CardListPanel(backingList, selectedPerson, selectedPerson::set);
        uiPartRule.setUiPart(cardListPanel);

        personListPanelHandle = new PersonListPanelHandle(getChildNode(cardListPanel.getRoot(),
                PersonListPanelHandle.PERSON_LIST_VIEW_ID));
    }
}
