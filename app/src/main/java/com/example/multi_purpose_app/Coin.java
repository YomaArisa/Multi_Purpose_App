package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class Coin extends AppCompatActivity implements View.OnClickListener {

    // Startbutton
    Button btnStart;
    // Bild für Münze
    ImageView changeCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        // Button nach ID festlegen
        btnStart = (Button) findViewById(R.id.btnStart);
        // OnClickListener für Startbutton
        btnStart.setOnClickListener(this);

        // View für Münze nach ID festlegen
        changeCoin = (ImageView) findViewById(R.id.iVCoin);
    }

    @Override
    public void onClick(View v) {
        flipCoin();
    }

    private void flipCoin() {
        // neue Instanz der Klasse Random
        Random rand = new Random();
        int max = 2;
        // Zufallswert von 1-2
        int side = rand.nextInt(max) + 1;

        System.out.println(side);

        // Zeige Kopf
        if (side == 1) {
            changeCoin.setImageResource(R.drawable.ic_coin_heads);
        }
        // Zeige Zahl
        else if (side == 2) {
            changeCoin.setImageResource(R.drawable.ic_coin_tails);
        }
        // Zeige Fehler
        else {
            changeCoin.setImageResource(R.drawable.ic_muenze);
        }
    }
}