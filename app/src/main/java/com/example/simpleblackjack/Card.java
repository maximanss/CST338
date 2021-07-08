package com.example.simpleblackjack;


import android.widget.Toast;
/****************************************************************
 * Card
 * imported Card class
 * Modified by Max Halbert
 * @version December 12, 2019
 ***************************************************************/
class Card
{
    public enum Suit
    {
        CLUBS, DIAMONDS, HEARTS, SPADES
    };

    // card values and suit
    private char value;
    private Suit suit;
    public static char[] valuRanks =
            { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X' };
    public static char[] suitChar =
            { 'C', 'D', 'H', 'S'};

    // Checks for illegal card data
    private boolean errorFlag;

    /**
     * Constructor
     *
     * @param value
     * @param suit
     */
    public Card(char value, Suit suit)
    {
        set(value, suit);
    }

    /**
     * Overloaded constructor
     */
    public Card()
    {
        this('A', Suit.SPADES);
    }

    /**
     * Copy Constructor
     *
     * @param otherCard
     */
    public Card(Card otherCard)
    {
        if (otherCard == null)
        {
            // since otherCard is null, create invalid card
            set('M', Suit.SPADES);
        }
        set(otherCard.value, otherCard.suit);
    }

    // Accessors
    public Suit getSuit()
    {
        return suit;
    }

    public char getValue()
    {
        return value;
    }

    public boolean getErrorFlag()
    {
        return errorFlag;
    }

    // Mutators
    public boolean set(char value, Suit suit)
    {
        if (isValid(value, suit))
        {
            this.value = value;
            this.suit = suit;
            errorFlag = false;
            return true;
        }
        errorFlag = true;
        return false;
    }

    // Valid card data returned
    public String toString()
    {
        if (errorFlag)
            return "[Card Not Valid]";
        return value + " of " + suit;

    }

    // Returns true if all field members are identical, false otherwise
    public boolean equals(Card card)
    {
        if (card == null)
            return false;

        // comparing member values
        if (this.value == card.value)
        {
            if (this.suit == card.suit)
                return true;
        }
        return false;
    }

    // Determine validity for the value
    private boolean isValid(char value, Suit suit)
    {
        // checks if value is in the field of valid values
        for (int i = 0; i < valuRanks.length; ++i)
        {
            if (value == valuRanks[i])
                return true;
        }
        // invalid value
        return false;
    }

    public static void arraySort(Card[] cArray, int arraySize)
    {
        for (int i = 0; i < arraySize - 1; i++)
        {
            for (int j = 0; j < arraySize - i - 1; j++)
            {
                if (cardAsInt(cArray[j]) > cardAsInt(cArray[j + 1]))
                {
                    Card temp = cArray[j];
                    cArray[j] = cArray[j + 1];
                    cArray[j + 1] = temp;
                }
            }
        }
    }

    /**
     * It returns an integer according to the value
     *
     * @param card
     * @return -1 if the value is not valid
     */
    public static int valueAsInt(Card card)
    {
        for (int i = 0; i < valuRanks.length; i++)
        {
            if (valuRanks[i] == card.getValue())
            {
                return i;
            }
        }
        // Not matching any values in the ranks
        return -1;
    }

    /**
     * It returns the suit position as an integer
     *
     * @param card
     * @return an integer
     */
    public static int suitAsInt(Card card)
    {
        return card.getSuit().ordinal();
    }

    /**
     * It returns a specific integer for a card according to the value and the
     * suit
     *
     * @param card
     * @return an integer for the card
     */
    public static int cardAsInt(Card card)
    {
        int total = Card.valueAsInt(card) * 4 + Card.suitAsInt(card);
        return total;
    }

    /**
     * returns the suit in character at the given index
     * @param index
     * @return
     */
    public static char suitAsChar(int index)
    {
        return suitChar[index];
    }

    /**
     * returns the value in a character at the given index
     * @param index
     * @return
     */
    public static char valueAsChar(int index)
    {
        return valuRanks[index];
    }

}
