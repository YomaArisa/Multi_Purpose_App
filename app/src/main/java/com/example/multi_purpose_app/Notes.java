package com.example.multi_purpose_app;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Notes extends AppCompatActivity implements View.OnClickListener {

    // Button für neue Notiz
    Button btnNewNote;

    // Container für Notizen
    LinearLayout notes;

    // Storage
    Storage storage = new Storage();

    // Zähler für Notiz-ID
    int noteId = 2000000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Button nach ID festlegen
        btnNewNote = findViewById(R.id.btnNewNote);
        // OnClickListener für Startbutton
        btnNewNote.setOnClickListener(this);

        // Notiz-Container nach ID festlegen
        notes = findViewById(R.id.containerNotes);

        // Prüfung, ob bereits Notizen vorhanden sind
        checkNotes();
    }

    private void checkNotes() {

        boolean isFilePresent = storage.isFilePresent(Notes.this, "notes.json");
        if(isFilePresent) {
            try {
                // JSON-Datei auslesen
                JSONObject jsonObjectIn = storage.read(this, "notes.json");
                JSONArray jsonArray = jsonObjectIn.getJSONArray("Notes");

                // jsonArray durchlaufen und Inhalt anzeigen
                for (int i=0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String text = jsonObject.getString("Text");
                    createNewNote(text, i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            boolean isFileCreated = false;
            try {
                isFileCreated = storage.create(Notes.this, "notes.json", "Notes");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(isFileCreated) {
                // virtueller Klick auf neue Notiz, um erste Notiz für Layout zu erstellen
                btnNewNote.callOnClick();
            } else {
                Toast.makeText(this, "Fehler beim Erstellen der Sicherungsdatei", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        createNewNote("", noteId);
    }

    @SuppressLint("SetTextI18n")
    private void createNewNote(String text, int id) {
        // Neuen Container in View anlegen
        LinearLayout note = new LinearLayout(getApplicationContext());

        // Parameter für Notiz-Container setzen
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(70,70,70,70);
        note.setLayoutParams(params);
        note.setOrientation(LinearLayout.HORIZONTAL);
        note.setPadding(15,5,15,5);
        note.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border, null));
        note.setId(noteId);

        // Editierbares Notizfeld anlegen
        EditText noteText = new EditText(getApplicationContext());
        LinearLayout.LayoutParams paramsEditText = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsEditText.weight = 1;
        noteText.setLayoutParams(paramsEditText);
        noteText.setHint("Hier Notiz eingeben");
        noteText.setHintTextColor(Color.GRAY);
        noteText.setTextColor(Color.WHITE);
        noteText.setTypeface(ResourcesCompat.getFont(this, R.font.questrial), Typeface.NORMAL);
        noteText.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(35,0,35,0);

        // Bild für Notiz speichern
        final ImageView saveNote = new ImageView(getApplicationContext());
        saveNote.setLayoutParams(layoutParams);
        saveNote.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save, null));

        // Bild für Löschbutton
        final ImageView deleteNote = new ImageView(getApplicationContext());
        deleteNote.setLayoutParams(layoutParams);
        deleteNote.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null));

        // Falls Notiz aus Speicher Text übernehmen
        if (text != null) {
            noteText.setText(text);
        }

        // Notizfeld zu Notiz-Container hinzufügen
        note.addView(noteText);
        note.addView(saveNote);
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

            // Notiztext in JSON-Datei schreiben
            boolean isNoteDeleted = false;
            try {
                isNoteDeleted = storage.delete(Notes.this, "notes.json", "Notes", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!isNoteDeleted) {
                Toast.makeText(this, "Fehler beim Löschen", Toast.LENGTH_LONG).show();

            }

            // reload JSON
            notes.removeAllViews();
            checkNotes();
        });

        // OnClickListener zum Speichern
        saveNote.setOnClickListener(v -> {
            String noteString = noteText.getText().toString();

            // JSON-Objekt der Notiz erstellen
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", id);
                jsonObject.put("Text", noteString);

                // Notiztext in JSON-Datei schreiben
                boolean isNoteSaved = storage.write(Notes.this, "notes.json", jsonObject, "Notes", null);
                if(!isNoteSaved) {
                    Toast.makeText(this, "Fehler beim Speichern", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            // reload JSON
            notes.removeAllViews();
            checkNotes();
        });
    }
}