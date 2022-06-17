package com.example.multi_purpose_app;

import android.content.Context;
import android.util.JsonWriter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    public boolean write(Context context, String fileName, JSONObject text, String root) throws IOException {
        // Dateipfad und Name definieren
        File file = new File(context.getFilesDir(), fileName);

        try {
            // JSON auslesen und in Map speichern, um Duplikate zu überschreiben
            JSONObject jsonObject = read(context, fileName);
            HashMap<Integer, String> notes = new HashMap<Integer, String>();
            JSONArray jsonArray = null;
            if (jsonObject != null) {
                jsonArray = jsonObject.optJSONArray(root);
            }
            switch (root) {
                case "Notes":
                    for (int n = 0; n < (jsonArray != null ? jsonArray.length() : 0); n++) {
                        JSONObject note = jsonArray.getJSONObject(n);
                        notes.put(note.getInt("ID"), note.getString("Text"));
                    }

                    // Altes JSON-Array clearen
                    while(jsonArray.length()>0)
                    {
                        jsonArray.remove(0);
                    }

                    // Notiz in JSON-Struktur integrieren
                    notes.put(text.getInt("ID"), text.getString("Text"));
                    for (Map.Entry<Integer, String> entry : notes.entrySet()) {
                            JSONObject note = new JSONObject();
                            System.out.println(entry.toString());
                            note.put("ID", entry.getKey());
                            note.put("Text", entry.getValue());
                            jsonArray.put(note);
                    }
                    break;
                case "Lists":
                    break;
                case "Cards":
                    break;
                default:
                    break;
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toString());
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
            HashMap<Integer, String> notes = new HashMap<Integer, String>();
            JSONArray jsonArray = null;
            if (jsonObject != null) {
                jsonArray = jsonObject.optJSONArray(root);
            }
            switch (root) {
                case "Notes":
                    // Altes JSON-Array clearen
                        jsonArray.remove(id - 1);
                    break;
                case "Lists":
                    break;
                case "Cards":
                    break;
                default:
                    break;
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
            Toast.makeText(context, "Gelöscht", Toast.LENGTH_LONG).show();
            return true;
        } catch (IOException e) {
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
