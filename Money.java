public class Money {
    private int balance;
    @SuppressWarnings("unused")
    private int bet;

    public Money() {
        this.balance = 1000;  // Starting money
    }

    // Add money
    public void winMoney(int amount) {
        balance += amount;
    }

    // Deduct money
    public void loseMoney(int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        balance -= amount;
    }

    public void placeBet(int betAmount) {
        if (canBet(betAmount)) {
            this.bet = betAmount;
        } else {
            throw new IllegalArgumentException("Insufficient funds for the bet.");
        }
    }

    // Reset the bet after each round
    public void resetBet() {
        this.bet = 0;
    }

    // Check if a bet is valid
    public boolean canBet(int bet) {
        return bet > 0 && bet <= balance;
    }

    // Accessor for balance
    public int getBalance() {
        return balance;
    }
}
