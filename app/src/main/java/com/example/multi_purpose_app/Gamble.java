package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Gamble extends AppCompatActivity implements View.OnClickListener {

    // Buttons
    LinearLayout btnDice;
    LinearLayout btnCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamble);

        // OnClickListener für Buttons
        btnDice = (LinearLayout) findViewById(R.id.btnDice);
        btnDice.setOnClickListener(this);

        btnCoin = (LinearLayout) findViewById(R.id.btnCoin);
        btnCoin.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        if (v.getId() == R.id.btnDice) {
            Intent intentDice = new Intent(this, Dice.class);
            startActivity(intentDice);
            this.finish();
        }
        else if (v.getId() == R.id.btnCoin) {
            Intent intentCoin = new Intent(this, Coin.class);
            startActivity(intentCoin);
            this.finish();
        }
    }
}