package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class Dice extends AppCompatActivity implements View.OnClickListener {

    // Startbutton
    Button btnStart;
    // Bild Würfel
    ImageView changeDice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        // Button nach ID festlegen
        btnStart = (Button) findViewById(R.id.btnStart);
        // OnClickListener für Startbutton
        btnStart.setOnClickListener(this);

        // View für Münze nach ID festlegen
        changeDice = (ImageView) findViewById(R.id.iVDice);
    }

    @Override
    public void onClick(View v) {
        rollDice();
    }

    private void rollDice() {
        // neue Instanz der Klasse Random
        Random rand = new Random();
        int max = 6;
        // Zufallswert von 1-6
        int pip = rand.nextInt(max) + 1;

        switch (pip) {
            // Zeige 1
            case 1:
                changeDice.setImageResource(R.drawable.ic_gamble);
                break;
            // Zeige 2
            case 2:
                changeDice.setImageResource(R.drawable.ic_gamble);
                break;
            // Zeige 3
            case 3:
                changeDice.setImageResource(R.drawable.ic_gamble);
                break;
            // Zeige 4
            case 4:
                changeDice.setImageResource(R.drawable.ic_gamble);
                break;
            // Zeige 5
            case 5:
                changeDice.setImageResource(R.drawable.ic_gamble);
                break;
            // Zeige 6
            case 6:
                changeDice.setImageResource(R.drawable.ic_gamble);
                break;
            // Zeige Fehler
            default:
                changeDice.setImageResource(R.drawable.ic_gamble);
                break;
        }
    }
}