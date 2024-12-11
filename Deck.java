// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck 
{
    private List<Cards> deck; // Using the list of cards from Cards.java to create a Deck
    private ArrayList<Cards> discardPile; // Create a discard pile to be added back into the deck

    public Deck() 
    {
        deck = new ArrayList<>(); // Create an ArrayList for each individual card to be put into
        discardPile = new ArrayList<>(); // Creates a discard pile to be reshuffled back into the deck
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"}; // List of suits to give to the cards
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"}; // List of ranks to give to the cards

        // Create a full deck of cards
        for (String suit : suits) // For each suit
        {
            for (String rank : ranks) // And for each rank
            {
                deck.add(new Cards(suit, rank)); // Create an entire deck of cards using all the combinations of ranks and suits
            }
        }
        shuffle();
    }

    // Shuffle the deck
    public void shuffle() 
    {
        Collections.shuffle(deck); // Shuffle the deck
    }

    // Deal a card
    public Cards dealCard() 
    {
        if (deck.isEmpty()) // Check if the deck is empty
        {
            replenishDeck();
        }
        Cards dealtCard = deck.remove(0); // If there is a card available, remove it from the deck
        discardPile.add(dealtCard);
        return dealtCard;
    }

    private void replenishDeck() {
        if (discardPile.isEmpty()) {
            throw new IllegalStateException("Both the deck and discard pile are empty!");
        }
        deck.addAll(discardPile);
        discardPile.clear();
        shuffle();
    }
    

    // Get the size of the deck
    public int getSize() 
    {
        return deck.size(); // Return the size of the deck
    }
}