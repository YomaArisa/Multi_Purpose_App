package com.example.multi_purpose_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class ToDo extends AppCompatActivity implements View.OnClickListener {

    // Listentitel
    TextView todoListName;

    // Button für neue Liste
    Button btnSaveList;

    // Button für neues Item
    Button btnNewItem;

    // Container für Listen
    LinearLayout containerItems;

    // Storage
    Storage storage = new Storage();

    // Zähler für List-ID
    int itemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        // Titel nach ID und Inhalt festlegen
        todoListName = findViewById(R.id.todoListName);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            todoListName.setText(extras.getString("title"));
        }

        // Buttons nach ID festlegen
        btnSaveList = findViewById(R.id.btnSaveList);
        btnNewItem = findViewById(R.id.btnNewItem);

        // OnClickListener für Startbutton
        btnNewItem.setOnClickListener(this);

        // Listen-Container nach ID festlegen
        containerItems = findViewById(R.id.containerTodo);

        btnSaveList.setOnClickListener(v -> {
            Intent intentTodo = new Intent(this, ToDoStart.class);
            startActivity(intentTodo);
            this.finish();
        });

        // Prüfung, ob bereits Items vorhanden sind
        checkItems();
    }

    private void checkItems() {

        boolean isFilePresent = storage.isFilePresent(ToDo.this, "todo.json");
        if (isFilePresent) {
            try {
                // JSON-Datei auslesen
                JSONObject jsonObjectIn = storage.read(this, "todo.json");
                JSONArray jsonArray = jsonObjectIn.getJSONArray("Lists");

                // jsonArray durchlaufen und Inhalt anzeigen
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItemObject = jsonArray.getJSONObject(i);
                    if (jsonItemObject.getString("Title").contentEquals(todoListName.getText())) {
                        JSONArray jsonItemArray = jsonItemObject.optJSONArray("Items");
                        for (int n = 0; n < Objects.requireNonNull(jsonItemArray).length(); n++) {
                            JSONObject jsonObject = jsonItemArray.getJSONObject(n);
                            String item = jsonObject.getString("Item");
                            boolean checked = jsonObject.getBoolean("IsChecked");
                            createNewItem(item, checked, n);
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Fehler beim Prüfen der Liste", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        createNewItem("", false, itemId);
    }

    @SuppressLint("SetTextI18n")
    private void createNewItem(String itemValue, boolean checked, int idA) {
        // Neuen Container in View anlegen
        LinearLayout item = new LinearLayout(getApplicationContext());

        // Parameter für Item-Container setzen
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(70,70,70,70);
        item.setLayoutParams(params);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setPadding(15,5,15,5);
        item.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border, null));

        // Checkbox für Item
        CheckBox checkItem = new CheckBox(getApplicationContext());
        LinearLayout.LayoutParams paramsCheckItem = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsCheckItem.weight = 1;
        checkItem.setLayoutParams(paramsCheckItem);
        checkItem.setVisibility(View.VISIBLE);
        checkItem.setHint("Hier Aufgabe eingeben");
        checkItem.setHintTextColor(Color.GRAY);
        checkItem.setTextColor(Color.WHITE);
        checkItem.setTypeface(ResourcesCompat.getFont(this, R.font.questrial), Typeface.NORMAL);
        checkItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        // Bild für Item bearbeiten
        final ImageView btnEditItem = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnEditItem.setLayoutParams(layoutParams);
        btnEditItem.setVisibility(View.VISIBLE);
        btnEditItem.setBackground(ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_edit, null));

        // Editierbares Textfeld für Aufgabe
        EditText editItem = new EditText(getApplicationContext());
        LinearLayout.LayoutParams paramsEditItem = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsEditItem.weight = 1;
        editItem.setLayoutParams(paramsEditItem);
        editItem.setVisibility(View.GONE);
        editItem.setHint("Hier Aufgabe eingeben");
        editItem.setHintTextColor(Color.GRAY);
        editItem.setTextColor(Color.WHITE);
        editItem.setTypeface(ResourcesCompat.getFont(this, R.font.questrial), Typeface.NORMAL);
        editItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        // Bild für Item speichern
        final ImageView saveItem = new ImageView(getApplicationContext());
        saveItem.setLayoutParams(layoutParams);
        saveItem.setVisibility(View.GONE);
        saveItem.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_save, null));

        // Falls Item aus Speicher Text übernehmen
        if (itemValue != null) {
            item.setId(idA);
            checkItem.setText(itemValue);
            checkItem.setChecked(checked);
        }

        // Checkbox, Textfeld und Speicherbutton zu Item-Container hinzufügen
        item.addView(checkItem);
        item.addView(btnEditItem);
        item.addView(editItem);
        item.addView(saveItem);

        // Item-Container zu Listen hinzufügen
        containerItems.addView(item);

        // OnClickListen um Checkbox Text zu ändern
        btnEditItem.setOnClickListener(v -> {
            // Sichtbarkeit ändern um Checkbox zu bearbeiten
            checkItem.setVisibility(View.GONE);
            btnEditItem.setVisibility(View.GONE);
            editItem.setVisibility(View.VISIBLE);
            saveItem.setVisibility(View.VISIBLE);
        });

        checkItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Speichern des Items in JSON
            String itemText = checkItem.getText().toString();
            int id = item.getId();
            saveItem(itemText, id, isChecked);
        });

        // OnClickListener zum Speichern
        saveItem.setOnClickListener(v -> {
            checkItem.setChecked(false);
            boolean isChecked = checkItem.isChecked();
            String itemText = editItem.getText().toString();


            // Sichbarkeit ändern um Checkbox mit neuen Text anzuzeigen
            checkItem.setText(itemText);
            editItem.setVisibility(View.GONE);
            saveItem.setVisibility(View.GONE);
            checkItem.setVisibility(View.VISIBLE);
            btnEditItem.setVisibility(View.VISIBLE);

            // Speichern des Items in JSON
            int id = item.getId();
            saveItem(itemText, id, isChecked);
        });
    }

    private void saveItem(String itemText, int id, boolean isChecked) {
        // JSON-Objekt des Items erstellen
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", id);
            jsonObject.put("Item", itemText);
            jsonObject.put("IsChecked", isChecked);

            // Item in JSON-Datei schreiben
            boolean isNoteSaved = storage.write(ToDo.this, "todo.json", jsonObject, "Items", todoListName.getText().toString());
            if(!isNoteSaved) {
                Toast.makeText(this, "Fehler beim Speichern", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        // reload JSON
        containerItems.removeAllViews();
        checkItems();
    }
}