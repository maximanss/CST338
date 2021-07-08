package com.example.simpleblackjack;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * MainActivity
 * Description: It is the main object created when the app is executed,
 * it creates the objects of BlackJackModel, BlackJackView and BlackJackController.
 * The main screen of the game is displayed.
 *
 * @Author: Max Halbert
 * @version: December 12, 2019
 *
 */
public class MainActivity extends AppCompatActivity {

    public BlackJackModel theModel;
    public BlackJackView theView;
    public BlackJackController theController;

    /**
     * creates the BlackJackModel, BlackJackView and BlackJackController objects, and
     * start and display the main screen of the game.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        // Prepare the game
        theModel = new BlackJackModel();

        theView = new BlackJackView(this);

        theController = new BlackJackController(this, this.theModel, this.theView);

        theController.start();  // start the game


    }
}
