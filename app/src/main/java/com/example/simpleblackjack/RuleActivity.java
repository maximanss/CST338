package com.example.simpleblackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * RuleActivity
 * Description: It is the second Activity object created from the MainActivity,
 * it displayed the screen of rules for the game.
 *
 * @Author Max Halbert
 * @version December 12 2019
 */
public class RuleActivity extends AppCompatActivity {

    public Button rules;
    public Button result;
    public Button dealer;
    public Button gamer;
    public Button done;
    public TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        // Set Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        rules = findViewById(R.id.btnRules);
        result = findViewById(R.id.btnResult);
        dealer = findViewById(R.id.btnDealer);
        gamer = findViewById(R.id.btnGamer);
        done = findViewById(R.id.done);

        tvResult = findViewById(R.id.tvResult);
        tvResult.setVisibility(View.GONE);

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(R.string.ruleContent);
            }

        });

        dealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(R.string.dealerDescription);
            }

        });

        gamer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(R.string.gamerDescription);
            }

        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(R.string.condition);
            }

        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvResult.setText(R.string.doneWith);
                RuleActivity.this.finish();
            }

        });

        tvResult.setVisibility(View.VISIBLE);
    }
}
