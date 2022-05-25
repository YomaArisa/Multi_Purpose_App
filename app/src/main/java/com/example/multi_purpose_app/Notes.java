package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Notes extends AppCompatActivity implements View.OnClickListener {

    // Button für neue Notiz
    Button btnNewNote;

    // Container für Notizen
    LinearLayout notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Button nach ID festlegen
        btnNewNote = (Button) findViewById(R.id.btnNewNote);
        // OnClickListener für Startbutton
        btnNewNote.setOnClickListener(this);

        // Notiz-Container nach ID festlegen
        notes = (LinearLayout) findViewById(R.id.containerNotes);

        // virtueller Klick auf neue Notiz, um erste Notiz für Layout zu erstellen
        btnNewNote.callOnClick();
    }

    @Override
    public void onClick(View v) {
        createNewNote();
        saveNewNote();
    }

    private void createNewNote() {
        // Neuen Container in View anlegen
        LinearLayout note = new LinearLayout(getApplicationContext());

        // Parameter für Notiz-Container setzen
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        note.setLayoutParams(params);
        note.setOrientation(LinearLayout.HORIZONTAL);
        note.setPadding(15,5,15,5);
        note.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border, null));

        // Editierbares Notizfeld anlegen
        EditText noteText = new EditText(getApplicationContext());
        LinearLayout.LayoutParams paramsEditText = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsEditText.weight = 1;
        noteText.setLayoutParams(paramsEditText);
        noteText.setHint("Hier Notiz eingeben");
        noteText.setHintTextColor(Color.GRAY);
        noteText.setTextColor(Color.WHITE);
        noteText.setTypeface(ResourcesCompat.getFont(this, R.font.questrial), Typeface.NORMAL);

        // Bild für Löschbutton
        final ImageView deleteNote = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams paramsDeleteNote = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsDeleteNote.weight = 0.15f;
        deleteNote.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null));

        // Notizfeld zu Notiz-Container hinzufügen
        note.addView(noteText);
        note.addView(deleteNote);

        // Notiz-Container zu Notizen hinzufügen
        notes.addView(note);

        // OnClickListener zum Löschen
        deleteNote.setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) deleteNote.getParent();
            parent.removeAllViews();
            if(parent.getParent() != null) {
                ((ViewGroup) parent.getParent()).removeView(parent);
            }
        });
    }

    private void saveNewNote() {
        // Notiz speichern
    }
}