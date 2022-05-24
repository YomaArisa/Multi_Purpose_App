package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Buttons
    LinearLayout btnGamble;
    LinearLayout btnNotes;
    LinearLayout btnRandom;
    LinearLayout btnStopwatch;
    LinearLayout btnTodo;
    LinearLayout btnStudycards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OnClickListener für Buttons
        btnGamble = (LinearLayout) findViewById(R.id.btnGamble);
        btnGamble.setOnClickListener(this);

        btnNotes = (LinearLayout) findViewById(R.id.btnNotes);
        btnNotes.setOnClickListener(this);

        btnRandom = (LinearLayout) findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(this);

        btnStopwatch = (LinearLayout) findViewById(R.id.btnStopwatch);
        btnStopwatch.setOnClickListener(this);

        btnTodo = (LinearLayout) findViewById(R.id.btnTodo);
        btnTodo.setOnClickListener(this);

        btnStudycards = (LinearLayout) findViewById(R.id.btnStudycard);
        btnStudycards.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGamble:
                Intent intentGamble = new Intent(this, Gamble.class);
                startActivity(intentGamble);
                this.finish();
                break;
            case R.id.btnNotes:
                Intent intentNotes = new Intent(this, Notes.class);
                startActivity(intentNotes);
                this.finish();
                break;
            default:
                break;
        }
    }
}