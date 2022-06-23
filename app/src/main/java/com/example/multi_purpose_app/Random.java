package com.example.multi_purpose_app;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        btnStart = findViewById(R.id.btnStart);
        // OnClickListener für Startbutton
        btnStart.setOnClickListener(this);

        // Von Eingabe nach ID festlegen
        editStart = findViewById(R.id.randomStartValue);

        // Textfarbe nach Eingabe ändern
        editStart.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                editStart.setTextColor(Color.WHITE);
            }
        });

        // Bis Eingabe nach ID festlegen
        editEnd = findViewById(R.id.randomEndValue);

        // Textfarbe nach Eingabe ändern
        editEnd.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                editEnd.setTextColor(Color.WHITE);
            }
        });

        // Ergebnis nach ID festlegen
        result = findViewById(R.id.randomResultValue);
    }



    @Override
    public void onClick(View v) {
                calcResult();
    }

    @SuppressLint("SetTextI18n")
    private void calcResult() {
        int start = Integer.parseInt(editStart.getText().toString());

        int end = Integer.parseInt(editEnd.getText().toString());

        if (start > end) {
            result.setText("Start größer als Ende");
        } else {
            // Zufallszahl von x bis y berechnen
            int r = (int) (Math.random() * (end - start)) + start;
            System.out.println(r);

            // Ergebnis ausgeben
            result.setText(Integer.toString(r));
        }
    }
}