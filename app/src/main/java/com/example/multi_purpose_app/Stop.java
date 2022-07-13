package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Stop extends AppCompatActivity implements View.OnClickListener {

    // Timer text
    TextView stopTime;

    // Start/Pause button
    Button btnStart;

    // Reset button
    Button btnRound;


    boolean timerRunning = false;
    boolean timerStarted = false;
    long startTime;
    long pauseTime;
    long missedTime = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);

        // Button nach ID festlegen
        btnStart = (Button) findViewById(R.id.btnStart);
        btnRound = (Button) findViewById(R.id.btnRound);
        // OnClickListener für Startbutton
        btnStart.setOnClickListener(this);
        btnRound.setOnClickListener(this);
        // TextView nach ID festlegen
        stopTime = (TextView) findViewById(R.id.stopTime);
        btnRound.setText("RESET");
        System.out.println(System.currentTimeMillis());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                runTimer();
                break;
            case R.id.btnRound:
                resetTimer();
                break;
            default:
                break;
        }
    }

    private void runTimer() {
        if (!timerStarted) {
            startTime = System.currentTimeMillis();
            timerStarted = true;
            pauseTime = startTime;
        }
        if (!timerRunning) {
            timerRunning = true;
            btnStart.setText("PAUSE");
            missedTime = missedTime + (System.currentTimeMillis() - pauseTime);

            handler.post(new Runnable() {

                @Override
                public void run()
                {
                    if (timerRunning) {
                        long milliTime = System.currentTimeMillis() - startTime - missedTime;
                        long minutes = milliTime / 60000;
                        long seconds = (milliTime / 1000) % 60;
                        long milliSeconds = (milliTime % 1000) / 10;
                        String elapsedTime = String.format("%02d:%02d.%02d", minutes, seconds, milliSeconds);
                        stopTime.setText(elapsedTime);
                        handler.postDelayed(this, 10);
                    }
                }
            });
        }
        else {
            timerRunning = false;
            pauseTime = System.currentTimeMillis();
            btnStart.setText("RESUME");
        }
    }

    private void resetTimer() {
        stopTime.setText("00:00.00");
        timerStarted = false;
        timerRunning = false;
        missedTime = 0;
        btnStart.setText("START");
    }
}