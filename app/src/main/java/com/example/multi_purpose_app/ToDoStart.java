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

public class ToDoStart extends AppCompatActivity implements View.OnClickListener {

    // Button für neue Liste
    Button btnNewList;

    // Container für Listen
    LinearLayout containerLists;

    // Storage
    Storage storage = new Storage();

    // Zähler für List-ID
    int listId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_start);

        // Button nach ID festlegen
        btnNewList = findViewById(R.id.btnNewList);
        // OnClickListener für Startbutton
        btnNewList.setOnClickListener(this);

        // Listen-Container nach ID festlegen
        containerLists = findViewById(R.id.containerLists);

        // Prüfung, ob bereits Notizen vorhanden sind
        checkLists();
    }

    private void checkLists() {

        boolean isFilePresent = storage.isFilePresent(ToDoStart.this, "todo.json");
        if (isFilePresent) {
            try {
                // JSON-Datei auslesen
                JSONObject jsonObjectIn = storage.read(this, "todo.json");
                JSONArray jsonArray = jsonObjectIn.getJSONArray("Lists");


                // jsonArray durchlaufen und Inhalt anzeigen
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("Title");
                    createNewList(title, i);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            boolean isFileCreated = false;
            try {
                isFileCreated = storage.create(ToDoStart.this, "todo.json", "Lists");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(isFileCreated) {
                // virtueller Klick auf neue Liste, um erste Liste für Layout zu erstellen
                btnNewList.callOnClick();
            } else {
                Toast.makeText(this, "Fehler beim Erstellen der Sicherungsdatei", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        createNewList("", listId);
    }

    @SuppressLint("SetTextI18n")
    private void createNewList(String title, int idA) {
        // Neuen Container in View anlegen
        LinearLayout list = new LinearLayout(getApplicationContext());

        // Parameter für Listen-Container setzen
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(70,70,70,70);
        list.setLayoutParams(params);
        list.setOrientation(LinearLayout.HORIZONTAL);
        list.setPadding(15,5,15,5);
        list.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border, null));

        // Editierbares Titelfeld anlegen
        EditText listTitle = new EditText(getApplicationContext());
        LinearLayout.LayoutParams paramsEditTitle = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsEditTitle.weight = 1;
        listTitle.setLayoutParams(paramsEditTitle);
        listTitle.setHint("Hier Listentitel eingeben");
        listTitle.setHintTextColor(Color.GRAY);
        listTitle.setTextColor(Color.WHITE);
        listTitle.setTypeface(ResourcesCompat.getFont(this, R.font.questrial), Typeface.NORMAL);
        listTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(35,0,35,0);

        // Bild für Liste speichern
        final ImageView saveList = new ImageView(getApplicationContext());
        saveList.setLayoutParams(layoutParams);
        saveList.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save, null));

        // Bild für Löschbutton
        final ImageView deleteList = new ImageView(getApplicationContext());
        deleteList.setLayoutParams(layoutParams);
        deleteList.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_delete, null));

        // Falls Liste aus Speicher Text übernehmen
        if (title != null) {
            list.setId(idA);
            listTitle.setText(title);
        }

        // Titelfeld zu Listen-Container hinzufügen
        list.addView(listTitle);
        list.addView(saveList);
        list.addView(deleteList);

        // OnClickListener zum Bearbeiten der Liste
        listTitle.setOnClickListener(v -> {
            Intent intentTodo = new Intent(this, ToDo.class);
            intentTodo.putExtra("title", listTitle.getText().toString());
            startActivity(intentTodo);
            this.finish();
        });

        // Listen-Container zu Listen hinzufügen
        containerLists.addView(list);

        // OnClickListener zum Löschen
        deleteList.setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) deleteList.getParent();
            parent.removeAllViews();
            if(parent.getParent() != null) {
                ((ViewGroup) parent.getParent()).removeView(parent);
            }

            int id = list.getId();

            // Liste in JSON-Datei schreiben
            boolean isNoteDeleted = false;
            try {
                isNoteDeleted = storage.delete(ToDoStart.this, "todo.json", "Lists", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!isNoteDeleted) {
                Toast.makeText(this, "Fehler beim Löschen", Toast.LENGTH_LONG).show();
            }

            // reload JSON
            containerLists.removeAllViews();
            checkLists();
        });

        // OnClickListener zum Speichern
        saveList.setOnClickListener(v -> {
            String titleString = listTitle.getText().toString();
            int id = list.getId();

            // JSON-Objekt der Liste erstellen
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", id);
                jsonObject.put("Title", titleString);

                // Liste in JSON-Datei schreiben
                boolean isNoteSaved = storage.write(ToDoStart.this, "todo.json", jsonObject, "Lists", null);
                if(!isNoteSaved) {
                    Toast.makeText(this, "Fehler beim Speichern", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            // reload JSON
            containerLists.removeAllViews();
            checkLists();
        });
    }
}

