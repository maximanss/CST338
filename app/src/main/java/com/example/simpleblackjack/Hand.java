package com.example.simpleblackjack;

/****************************************************************
 * Hand
 * imported Hand class
 ***************************************************************/
class Hand
{
    // a hand can only have 52 cards maximum
    public static final int MAX_CARDS = 52;

    private Card[] myCards;
    private int numCards;

    // Constructor
    public Hand()
    {
        myCards = new Card[MAX_CARDS];
        numCards = 0;
    }

    /**
     * resets hand
     */
    public void resetHand()
    {
        numCards = 0;
    }

    /**
     * Adds a card to the next available position
     *
     * @param card
     * @return boolean
     */
    public boolean takeCard(Card card)
    {
        if (numCards < MAX_CARDS && card != null && !card.getErrorFlag())
        {
            Card tempCard = new Card(card);
            myCards[numCards] = tempCard;
            numCards++;
            return true;
        }
        return false;
    }

    // stringifies the hand
    public String toString()
    {
        String myHand = "";
        if (numCards > 0)
        {

            myHand += myCards[0];
            for (int i = 1; i < numCards; i++)
            {
                myHand += " , " + myCards[i];
            }

        }
        return "Hand = " + "(" + myHand + ")";
    }

    // Accessor
    public int getNumCards()
    {
        return numCards;
    }

    // Accessor
    public Card inspectCard(int k)
    {

        if (k >= 0 && k < numCards) // assume valid k starts from 0
        {
            Card aCard = new Card(myCards[k]); // prevent privacy leaks
            return aCard;
        }

        // return a dummy invalid card
        Card badCard = new Card('M', Card.Suit.SPADES);
        return badCard;

    }

    /**
     * Sort the cards in the hand in ascending order
     */
    public void sort()
    {
        Card.arraySort(myCards, numCards);
    }

    /**
     * Return a card on the top of the hand
     *
     * @return a good card if there is any
     */
    public Card playCard()
    {
        if (numCards > 0)
        {
            numCards--;
            return myCards[numCards];
        }

        // No more cards, return a card that does not work
        return new Card('M', Card.Suit.SPADES);

    }

    /**
     * Remove a card from the given location and slide down cards behind down one
     * spot
     *
     * @param cardIndex the location where the card is removed from the hand
     * @return the card at the given location or a bad card if location is
     *         invalid
     */
    public Card playCard(int cardIndex)
    {
        if (cardIndex < 0 || cardIndex >= numCards) // out of bound error
        {
            // Creates a card that does not work
            return new Card('M', Card.Suit.SPADES);
        }
        // Decreases numCards.
        Card card = myCards[cardIndex];
        numCards--;

        // Slide down cards that followed down one spot
        for (int i = cardIndex; i < numCards; i++)
        {
            myCards[i] = myCards[i + 1];
        }

        myCards[numCards] = null;

        return card;
    }

}