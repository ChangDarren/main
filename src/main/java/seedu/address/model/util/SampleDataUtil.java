package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyTopDeck;
import seedu.address.model.TopDeck;
import seedu.address.model.deck.Card;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Card[] getSampleCards() {
        return new Card[] {
                new Card("What transport does HTTP use?", "TCP", getTagSet("CS2105")),
                new Card("How many sides does a triangle have?", "3", getTagSet("Geometry")),
                new Card("How many seconds are there in 1 millisecond?", "1 * 10^-3", null)
        };
    }

    public static ReadOnlyTopDeck getSampleTopDeck() {
        TopDeck sampleTd = new TopDeck();
        for (Card sampleCard : getSampleCards()) {
            sampleTd.addCard(sampleCard);
        }
        return sampleTd;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
