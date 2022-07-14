package com.example.multi_purpose_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class StudycardCollection extends AppCompatActivity implements View.OnClickListener {

    // Button für neue Liste
    Button btnNewStudycard;

    // Container für Listen
    LinearLayout containerStudycards;

    // Storage
    Storage storage = new Storage();

    // Zähler für List-ID
    int StudycardId = 2000000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studycard_collection);

        // Button nach ID festlegen
        btnNewStudycard = findViewById(R.id.btnNewStudycard);
        // OnClickListener für Startbutton
        btnNewStudycard.setOnClickListener(this);

        // Listen-Container nach ID festlegen
        containerStudycards = findViewById(R.id.containerStudycards);

        // Prüfung, ob bereits Notizen vorhanden sind
        checkStudycards();
    }

    private void checkStudycards() {

        boolean isFilePresent = storage.isFilePresent(StudycardCollection.this, "Studycard.json");
        if (isFilePresent) {
            try {
                // JSON-Datei auslesen
                JSONObject jsonObjectIn = storage.read(this, "Studycard.json");
                JSONArray jsonArray = jsonObjectIn.getJSONArray("StudycardList");


                // jsonArray durchlaufen und Inhalt anzeigen
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("Title");
                    createNewStudycardList(title, i);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            boolean isFileCreated = false;
            try {
                isFileCreated = storage.create(StudycardCollection.this, "Studycard.json", "StudycardList");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(isFileCreated) {
                // virtueller Klick auf neue Liste, um erste Liste für Layout zu erstellen
                btnNewStudycard.callOnClick();
            } else {
                Toast.makeText(this, "Fehler beim Erstellen der Sicherungsdatei", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        createNewStudycardList("", StudycardId);
    }

    @SuppressLint("SetTextI18n")
    private void createNewStudycardList(String title, int idA) {
        // Neuen Container in View anlegen
        LinearLayout Studycard = new LinearLayout(getApplicationContext());

        // Parameter für Listen-Container setzen
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(70,70,70,70);
        Studycard.setLayoutParams(params);
        Studycard.setOrientation(LinearLayout.HORIZONTAL);
        Studycard.setPadding(15,5,15,5);
        Studycard.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border, null));

        // Editierbares Titelfeld anlegen
        EditText StudycardlistTitle = new EditText(getApplicationContext());
        LinearLayout.LayoutParams paramsEditTitle = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsEditTitle.weight = 1;
        StudycardlistTitle.setLayoutParams(paramsEditTitle);
        StudycardlistTitle.setHint("Hier StudycardName eingeben");
        StudycardlistTitle.setHintTextColor(Color.GRAY);
        StudycardlistTitle.setTextColor(Color.WHITE);
        StudycardlistTitle.setTypeface(ResourcesCompat.getFont(this, R.font.questrial), Typeface.NORMAL);
        StudycardlistTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(35,0,35,0);

        // Bild für Liste speichern
        final ImageView saveStudycardList = new ImageView(getApplicationContext());
        saveStudycardList.setLayoutParams(layoutParams);
        saveStudycardList.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save, null));

        // Bild für Löschbutton
        final ImageView deleteStudycardList = new ImageView(getApplicationContext());
        deleteStudycardList.setLayoutParams(layoutParams);
        deleteStudycardList.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null));

        // Falls Liste aus Speicher Text übernehmen
        if (title != null) {
            Studycard.setId(idA);
            StudycardlistTitle.setText(title);
        }

        // Titelfeld zu Listen-Container hinzufügen
        Studycard.addView(StudycardlistTitle);
        Studycard.addView(saveStudycardList);
        Studycard.addView(deleteStudycardList);

        // OnClickListener zum Bearbeiten der Liste
        StudycardlistTitle.setOnClickListener(v -> {
            Intent intentTodo = new Intent(this, StudycardCollectionView.class);
            intentTodo.putExtra("title", StudycardlistTitle.getText().toString());
            startActivity(intentTodo);
            this.finish();
        });

        // Listen-Container zu Listen hinzufügen
        containerStudycards.addView(Studycard);

        // OnClickListener zum Löschen
        deleteStudycardList.setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) deleteStudycardList.getParent();
            parent.removeAllViews();
            if(parent.getParent() != null) {
                ((ViewGroup) parent.getParent()).removeView(parent);
            }

            int id = Studycard.getId();

            // Liste in JSON-Datei schreiben
            boolean isStudycardDeleted = false;
            try {
                isStudycardDeleted = storage.delete(StudycardCollection.this, "Studycard.json", "StudycardList", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!isStudycardDeleted) {
                Toast.makeText(this, "Fehler beim Löschen", Toast.LENGTH_LONG).show();
            }

            // reload JSON
            containerStudycards.removeAllViews();
            checkStudycards();
        });

        // OnClickListener zum Speichern
        saveStudycardList.setOnClickListener(v -> {
            String titleString = StudycardlistTitle.getText().toString();
            int id = Studycard.getId();

            // JSON-Objekt der Liste erstellen
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", id);
                jsonObject.put("Title", titleString);

                // Liste in JSON-Datei schreiben
                boolean isStudycardSaved = storage.write(StudycardCollection.this, "Studycard.json", jsonObject, "StudycardList", null);
                if(!isStudycardSaved) {
                    Toast.makeText(this, "Fehler beim Speichern", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            // reload JSON
            containerStudycards.removeAllViews();
            checkStudycards();
        });
    }
}