package com.example.simpleblackjack;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

//import org.w3c.dom.Text;

/****************************************************************
 * BlackJackView
 *
 * Description: Updates the images of the game
 *
 * Imported and Modified by Max Halbert
 *
 * @version December 12, 2019
 *
 *****************************************************************/

class BlackJackView
{

    private MainActivity theMain;

    // textviews of dealer and gamer cards
    private TextView cardViews[][] = new TextView[BlackJackModel.MAX_PLAYERS][BlackJackModel.MAX_CARDS];

    // images of cards only loaded once
    public static boolean imagesLoaded = false;
    public static Drawable cardImages[][] = new Drawable[13][4];
    public static Drawable backImage;


    /**
     * Constructor that takes the number of cards per hand
     *
     * @param theMain
     */
    public BlackJackView(MainActivity theMain)
    {
        this.theMain = theMain;
        this.loadCardImage();
        this.loadCardView();
    }

    /**
     * Load the card image from the drawables
     */
    private void loadCardImage()
    {
        // Only load the images of all the cards once
        if (this.imagesLoaded)
            return;

        // generates file names of icon and adds to Icon's array
        String filename = "";
        for (int i = 0; i < 13; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                String imageName = "" + Card.suitAsChar(j) + Card.valueAsChar(i);
                imageName = imageName.toLowerCase();
                int imgID = this.theMain.getResources().getIdentifier( imageName, "drawable", this.theMain.getPackageName() );
                Drawable image = this.theMain.getResources().getDrawable(imgID);
                image.setBounds( 0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight() );
                this.cardImages[i][j] = image;
            }
        }
        // get the image of the back of a card
        int imgID = this.theMain.getResources().getIdentifier( "bk", "drawable", this.theMain.getPackageName() );
        Drawable image = this.theMain.getResources().getDrawable(imgID);
        image.setBounds( 0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight() );
        this.backImage = image;
        this.imagesLoaded = true;

    }

    /**
     * Find all the textview of the cards for both dealer and gamer
     */
    private void loadCardView()
    {
        // find all the card views of dealer
        int cardIndex = 0;
        cardViews[BlackJackModel.DEALER][cardIndex++] = theMain.findViewById(R.id.tvD1);
        cardViews[BlackJackModel.DEALER][cardIndex++] = theMain.findViewById(R.id.tvD2);
        cardViews[BlackJackModel.DEALER][cardIndex++] = theMain.findViewById(R.id.tvD3);
        cardViews[BlackJackModel.DEALER][cardIndex++] = theMain.findViewById(R.id.tvD4);
        cardViews[BlackJackModel.DEALER][cardIndex++] = theMain.findViewById(R.id.tvD5);
        cardViews[BlackJackModel.DEALER][cardIndex++] = theMain.findViewById(R.id.tvD6);

        // find all the card views of player
        cardIndex = 0;
        cardViews[BlackJackModel.GAMER][cardIndex++] = theMain.findViewById(R.id.tvP1);
        cardViews[BlackJackModel.GAMER][cardIndex++] = theMain.findViewById(R.id.tvP2);
        cardViews[BlackJackModel.GAMER][cardIndex++] = theMain.findViewById(R.id.tvP3);
        cardViews[BlackJackModel.GAMER][cardIndex++] = theMain.findViewById(R.id.tvP4);
        cardViews[BlackJackModel.GAMER][cardIndex++] = theMain.findViewById(R.id.tvP5);
        cardViews[BlackJackModel.GAMER][cardIndex++] = theMain.findViewById(R.id.tvP6);

    }

    /**
     * Display the status of the dealer
     * @param dealerTurn
     */
    public void updateDealerStatus(boolean dealerTurn)
    {
        TextView dealer = theMain.findViewById(R.id.tvDealer);
        dealer.setVisibility(View.INVISIBLE);
        if (dealerTurn) {
            dealer.setText("Dealer Turn");
        } else {
            dealer.setText("Dealer");
        }
        dealer.setVisibility(View.VISIBLE);
    }

    /**
     * Display the status of the gamer
     * @param playerTurn
     */
    public void updateGamerStatus(boolean playerTurn)
    {
        TextView player = theMain.findViewById(R.id.tvPlayer);
        player.setVisibility(View.INVISIBLE);
        if (playerTurn) {
            player.setText("Your Turn");
        } else {
            player.setText("You");
        }
        player.setVisibility(View.VISIBLE);
    }

    /**
     * Display the image of the card
     * @param who
     * @param viewIndex
     * @param card
     */
    public void displayCard(int who, int viewIndex, Card card)
    {
        // get the image of the card
        int value = Card.valueAsInt(card);
        int suit = Card.suitAsInt(card);
        Drawable image = cardImages[value][suit];

        // display the image on the selected view
        TextView tv = cardViews[who][viewIndex];
        //tv.setVisibility(View.INVISIBLE);
        tv.setCompoundDrawables(null, image, null, null);
        tv.setVisibility(View.VISIBLE);
    }

    /**
     * Display the back of card image
     * @param who
     * @param viewIndex
     */

    public void showBackofCard(int who, int viewIndex)
    {
        // get the image of the back of card
        Drawable image = backImage;

        // display the image on the selected view
        TextView tv = cardViews[who][viewIndex];
        tv.setVisibility(View.INVISIBLE);
        tv.setCompoundDrawables(null, image, null, null);
        tv.setVisibility(View.VISIBLE);
    }

    /**
     * Display the image of the cards in the given hand
     * @param who
     * @param hand
     */
    public void showHand(int who, Hand hand)
    {
        for (int i=0; i < hand.getNumCards(); i++)
        {
            Card card = hand.inspectCard(i);

            displayCard(who, i, card);
        }
    }

    /**
     * clears all the card views of dealer's hand
     */
    public void clearDealerViews()
    {
        for (int i=0; i < BlackJackModel.MAX_CARDS; i++)
        {
            TextView tv = cardViews[BlackJackModel.DEALER][i];
            tv.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * clears all the card views of gamer's hand
     */
    public void clearGamerViews()
    {
        for (int i=0; i < BlackJackModel.MAX_CARDS; i++)
        {
            TextView tv = cardViews[BlackJackModel.GAMER][i];
            tv.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * display a busted message
     * @param player
     * @param points
     */
    public void displayBustedMessage(int player, int points)
    {
        String title;
        String message;
        if (player == BlackJackModel.DEALER)
        {
            title = "Dealer BUSTED! You Won!";
            message = "Dealer is busted with " + points + " points";
        }
        else
        {
            title = "You BUSTED! Dealer Won!";
            message = "You are busted with " + points + " points";
        }

        displayAlertMessage(title, message);

    }

    /**
     * display a Black Jack message
     * @param player
     */
    public void displayBlackJackMessage(int player)
    {
        String title;
        String message;
        if (player == BlackJackModel.DEALER)
        {
            title = "BLACK JACK!";
            message = "Dealer got Black Jack and Won!" ;
        }
        else
        {
            title = "BLACK JACK!";
            message = "You got Black Jack and Won!";
        }

        displayAlertMessage(title, message);

    }


    /**
     * display a message to show who won the game
     *
     * @param compScore
     * @param humanScore
     */
    public void displayWinner(int compScore, int humanScore)
    {
        String title = "";
        String message = "";
        if (compScore > humanScore)
        {
            title = "Dealer won!";
            message = "" + compScore + " vs " + humanScore;
        }
        else if (compScore < humanScore)
        {
            title = "You Won!";
            message = "" + humanScore + " vs "+ compScore;
        }
        else
        {
            title = "Tied Game!";
            message = "You and Dealer are tied with " + compScore + " points";
        }
        displayAlertMessage(title, message);
    }

    /**
     * display the winnings of gamer and dealer
     * @param compWins
     * @param humanWins
     */
    public void displayScores(int compWins, int humanWins)
    {
        String title = "Scores";
        String message = "Dealer won " + compWins + " times\n" +
                "You won " + humanWins + " times";
        displayAlertMessage(title, message);
    }

    /**
     * display the message as a pop up alert style
     * @param title
     * @param message
     */
    private void displayAlertMessage(String title, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(theMain);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}




