package com.example.multi_purpose_app;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    // Erstellen der JSON-Datei
    public boolean create(Context context, String fileName, String root) throws IOException {
        // Dateipfad und Name definieren
        File file = new File(context.getFilesDir(), fileName);

        // JSON-Struktur anlegen und in Datei schreiben
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("{\"" + root + "\":[ \n ] \n}");
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
            return false;
        }

    }

    public boolean write(Context context, String fileName, JSONObject text, String root, String object) throws IOException {
        // Dateipfad und Name definieren
        File file = new File(context.getFilesDir(), fileName);

        try {
            // JSON auslesen
            JSONObject jsonObject = read(context, fileName);
            JSONArray jsonArray;

            switch (root) {
                case "Notes":
                    // JSON-Objekte in Map speichern, um Duplikate zu überschreiben
                    HashMap<Integer, String> notes = new HashMap<>();
                    jsonArray = null;
                    if (jsonObject != null) {
                        jsonArray = jsonObject.optJSONArray(root);
                    }
                    for (int n = 0; n < (jsonArray != null ? jsonArray.length() : 0); n++) {
                        JSONObject note = jsonArray.getJSONObject(n);
                        notes.put(note.getInt("ID"), note.getString("Text"));
                    }

                    // Altes JSON-Array clearen
                    if (jsonArray != null) {
                        while(jsonArray.length()>0)
                        {
                            jsonArray.remove(0);
                        }
                    }

                    // Notiz in JSON-Struktur integrieren
                    notes.put(text.getInt("ID"), text.getString("Text"));
                    for (Map.Entry<Integer, String> entry : notes.entrySet()) {
                            JSONObject note = new JSONObject();
                            note.put("ID", entry.getKey());
                            note.put("Text", entry.getValue());
                        if (jsonArray != null) {
                            jsonArray.put(note);
                        }
                    }
                    break;
                case "Lists":
                    int id = 0;
                    // JSON-Objekte in Map speichern, um Duplikate zu überschreiben
                    HashMap<Integer, String> lists = new HashMap<>();
                    jsonArray = null;
                    if (jsonObject != null) {
                        jsonArray = jsonObject.optJSONArray(root);
                    }
                    for (int n = 0; n < (jsonArray != null ? jsonArray.length() : 0); n++) {
                        JSONObject list = jsonArray.getJSONObject(n);
                        lists.put(list.getInt("ID"), list.getString("Title"));
                    }

                    // ID festlegen
                    if (text.getInt("ID") == 0) {
                        if (jsonArray != null) {
                            id = jsonArray.length();
                        }
                    } else {
                        id = text.getInt("ID");
                    }

                    // Altes JSON-Array clearen
                    if (jsonArray != null) {
                        while(jsonArray.length()>0)
                        {
                            jsonArray.remove(0);
                        }
                    }

                    // Liste in JSON-Struktur integrieren
                    lists.put(id, text.getString("Title"));
                    for (Map.Entry<Integer, String> entry : lists.entrySet()) {
                        JSONObject list = new JSONObject();
                        list.put("ID", entry.getKey());
                        list.put("Title", entry.getValue());
                        list.put("Items", new JSONArray());
                        if (jsonArray != null) {
                            jsonArray.put(list);
                        }
                    }
                    break;
                case "Items":
                    id = 0;
                    // JSON-Objekte in Map speichern, um Duplikate zu überschreiben
                    HashMap<Integer, ArrayList<String>> items = new HashMap<>();
                    ArrayList<String> itemVals = new ArrayList<>();
                    jsonArray = null;
                    if (jsonObject != null) {
                        JSONArray jsonRootArray = jsonObject.optJSONArray("Lists");
                        // jsonArray durchlaufen um Object zu ermitteln
                        if (jsonRootArray != null) {
                            for (int i = 0; i < jsonRootArray.length(); i++) {
                                JSONObject jsonItemObject = jsonRootArray.getJSONObject(i);
                                if (jsonItemObject.getString("Title").equals(object)) {
                                    jsonArray = jsonItemObject.getJSONArray("Items");
                                }
                            }
                        }
                    }

                    for (int n = 0; n < (jsonArray != null ? jsonArray.length() : 0); n++) {
                        JSONObject item = jsonArray.getJSONObject(n);
                        items.put(item.getInt("ID"), new ArrayList<>());
                        items.get(item.getInt("ID")).add(item.getString("Item"));
                        items.get(item.getInt("ID")).add(item.getString("IsChecked"));
                    }

                    // ID festlegen
                    if (text.getInt("ID") == 0) {
                        if (jsonArray != null) {
                            id = jsonArray.length();
                        }
                    } else {
                        id = text.getInt("ID");
                    }

                    // Altes JSON-Array clearen
                    if (jsonArray != null) {
                        while(jsonArray.length()>0)
                        {
                            jsonArray.remove(0);
                        }
                    }

                    // Item in JSON-Struktur integrieren
                    items.put(id, new ArrayList<>());
                    items.get(id).add(text.getString("Item"));
                    items.get(id).add(text.getString("IsChecked"));
                    for (Map.Entry<Integer, ArrayList<String>> entry : items.entrySet()) {
                        JSONObject item = new JSONObject();
                        item.put("ID", entry.getKey());
                        ArrayList<String> itemVal = entry.getValue();
                        item.put("Item", itemVal.get(0));
                        item.put("IsChecked", itemVal.get(1));
                        if (jsonArray != null) {
                            jsonArray.put(item);
                        }
                    }
                    System.out.println(jsonArray);
                    break;
                case "Cards":
                    break;
                default:
                    break;
            }

            FileWriter fileWriter = new FileWriter(file);
            if (jsonObject != null) {
                fileWriter.write(jsonObject.toString());
            }
            fileWriter.close();
            Toast.makeText(context, "Gespeichert", Toast.LENGTH_LONG).show();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Speichern fehlgeschlagen", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // Auslesen der JSON-Datei
    public JSONObject read(Context context, String fileName) {
        try {
            // Dateipfad und Name definieren
            File file = new File(context.getFilesDir(), fileName);

            // Datei auslesen
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            return new JSONObject(sb.toString());
        } catch (IOException | JSONException fileNotFound) {
            return null;
        }
    }

    public boolean delete (Context context, String fileName, String root, int id) throws JSONException {
        // Dateipfad und Name definieren
        File file = new File(context.getFilesDir(), fileName);

        try {
            // JSON auslesen
            JSONObject jsonObject = read(context, fileName);
            JSONArray jsonArray = null;
            if (jsonObject != null) {
                jsonArray = jsonObject.optJSONArray(root);
            }

            // Aus JSON löschen
            if (jsonArray != null) {
                jsonArray.remove(id);
            }

                FileWriter fileWriter = new FileWriter(file);
                if (jsonObject != null) {
                    fileWriter.write(jsonObject.toString());
                }
                fileWriter.close();
                Toast.makeText(context, "Gelöscht", Toast.LENGTH_LONG).show();
                return true;
            } catch(IOException e){
                e.printStackTrace();
                return false;
            }
    }


    // Prüfung, ob json-Datei vorhanden ist
    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}
