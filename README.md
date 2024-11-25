# Blackjack

Team Gambit 

Members: Cole Smolinski

I am building a simple game of blackjack that will:
-  Use a full deck of 52 cards to deal to both the player and the "dealer."
-  Give the player the option to "hit" or "stand" each round.
-  Have the player and/or "dealer" lose the round or "bust" if the value of their hand is over 21.
-  Keep track of the cards in the deck and shuffle them when it runs out.
-  Keep track of the player's money and not allow them to bet more than they have.
-  Update the player's money based on the round's outcome.
-  Use traditional rules that dictate that if the dealer has a score of 16 or lower, they must hit, and if they have 17 or over, they must stand.
-  Allow the player to "cash out" at the end and display the amount of money they won and lost, as well as number of hands they won, lost, and tied, with a win percentage.
-  Have a GUI to display all this information to the user.

I want to build this project because I would ideally like to be a game designer in the future, so making a more simplistic game like blackjack and creating a GUI for it will be great experience for me going into the future.

To me, this project is useful because, as I mentioned before, I want to make games, but also I just want to create a fun game that anybody would be able to pick up and play if they want.

As for how it will be used, I want to create an application that will run using a list of assets such as visuals and sound effects to create a more game-like experience.

Here are my updated class diagrams (as of 11-24-2024):

[Uploading UclassDiagram
    BlackjackGame <|-- Deck : contains
    BlackjackGame <|-- Money : uses
    BlackjackGame <|-- Dealer : play against
    Deck <|-- Cards : uses
    BlackjackGame : +Deck deck
    BlackjackGame : +Dealer dealer
    BlackjackGame : +Money playerMoney
    BlackjackGame : +arrayList playerHand
    BlackjackGame : +int playerScore
    BlackjackGame : +int currentBet
    BlackjackGame : +int roundsWon
    BlackjackGame : +int roundsLost
    BlackjackGame : +int roundsTied
    BlackjackGame : +int moneyWon
    BlackjackGame : +int moneyLost
    BlackjackGame : +double winPercentage
    BlackjackGame : +boolean isDealerSecondCardHidden
    BlackjackGame: +start()
    BlackjackGame: +setBackground()
    BlackjackGame: +updatePlayerMoneyDisplay()
    BlackjackGame: +updateCurrentBetDisplay()
    BlackjackGame: +updatePlayerHandUI()
    BlackjackGame: +updateDealerHandUI()
    BlackjackGame: +updateWinScore()
    BlackjackGame: +updateLoseScore()
    BlackjackGame: +updateScores()
    BlackjackGame: +cashOut()
    BlackjackGame: +createMainMenu()
    BlackjackGame: +createBettingScene()
    BlackjackGame: +createGameScene()
    BlackjackGame: +createResultsScreen()
    BlackjackGame: +startBetting()
    BlackjackGame: +startNewGame()
    BlackjackGame: +handleHit()
    BlackjackGame: +handleStand()
    BlackjackGame: +calculateScores()
    BlackjackGame: +endGame()
    class Deck{
      +arrayList deck
      +shuffle()
      +dealCard()
      +getSize()
    }
    class Cards{
      +string suits
      +string ranks
      +getSuit()
      +getRank()
      +toString()
    }
    class Money{
      +int balance
      +int bet
      +Money()
      +winMoney()
      +loseMoney()
      +placeBet()
      +resetBet()
      +canBet()
      +getBalance()
    }
    class Dealer{
      +arrayList hand
      +int score
      +addCard()
      +shouldHit()
      +calculateScore()
      +getScore()
      +getHand()
    }
    pdated UML Diagram (11-25-2024).mmd…]()


This diagram is just my starting draft and certain methods and processes may be subject to move and/or change over time.

In terms of my plan, I plan on having a lot of the basic functionality of the game to be done and working within the first week or so of working on the project, then focusing on creating the GUI and implementing it over the next two weeks. Then, the last week will be focused mostly on debugging and testing all the features to make sure everything is working together properly.

As for the estimate of effort, I have obviously chosen to work on this project on my own, so I will be the one putting in all the effort to create this project. I could definitely see a situation where I am getting a little ahead of myself with this project and it will end up being more difficult than I initially thought, but I want to try and push myself a bit to see what I can accomplish.
