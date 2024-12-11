import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

@SuppressWarnings("unused")
public class BlackjackGameApp extends Application 
{

    private Scene mainMenuScene;
    private Scene gameScene;
    private Scene resultScene;
    private Stage stage;

    private Deck deck;
    private Dealer dealer;
    private Money playerMoney;
    private List<Cards> playerHand;
    private int playerScore;
    private int currentBet;
    private int roundsWon = 0;
    private int roundsLost = 0;
    private int roundsTied = 0;
    private int moneyWon = 0;
    private int moneyLost = 0;
    private double winPercentage;
    private boolean isDealerSecondCardHidden = true;
    private boolean splitHand = false;
    private Cards firstCard;
    private Cards secondCard;
    private int splitBet;
    private List<Cards> playerHand2;
    private int splitScore;
    private String splitResult;

    private HBox playerHandBox;
    private HBox dealerHandBox;
    private HBox buttonBox;
    private Label playerMoneyLabel;
    private Label playerScoreLabel;
    private Label dealerScoreLabel;
    private Label resultLabel;
    private Label currentBetLabel;
    private Label errorLabel;


    @Override
    public void start(Stage stage) 
    {
        // Initialize game objects
        deck = new Deck();
        dealer = new Dealer();
        playerMoney = new Money();
        playerHand = new ArrayList<>();
        playerHand2 = new ArrayList<>();

        // Shuffle the deck
        deck.shuffle();

        // Create scenes
        mainMenuScene = createMainMenu(stage);
        gameScene = createGameScene(stage);
        resultScene = createResultsScreen(stage);

        // Set up the stage
        stage.setTitle("Blackjack Game");
        stage.setScene(mainMenuScene);
        stage.show();

    }

    private void setBackground(Pane layout, String imagePath) {
        // Load the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
    
        // Create the BackgroundImage object
        BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT, // No tiling
            BackgroundRepeat.NO_REPEAT, // No tiling
            BackgroundPosition.CENTER,  // Center the image
            new BackgroundSize(
                100, 100, true, true, false, true // Scale to fill
            )
        );
    
        // Apply the background to the layout
        layout.setBackground(new Background(bgImage));
    }
    

    private void updatePlayerMoneyDisplay() 
    {
        playerMoneyLabel.setText("Player Money: $" + playerMoney.getBalance());
    }

    private void updateCurrentBetDisplay() 
    {
        currentBetLabel.setText("Current Bet: $" + currentBet);
    }

    private void updateWinScore() 
    {
        playerMoney.winMoney(currentBet);
        moneyWon += currentBet;
        roundsWon++;
    }

    private void updateLoseScore()
    {
        playerMoney.loseMoney(currentBet);
        moneyLost += currentBet;
        roundsLost++;
    }

    private void cashOut(Stage stage)
    {
        stage.setScene(createResultsScreen(stage));
    }
    
    private Scene createMainMenu(Stage stage) 
    {
        BorderPane mainMenuPane = new BorderPane();
        HBox menuLayout = new HBox(20);
        setBackground(mainMenuPane, "/resources/menu_background.png");

        Label gameTitleLabel = new Label("A Game of Blackjack");
        gameTitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 40px");
        Label gameSubtitleLabel = new Label("by Cole Smolinski");
        gameSubtitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px");

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 20px");
        playButton.setPrefSize(150, 50);
        playButton.setOnAction(_ -> startBetting(stage));
        {
            stage.setScene(gameScene);
        };

        Button quitButton = new Button("Quit");
        quitButton.setStyle("-fx-font-size: 20px");
        quitButton.setPrefSize(150, 50);
        quitButton.setOnAction(_ -> stage.close());

        menuLayout.getChildren().addAll(playButton, quitButton);
        menuLayout.setAlignment(Pos.CENTER);

        VBox mainMenu = new VBox(20, gameTitleLabel, gameSubtitleLabel, menuLayout);
        mainMenu.setAlignment(Pos.CENTER);

        mainMenuPane.setCenter(mainMenu);

        return new Scene(mainMenuPane, 800, 600);
    }

    private void startBetting(Stage stage) 
    {
        if (playerMoney.getBalance() <= 0) 
        {
            stage.setScene(createResultsScreen(stage));
        } 
        else 
        {
            stage.setScene(createBettingScene(stage));
        }
    }
    

    private Scene createBettingScene(Stage stage) 
    {
        VBox bettingLayout = new VBox(20);
        setBackground(bettingLayout, "/resources/menu_background.png");
        bettingLayout.setAlignment(Pos.CENTER);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        Label bettingPrompt = new Label("Enter your bet:");
        bettingPrompt.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        Label playerMoneyLabel = new Label("Current Balance: $" + playerMoney.getBalance());
        playerMoneyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px");
        TextField betInput = new TextField();
        betInput.setPromptText("Enter amount");
        betInput.setStyle("-fx-font-size: 20px");
        betInput.setMaxWidth(145);
        betInput.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20px");

        Button placeBetButton = new Button("Place Bet");
        placeBetButton.setStyle("-fx-font-size: 15px");
        placeBetButton.setOnAction(_ -> {
            try 
            {
                int bet = Integer.parseInt(betInput.getText());
                if (bet <= 0) 
                {
                    throw new NumberFormatException("Bet must be positive.");
                }
                if (!playerMoney.canBet(bet)) 
                {
                    errorLabel.setText("Insufficient funds.");
                    pause.setOnFinished(event -> errorLabel.setText(""));
                    pause.play();
                } 
                else 
                {
                    // Place bet and start the game
                    playerMoney.placeBet(bet);
                    currentBet = bet;
                    updatePlayerMoneyDisplay();
                    updateCurrentBetDisplay();
                    startNewGame();
                    stage.setScene(gameScene);
                }
            } 
            catch (NumberFormatException ex) 
            {
                errorLabel.setText("Invalid bet amount.");
            }
        });
        
        Button cashOutButton = new Button("Cash Out");
        cashOutButton.setStyle("-fx-font-size: 15px");
        cashOutButton.setOnAction(_ -> {
            cashOut(stage);
        });

        bettingLayout.getChildren().addAll(bettingPrompt, playerMoneyLabel, betInput, placeBetButton, cashOutButton, errorLabel);

        return new Scene(bettingLayout, 800, 600);
    }

    private Scene createGameScene(Stage stage) 
    {
        BorderPane gameLayout = new BorderPane();
        setBackground(gameLayout, "/resources/menu_background.png");
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
    
        // Player and dealer card areas
        playerHandBox = new HBox(10);
        playerHandBox.setAlignment(Pos.CENTER);
        dealerHandBox = new HBox(10);
        dealerHandBox.setAlignment(Pos.CENTER);

        // Create Tooltips for each button
        Tooltip hitTooltip = new Tooltip("Adds an additional card to your hand.");
        Tooltip standTooltip = new Tooltip("End your turn and begin the dealer's turn.");
        Tooltip cashOutTooltip = new Tooltip("End the game with current money and show results.");
        Tooltip doubleDownTooltip = new Tooltip("Double your bet and draw one card this round.");
        Tooltip splitTooltip = new Tooltip("If both cards are the same rank, split them into two piles and play two games.");
        
        // Labels for scores and results
        playerScoreLabel = new Label("Player Score: 0");
        playerScoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
        dealerScoreLabel = new Label("Dealer Score: ?");
        dealerScoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
        resultLabel = new Label("");
        resultLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
        currentBetLabel = new Label("Current Bet: $0");
        currentBetLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
    
        // Money label
        playerMoneyLabel = new Label("Player Money: $" + playerMoney.getBalance());
        playerMoneyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
    
        VBox infoBox = new VBox(10, playerScoreLabel, dealerScoreLabel, playerMoneyLabel, currentBetLabel, resultLabel);
        infoBox.setAlignment(Pos.CENTER);
    
        Button hitButton = new Button("Hit");
        hitButton.setStyle("-fx-font-size: 20px");
        Tooltip.install(hitButton, hitTooltip);
        Button standButton = new Button("Stand");
        standButton.setStyle("-fx-font-size: 20px");
        Tooltip.install(standButton, standTooltip);
        Button cashOutButton = new Button("Cash Out");
        cashOutButton.setStyle("-fx-font-size: 20px");
        Tooltip.install(cashOutButton, cashOutTooltip);
        Button doubleDownButton = new Button ("Double Down");
        doubleDownButton.setStyle("-fx-font-size: 20px");
        Tooltip.install(doubleDownButton, doubleDownTooltip);
        Button splitButton = new Button ("Split");
        splitButton.setStyle("-fx-font-size: 20px");
        Tooltip.install(splitButton, splitTooltip);
    
        // Define actions for each button
        hitButton.setOnAction(_ -> handleHit());
        standButton.setOnAction(_ -> handleStand());
        doubleDownButton.setOnAction(_ -> {
        if (playerHand.size() > 2)
            {
                errorLabel.setText("Already hit. Cannot double down.");
                pause.setOnFinished(event -> errorLabel.setText(""));
                pause.play();
            }
            else if ((currentBet * 2) > playerMoney.getBalance()) 
            {
                errorLabel.setText("Not enough money to double down.");
                pause.setOnFinished(event -> errorLabel.setText(""));
                pause.play();
            }
            else
            {
                handleDoubleDown();
            }
        });
        
        cashOutButton.setOnAction(_ -> cashOut(stage));

        buttonBox = new HBox(20, hitButton, standButton, doubleDownButton, cashOutButton);
        buttonBox.setAlignment(Pos.CENTER);
    
        // Combine elements
        VBox centerBox = new VBox(20, dealerHandBox, infoBox, playerHandBox, buttonBox, errorLabel);
        centerBox.setAlignment(Pos.CENTER);
    
        // Back to menu button
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(_ -> stage.setScene(mainMenuScene));
    
        gameLayout.setTop(backButton);
        gameLayout.setCenter(centerBox);
    
        return new Scene(gameLayout, 800, 600);
    }

    private void startNewGame() 
    {
        // Reset game state
        playerHand.clear();
        playerHand2.clear();
        dealer = new Dealer();
        playerScore = 0;
        playerHandBox.getChildren().clear();
        dealerHandBox.getChildren().clear();
        resultLabel.setText("");
        deck.shuffle();
        
        // Deal initial cards
        playerHand.add(deck.dealCard());
        playerHand.add(deck.dealCard());
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        // Update UI
        updatePlayerHandUI();
        updateDealerHandUI(true); // Hide dealer's second card
        updateScores();
        updatePlayerMoneyDisplay();
        updateCurrentBetDisplay();
    }

    private void handleHit() 
    {
        // Player draws a card
        Cards card = deck.dealCard();
        playerHand.add(card);
        updatePlayerHandUI();
        updateScores();

        // Check for bust
        if (playerScore > 21)
        {
            updateDealerHandUI(false);
            endGame("Player Busts! Dealer Wins.");
        }
    }


    private void handleStand() 
    {

        // Dealer's turn
        updateDealerHandUI(false); // Reveal dealer's hand
        while (dealer.shouldHit()) 
        {
            dealer.addCard(deck.dealCard());
            updateDealerHandUI(false);
        }

        // Determine the result
        int dealerScore = dealer.getScore();
        if (dealerScore > 21) 
        {
            endGame("Dealer Busts! Player Wins.");
        } 
        else if (playerScore > dealerScore) 
        {
            endGame("Player Wins!");
        } 
        else if (playerScore < dealerScore) 
        {
            endGame("Dealer Wins!");
        } 
        else 
        {
            endGame("It's a Tie!");
        }
    }

    private void handleDoubleDown()
    {
        // Double player's bet and draw one card
        currentBet = currentBet * 2;
        Cards card = deck.dealCard();
        playerHand.add(card);
        updatePlayerHandUI();
        updateScores();
        if (playerScore > 21)
        {
            updateDealerHandUI(false);
            endGame("Player Busts! Dealer Wins.");
        }
        else
        {
            handleStand();
        }
    }

    private void updatePlayerHandUI() 
    {
        playerHandBox.getChildren().clear();
        for (Cards card : playerHand) 
        {
            ImageView cardImage = new ImageView(new Image(getClass().getResourceAsStream("/cards/" + card.getRank() + "_of_" + card.getSuit() + ".png")));
            cardImage.setFitWidth(100);
            cardImage.setPreserveRatio(true);
            playerHandBox.getChildren().add(cardImage);
        }
    }

    private void updateDealerHandUI(boolean hideSecondCard) 
    {
        dealerHandBox.getChildren().clear();
        List<Cards> dealerHand = dealer.getHand();

        isDealerSecondCardHidden = hideSecondCard;

        for (int i = 0; i < dealerHand.size(); i++) 
        {
            if (i == 1 && hideSecondCard) 
            {
                ImageView cardBack = new ImageView(new Image(getClass().getResourceAsStream("/cards/Card_Back.png")));
                cardBack.setFitWidth(100);
                cardBack.setPreserveRatio(true);
                dealerHandBox.getChildren().add(cardBack);
            } 
            else 
            {
                Cards card = dealerHand.get(i);
                ImageView cardImage = new ImageView(new Image(getClass().getResourceAsStream("/cards/" + card.getRank() + "_of_" + card.getSuit() + ".png")));
                cardImage.setFitWidth(100);
                cardImage.setPreserveRatio(true);
                dealerHandBox.getChildren().add(cardImage);
            }
        }
        updateScores();
    }

    private void updateScores() 
    {
        // Calculate player score and update label
        playerScore = calculateScore(playerHand);
        playerScoreLabel.setText("Player Score: " + playerScore);

        // Calculate dealer score
        int dealerScore = dealer.getScore();
        if (isDealerSecondCardHidden) 
        // Show only the score of the first visible card and hide the second
        {
            int visibleCardScore = calculateScore(List.of(dealer.getHand().get(0)));
            dealerScoreLabel.setText("Dealer Score: " + visibleCardScore + " + ?");
        }
        else 
        {
            dealerScoreLabel.setText("Dealer Score: " + dealerScore);
        }
    }

    private int calculateScore(List<Cards> hand) 
    {
        int total = 0;
        int aces = 0;

        for (Cards card : hand) 
        {
            String rank = card.getRank();
            switch (rank) 
            {
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

        while (total > 21 && aces > 0) 
        {
            total -= 10;
            aces--;
        }

        return total;
    }

    private void updateWinPercentage() 
    {
        if (roundsWon + roundsLost == 0) 
        {
            winPercentage = 0.0; // No games played, set win percentage to 0
        } 
        else 
        {
            winPercentage = (double) roundsWon / (roundsWon + roundsLost) * 100;
        }
    }
    
    private void endGame(String result) 
    {
        resultLabel.setText(result);
        playerScoreLabel.setText("Player Score: " + playerScore);
        dealerScoreLabel.setText("Dealer Score: " + dealer.getScore());
        
        // Update player's money based on the result
        if (result.contains("Player Wins")) 
        {
            updateWinScore();
        } 
        else if (result.contains("Dealer Wins")) 
        {
            updateLoseScore();
        } 
        else if (result.contains("Tie"))
        {
            roundsTied++;
        }

        updatePlayerMoneyDisplay();
        updateWinPercentage();


        PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(_ -> 
            {
                startBetting((Stage) resultLabel.getScene().getWindow());
            });
        delay.play();
    }

    
    private Scene createResultsScreen(Stage stage) 
    {
        VBox resultsLayout = new VBox(20);
        setBackground(resultsLayout, "/resources/menu_background.png");
        resultsLayout.setAlignment(Pos.CENTER);

        Label winLabel = new Label("Game Over!" + "\nRounds Won: " + roundsWon + "\nRounds Lost: " + roundsLost + "\nRounds Tied: " + roundsTied + "\nWin Percentage: " + String.format("%.2f", winPercentage) + "%" + "\n*Win Percentage excludes ties*");
        winLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px");
        Label moneyLabel = new Label("Final Money: $" + playerMoney.getBalance() + "\nMoney Won: $" + moneyWon + "\nMoney Lost: $" + moneyLost);
        moneyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px");
        Button quitButton = new Button("Quit");
        quitButton.setStyle("-fx-font-size: 20px");

        quitButton.setOnAction(_ -> stage.close());

        resultsLayout.getChildren().addAll(winLabel, moneyLabel, quitButton);
        resultsLayout.setAlignment(Pos.CENTER);

        return new Scene(resultsLayout, 800, 600);
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}