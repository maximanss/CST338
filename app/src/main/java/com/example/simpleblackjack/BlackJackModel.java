package com.example.simpleblackjack;

/****************************************************************
 * BlackJackModel
 *
 * Description: Creates the model for a game called "Simple Black Jack"
 * Usage: Manages the data of the card game
 *
 * Imported and Modified by Max Halbert
 * @version December 12, 2019
 *****************************************************************/

class BlackJackModel
{
    public static final int MAX_PLAYERS = 2; //only dealer and a player
    public static final int MAX_CARDS = 6; // maximun number of cards in a heand
    public static final int GAMER = 1;
    public static final int DEALER = 0;

    private int numPlayers;
    private static final int NUM_PACK = 1; // # standard 52-card packs per deck
    private static final int NUM_CARDS_START = 2; // number of cards each hand at start

    private Deck deck; // holds the initial full deck and gets
    private boolean deckExhausted = false;

    // one hand for each player
    private Hand[] hand; // one Hand for each player

    // Variables to keep track of winnings
    private int[] winnings = new int[MAX_PLAYERS];


    /**
     * override default Constructor
     */
    public BlackJackModel()
    {
        // only one pack, no jokers, no unused cards
        // only two players and each starts with two cards start with
        numPlayers = MAX_PLAYERS;

        hand = new Hand[MAX_PLAYERS];
        for (int k = 0; k < numPlayers; k++)
            hand[k] = new Hand();

        deck = new Deck(1); // create a deck of cards

        // reset the winnings of each player
        for (int k=0; k < MAX_PLAYERS; k++)
            winnings[k] = 0;

        startNewGame();
    }

    /**
     * reset the deck and hands, deals cards to each hand
     */
    public void startNewGame()
    {
        initGame();
        dealToHand(); // deal cards to hands
    }

    /**
     * start a new deck and reset hands. Shuffle the deck.
     */
    private void initGame()
    {
        int k;

        // clear the hands
        for (k = 0; k < MAX_PLAYERS; k++)
            hand[k].resetHand();

        // restock the deck
        deck.init(NUM_PACK);

        // shuffle the cards
        deck.shuffle();

        deckExhausted = false;
    }

    /**
     * deal the specified number of cards to each hand
     *
     * @return true if successful, false if not enough cards
     */
    public boolean dealToHand()
    {
        // returns false if not enough cards, but deals what it can
        boolean enoughCards = true;

        // clear all hands
        for (int j = 0; j < MAX_PLAYERS; j++)
            hand[j].resetHand();

        enoughCards = true;
        for (int k = 0; k < NUM_CARDS_START && enoughCards; k++)
        {
            for (int j = 0; j < MAX_PLAYERS; j++)
                if (deck.getNumCards() > 0)
                    hand[j].takeCard(deck.dealCard());
                else
                {
                    enoughCards = false;
                    deckExhausted = true;
                    break;
                }
        }
        return enoughCards;
    }

    /**
     * deal a card from the deck to the specified player
     *
     * @param playerIndex the specified player
     * @return true if successful, false if deck is empty
     */
    public boolean takeCard(int playerIndex)
    {
        // returns false if either argument is bad
        if (playerIndex < 0 || playerIndex > numPlayers - 1)
            return false;

        // Are there enough Cards?
        if (deck.getNumCards() <= 0)
        {
            // deck is empty, set the flag
            deckExhausted = true;
            return false;
        }
        // deck is not empty, deal a card to player
        return hand[playerIndex].takeCard(deck.dealCard());
    }

   /**
     * Sort the hands of each player
     */
    public void sortHands()
    {
        int k;

        for (k = 0; k < numPlayers; k++)
            hand[k].sort();
    }

    /**
     * Sorts the specified player's hand
     *
     * @param playerIndex the specified player
     */
    public void sortHand(int playerIndex)
    {
        getHand(playerIndex).sort();
    }

    /**
     * Returns the number of cards in a players hand
     *
     * @param playerIndex the specified player
     * @return the number of cards
     */
    public int getNumCardsInHand(int playerIndex)
    {
        return getHand(playerIndex).getNumCards();
    }

    /**
     * Return the hand of the specified player
     *
     * @param k the specified player
     * @return the hand of the player
     */
    public Hand getHand(int k)
    {
        // hands start from 0 like arrays

        // on error return automatic empty hand
        if (k < 0 || k >= numPlayers)
            return new Hand();

        return hand[k];
    }

    /**
     * Return winnings of each player
     * @param k the index of a player
     * @return -1 if invalid player index
     */
    public int getWinnings(int k)
    {
        return winnings[k];
    }

    /**
     * Add s to the winnings of a player
     * @param k index to the player
     */
    public void addWinnings(int k)
    {
        winnings[k] += 1;
    }



    /**
     * Get a card from the deck
     *
     * @return the card if deck is not empty
     */
    public Card getCardFromDeck()
    {
        if (deck.getNumCards() == 0)
        {
            // set the flag to indicate deck is empty
            deckExhausted = true;
        }

        return deck.dealCard();
    }

    /**
     * Return the number of cards left in the deck
     */
    public int getNumCardsRemainingInDeck()
    {
        return deck.getNumCards();
    }


}
