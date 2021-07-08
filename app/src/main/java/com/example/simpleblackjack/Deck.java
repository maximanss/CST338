package com.example.simpleblackjack;

/****************************************************************
 * Deck import
 * imported Deck class
 ***************************************************************/
import java.util.Random;

class Deck
{
    // playing card pack values
    public static final int MAX_CARDS = 312; // 6 packs x 52 cards
    public static final int ONE_PACK = 52; // standard 52 cards
    public static boolean beenHereBefore = false;

    // creates a new object with one pack of cards
    private static Card[] masterPack = new Card[ONE_PACK];

    private Card[] cards; // cards in the deck
    private int topCard; // keep track of number of cards in the deck
    private int numPacks; // keep track of number of packs in the deck

    /**
     * Constructor that takes in a number of packs as an argument and then
     * creates a deck of cards with that many packs of cards (56 x numPacks)
     *
     * @param numPacks
     */
    public Deck(int numPacks)
    {
        // maximum packs is 6
        if ((numPacks * ONE_PACK) > MAX_CARDS)
            numPacks = 6;

        this.numPacks = numPacks;

        allocateMasterPack();

        // create the cards array with (52+4) x numPacks cards
        // standard is 52 cards but possible add 4 jokers per pack
        cards = new Card[numPacks * (ONE_PACK + 4)];

        // populate the cards in the deck
        init(numPacks);
    }

    /*
     * Overloaded no argument constructor that creates a pack of cards using just
     * one deck
     */
    public Deck()
    {
        this.numPacks = 1;

        allocateMasterPack();

        // Create the cards array using one pack of cards
        cards = new Card[ONE_PACK + 4]; // add 4 joker spaces

        // Initialize the last index of the array to be the top card of the deck
        topCard = ONE_PACK; // no jokers that will be added if needed

        // Loop through the cards array, populating it with Cards
        for (int i = 0; i < topCard; i++)
        {
            // Create a new Card Object, copying it from the masterPack
            cards[i] = new Card(masterPack[i]);
        }
    }

    /**
     * Method to re-populate cards[] with 52 x numPacks cards.
     *
     * @param numPacks
     */
    public void init(int numPacks)
    {

        // Initialize the last index of the array to be the top card of the deck
        topCard = numPacks * ONE_PACK; // no jokers that can be added when needed

        this.numPacks = numPacks;

        // Populate the card array with Card objects, copying values from
        // masterPack
        for (int masterCounter = 0, i = 0; i < topCard; i++, masterCounter++)
        {
            // Create a new Card Object, copying it from the masterPack
            cards[i] = new Card(masterPack[masterCounter]);

            // If the cards array is more than one pack, reset the index of
            // masterPack
            // in order to loop through it again
            if (masterCounter == ONE_PACK - 1)
                masterCounter = -1;
        }
    }

    /**
     * Shuffles the deck of Cards
     */
    public void shuffle()
    {
        Random shuffle = new Random();
        Card tempCard;
        int randCard;

        // loops through the entire deck
        for (int x = 0; x < topCard; x++)
        {
            // Picks a random card from the deck
            randCard = shuffle.nextInt(ONE_PACK);
            // assigns the random card to a placeholder
            tempCard = cards[randCard];
            // assigns the random card to the next card in the deck
            cards[randCard] = cards[x];
            // assigns the next card in the deck to the card in
            // the place holder
            cards[x] = tempCard;
        }
    }

    /**
     * Deals a card by taking the top of the deck and makes sure there are still
     * cards available.
     *
     * @return the top Card from the deck.
     */
    public Card dealCard()
    {
        Card dealCard;

        // checks if there are cards in the deck
        if (topCard > 0)
        {
            // assigns the top card to the dealCard variable
            dealCard = inspectCard(topCard - 1);

            // removes the topcard from the deck
            cards[topCard - 1] = null;

            // decreases card count
            topCard--;
            return dealCard;
        }
        // returns null if no more cards
        return null;
    }

    /**
     * Returns the number of cards in a deck.
     *
     * @return the number of cards in the deck
     */
    public int getTopCard()
    {
        return topCard;
    }

    /**
     * Accessor for an individual card. Returns a card or returns a card with an
     * error flag.
     *
     * @return the card at index k
     * @return a card with with an error flag
     */
    public Card inspectCard(int k)
    {
        Card returnCard;

        // If k is out of bounds, return a card with an error flag
        if (k < 0 || k >= topCard)
        {
            // Create an invalid card with errorFlag = true
            returnCard = new Card('E', Card.Suit.CLUBS);
        }
        else
        {
            // Otherwise return the card at k index
            returnCard = cards[k];
        }
        return returnCard;
    }

    /**
     * This method creates new cards and fills the masterPack.
     */
    private static void allocateMasterPack()
    {
        // Check if this method has already been run. Return if it has.
        if (beenHereBefore)
            return;

        char[] value =
                { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K' };

        Card.Suit[] suits =
                { Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.HEARTS,
                        Card.Suit.SPADES };

        int curIndex = 0;

        // Loop through the value array
        for (int x = 0; x < value.length; x++)
        {
            // Loop through the suits array
            for (int y = 0; y < suits.length; y++)
            {
                // Create a new Card object with the correct suit and value
                masterPack[curIndex] = new Card(value[x], suits[y]);
                curIndex++;
            }
        }

        beenHereBefore = true;
    }

    /**
     * Adds the card to the top of the deck if there aren't too many instances
     *
     * @param card the card to be added
     * @return false if the card is already there or no rooms, otherwise return
     *         true
     */
    public boolean addCard(Card card)
    {
        if (cards.length == topCard)
        {
            return false; // no room for the card
        }

        int ctr = 0; // counter for the instances of the card
        for (int i = 0; i < topCard; i++)
        {
            if (cards[i].equals(card))
            {
                ctr++;
            }
        }

        if (ctr < numPacks)
        {
            // number of instances not exceeding the numPacks
            cards[topCard] = card; // add the card to the deck
            topCard++;
            return true;
        }
        return false;
    }

    /**
     * Remove one instance of a specific card from the deck
     *
     * @param card the specific card
     * @return true if success, otherwise false
     */
    public boolean removeCard(Card card)
    {

        // remove only one instance of the card
        for (int i = 0; i < topCard; i++)
        {
            if (cards[i].equals(card))
            {
                cards[i] = cards[topCard - 1];
                cards[topCard - 1] = null;
                topCard--;
                return true;
            }
        }
        return false;
    }

    public void sort()
    {
        Card.arraySort(cards, topCard);
    }

    public int getNumCards()
    {
        return topCard;
    }
}