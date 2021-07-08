package com.example.simpleblackjack;

/******************************************************************************************
 * BlackJackController
 * Description: set up buttons and callbacks, manage the logic of the game and
 *      show the cards and status of the game
 *
 * @author Max Halbert
 * @version December 12, 2019
 *****************************************************************************************/

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import java.util.concurrent.TimeUnit;

public class BlackJackController {

    public static final int MAX_POINTS = 21; // maximum points for black jack game
    public static final int DEALER_MAX_HARD_POINTS = 17; // no cards for dealer with this or greater
    public static final int DEALER_MAX_SOFT_POINTS = 18; // no cards for dealer with this or greater

    private MainActivity theMain;
    private BlackJackView theView;
    private BlackJackModel theModel;

    // button Views
    private Button btnHitOrNew;
    private Button btnStandOrScore;
    private Button btnRules;

    // player status
    private boolean gamerTurn = true; // gamer starts first
    private boolean dealerTurn = false;

    /**
     * Constructor of BlackJackController
     * @param theMain
     * @param theModel
     * @param theView
     */
    public BlackJackController(MainActivity theMain, BlackJackModel theModel, BlackJackView theView) {
        this.theMain = theMain;
        this.theModel = theModel;
        this.theView = theView;

        btnHitOrNew = theMain.findViewById(R.id.btnHit);
        btnStandOrScore = theMain.findViewById(R.id.btnStand);
        btnRules = theMain.findViewById(R.id.btnR);

        // set Hit or New button OnClick listener and call back
        btnHitOrNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = btnHitOrNew.getText().toString();
                if (btnText.equals("HIT"))
                {
                    playerTakeCard(BlackJackModel.GAMER);
                    int total = calculateSoftTotal(BlackJackModel.GAMER);
                    if (total > MAX_POINTS)
                    {
                        // player is busted - exceed maximum points
                        // gamer loss, dealer win
                        showBustedMessage(BlackJackModel.GAMER, total);
                        updateScore(BlackJackModel.DEALER);
                        endGame();
                    }
                    else if (isNumCardsMaximum(BlackJackModel.GAMER))
                    {
                        // gamer reach the maximum number of cards
                        disableButtons();
                        dealerTurn = true;
                        gamerTurn = false;
                        dealerGame();
                    }
                }
                else if (btnText.equals("NEW GAME"))
                {
                    restartGame();  // starts a new game
                }

            }
        });

        // set Stand or Score button OnClick listener and call back
        btnStandOrScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = btnStandOrScore.getText().toString();
                if (btnText.equals("STAND"))
                {
                    // gamer is done, it is dealer's turn
                    disableButtons();
                    dealerTurn = true;
                    gamerTurn = false;

                    dealerGame();
                }
                else if (btnText.equals("SCORES"))
                {
                    showScores();   // show the winnings of dealer and gamer
                }

            }
        });

        // set Stand or Score button OnClick listener and call back
        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show rules of the game on a new screen
            showRules();
            }
        });

    }

    /**
     * Start the Black Jack game here
     */
    public void start()
    {
        // clear the cards
        theView.clearDealerViews();
        theView.clearGamerViews();
        theView.updateDealerStatus(dealerTurn);
        theView.updateGamerStatus(gamerTurn);

        // display the cards of player and dealer
        theView.showHand(BlackJackModel.GAMER, theModel.getHand(BlackJackModel.GAMER));
        theView.showHand(BlackJackModel.DEALER, theModel.getHand(BlackJackModel.DEALER));
        theView.showBackofCard(BlackJackModel.DEALER, 0); // first card of dealer not shown

        // check whether the gamer has Black Jack right in the beginning
        if (isBlackJack(BlackJackModel.GAMER))
        {
            // gamer got Black Jack
            showBlackJackMessage(BlackJackModel.GAMER);
            updateScore(BlackJackModel.GAMER);
            endGame();  //this round is over, dealer has no chance to play
        }

    }

    /**
     * Restart the game by reset the deck and the hands of players
     */
    private void restartGame()
    {

        btnHitOrNew.setVisibility(View.INVISIBLE);
        btnStandOrScore.setVisibility(View.INVISIBLE);
        btnHitOrNew.setText("HIT");
        btnStandOrScore.setText("STAND");
        btnHitOrNew.setVisibility(View.VISIBLE);
        btnStandOrScore.setVisibility(View.VISIBLE);

        theModel.startNewGame();   // reset the deck and hands
        dealerTurn = false;
        gamerTurn = true; // gamer always starts first
        start();    // start the game

    }

    private void showRules()
    {
        Intent ruleIntent = new Intent(theMain,com.example.simpleblackjack.RuleActivity.class );
        theMain.startActivity(ruleIntent);
    }

    /**
     * Dealer's turn to play the game
     */
    private void dealerGame()
    {
        disableButtons();
        dealerTurn = true;
        theView.updateDealerStatus(dealerTurn);
        theView.updateGamerStatus(gamerTurn);
        // display the first card in view
        Card card = theModel.getHand(BlackJackModel.DEALER).inspectCard(0);
        theView.displayCard(BlackJackModel.DEALER, 0, card);

        // check for Black Jack for the gamer
        if (isBlackJack(BlackJackModel.DEALER))
        {
            // gamer got Black Jack
            showBlackJackMessage(BlackJackModel.DEALER);
            updateScore(BlackJackModel.DEALER);
            endGame();
        }
        else
        {
            // check current points of gamer and dealer
            int gamerSoftTotal = calculateSoftTotal(BlackJackModel.GAMER);
            int gamerHardTotal = calculateHardTotal(BlackJackModel.GAMER);
            int gamerTotal;
            if ((gamerHardTotal <= MAX_POINTS) && (gamerHardTotal > gamerSoftTotal))
                gamerTotal = gamerHardTotal;
            else
                gamerTotal = gamerSoftTotal;

            int dealerSoftTotal = calculateSoftTotal(BlackJackModel.DEALER);
            int dealerHardTotal = calculateHardTotal(BlackJackModel.DEALER);
            int dealerTotal;
            if ((dealerHardTotal <= MAX_POINTS) && (dealerHardTotal > dealerSoftTotal))
                dealerTotal = dealerHardTotal;
            else
                dealerTotal = dealerSoftTotal;

            // Looop until dealer is done
            while ((dealerTotal < gamerTotal) &&
                    (!isNumCardsMaximum(BlackJackModel.DEALER)) &&
                    ((dealerSoftTotal < DEALER_MAX_SOFT_POINTS) || dealerHardTotal < DEALER_MAX_HARD_POINTS) )
            {
                playerTakeCard(BlackJackModel.DEALER);  // dealer can take a card

                // calculate the soft total, hard total of the dealer's hand
                dealerSoftTotal = calculateSoftTotal(BlackJackModel.DEALER);
                dealerHardTotal = calculateHardTotal(BlackJackModel.DEALER);
                if ((dealerHardTotal <= MAX_POINTS) && (dealerHardTotal > dealerSoftTotal))
                    dealerTotal = dealerHardTotal;
                else
                    dealerTotal = dealerSoftTotal;

                if (dealerSoftTotal > MAX_POINTS)
                {
                    // dealer is busted - exceed maximum points
                    // gamer wins, dealer loss, update the winnings of gamer
                    showBustedMessage(BlackJackModel.DEALER, dealerSoftTotal);
                    updateScore(BlackJackModel.GAMER);
                    endGame();
                    return; // this round of game is done
                }
            }

            // show who win the game and update the winnings of the player who won
            theView.displayWinner(dealerTotal, gamerTotal);
            if (dealerTotal > gamerTotal)
            {
                updateScore(BlackJackModel.DEALER);
            }
            else if (dealerTotal < gamerTotal)
            {
                updateScore(BlackJackModel.GAMER);
            }
            endGame();
        }
    }


    /**
     * Check whether number of cards of a player reach the maximum
     * @param who
     * @return
     */
    private boolean isNumCardsMaximum(int who)
    {
        if (theModel.getNumCardsInHand(who) >= BlackJackModel.MAX_CARDS)
            return true;
        else
            return false;

    }

    /**
     * disabled the buttons of the gamer
     */
    private void disableButtons()
    {
        btnHitOrNew = theMain.findViewById(R.id.btnHit);
        btnStandOrScore = theMain.findViewById(R.id.btnStand);
        btnHitOrNew.setVisibility(View.INVISIBLE);
        btnStandOrScore.setVisibility(View.INVISIBLE);

    }

    /**
     * This round of game is over, change the labels of the buttons to
     * "NEW GAME" and "SCORES"
     */
    private void endGame()
    {
        // update dealer and gamer status for the game is over
        gamerTurn = false;
        dealerTurn = false;
        theView.updateDealerStatus(dealerTurn);
        theView.updateGamerStatus(gamerTurn);

        // change the labels on the buttons
        btnHitOrNew = theMain.findViewById(R.id.btnHit);
        btnStandOrScore = theMain.findViewById(R.id.btnStand);
        btnHitOrNew.setVisibility(View.INVISIBLE);
        btnStandOrScore.setVisibility(View.INVISIBLE);
        btnHitOrNew.setText("NEW GAME");
        btnStandOrScore.setText("SCORES");
        btnHitOrNew.setVisibility(View.VISIBLE);
        btnStandOrScore.setVisibility(View.VISIBLE);
    }

    /**
     * Display busted message of the player and the points in player's hand
     * @param who
     * @param points
     */
    private void showBustedMessage(int who, int points)
    {
        theView.displayBustedMessage(who, points);
    }

    /**
     * Display Black Jack winning message of the player
     * @param who
     *
     */
    private void showBlackJackMessage(int who)
    {
        theView.displayBlackJackMessage(who);
    }

    /**
     * Increment number of scores of a player
     * @param who
     */
    private void updateScore(int who)
    {
        theModel.addWinnings(who);
    }

    /**
     * deal a card to the player and show the card
     * @param who
     */
    private void playerTakeCard(int who)
    {
        // get a card from the deck and place it in the hand
        if (theModel.takeCard(who))
        {
            // display the card in view
            int cardIndex = theModel.getHand(who).getNumCards() - 1;
            Card card = theModel.getHand(who).inspectCard(cardIndex);
            theView.displayCard(who, cardIndex, card);
        }

    }

    /**
     * Show the winnings of dealer and gamer
     */
    private void showScores()
    {
        theView.displayScores(theModel.getWinnings(BlackJackModel.DEALER),
                theModel.getWinnings(BlackJackModel.GAMER));
    }

    /**
     * Check whether the player has Black Jack hand
     * @param who
     * @return
     */
    private boolean isBlackJack(int who)
    {
        // examine the first two cards of the hand
        Hand hand = theModel.getHand(who);
        Card card1 = hand.inspectCard(0);
        Card card2 = hand.inspectCard(1);

        // check the cards has an Ace and a ten-pointer
        if ((card1.getValue() == 'A' && Card.valueAsInt(card2) >= 9) ||
                (card2.getValue() == 'A' && Card.valueAsInt(card1) >= 9) )
        {
            // the hand has Black Jack, which is Ace with a 10-pointer card
            return true;
        }
        return false;
    }

    /**
     * Calculate the soft-total of a player's hand where 'Ace' is 1 point
     * @param who
     * @return
     */
    private int calculateSoftTotal(int who)
    {
        int softTotal = 0;
        Hand hand = theModel.getHand(who);
        for (int k=0; k < hand.getNumCards(); k++)
        {
            Card card = hand.inspectCard(k);
            int points = Card.valueAsInt(card) + 1; // add 1 to get true value
            if (points > 10)
                points = 10;    // J, Q and K only 10 points
            softTotal += points;
        }
        return softTotal;
    }

    /**
     * Calculate the hard-total of a player's hand where first 'Ace' is 11 points
     * @param who
     * @return
     */
    private int calculateHardTotal(int who)
    {
        int hardTotal = 0;
        boolean hasAce = false;
        Hand hand = theModel.getHand(who);
        for (int k=0; k < hand.getNumCards(); k++)
        {
            Card card = hand.inspectCard(k);
            int points = Card.valueAsInt(card) + 1; // add 1 for true value
            if (points > 10)
                points = 10;    // J, Q and K only 10 points
            if (points == 1)
                hasAce = true;  // there is an Ace in hand
            hardTotal += points;
        }
        if (hasAce)
        {
            // one or more Ace in hand, add 10 points to the hand
            hardTotal += 10;
        }
        return hardTotal;
    }
}
