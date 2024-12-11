// Imports
import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Cards> hand;
    private int score;

    public Dealer() {
        hand = new ArrayList<>();
        score = 0;
    }

    // Add a card to the dealer's hand
    public void addCard(Cards card) {
        hand.add(card);
        score = calculateScore();
    }

    // Dealer's turn logic
    public boolean shouldHit() {
        return score < 17;
    }

    // Calculate the dealer's score
    private int calculateScore() {
        int total = 0;
        int aces = 0;

        for (Cards card : hand) {
            String rank = card.getRank();
            switch (rank) {
                case "Jack":
                case "Queen":
                case "King":
                    total += 10;
                    break;
                case "Ace":
                    aces++;
                    total += 11;
                    break;
                default:
                    total += Integer.parseInt(rank);
            }
        }

        // Handle Aces as 1 if total exceeds 21
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    // Get the dealer's score
    public int getScore() {
        return score;
    }

    // Get the dealer's hand
    public List<Cards> getHand() {
        return hand;
    }
}
