package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Random extends AppCompatActivity implements View.OnClickListener {

    // Startbutton
    Button btnStart;

    // Von Eingabe
    EditText editStart;

    // Bis Eingabe
    EditText editEnd;

    // Ergebnis
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        // Button nach ID festlegen
        btnStart = (Button) findViewById(R.id.btnStart);
        // OnClickListener für Startbutton
        btnStart.setOnClickListener(this);

        // Von Eingabe nach ID festlegen
        editStart = (EditText) findViewById(R.id.randomStartValue);

        // Bis Eingabe nach ID festlegen
        editEnd = (EditText) findViewById(R.id.randomEndValue);

        // Ergebnis nach ID festlegen
        result = (TextView) findViewById(R.id.randomResultValue);
    }

    @Override
    public void onClick(View v) {
        calcResult();
    }

    private void calcResult() {
        int start = Integer.parseInt(editStart.getText().toString());
        int end = Integer.parseInt(editEnd.getText().toString());

        // Zufallszahl von x bis y berechnen
        int r = (int) (Math.random() * (end - start)) + start;
        System.out.println(r);

        // Ergebnis ausgeben
        result.setText(Integer.toString(r));
    }
}