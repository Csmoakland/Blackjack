public class Cards 
{
    private String suit;  // Hearts, Diamonds, Clubs, Spades
    private String rank;  // 2-10, Jack, Queen, King, Ace

    public Cards(String suit, String rank) // Create cards with suits and ranks
    {
        this.suit = suit; // Give the card a suit
        this.rank = rank; // Give the card a rank
    }

    // Accessors
    public String getSuit() // Returns the suit of the card
    {
        return suit;
    }

    public String getRank() // Returns the rank of the card
    {
        return rank;
    }

    @Override
    public String toString() // Converts the card to a readable string (e.g., "Ace of Spades")
    {
        return rank + " of " + suit;
    }
}
