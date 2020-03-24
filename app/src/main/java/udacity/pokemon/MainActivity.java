/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package udacity.pokemon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView list_view;

    ArrayList<HashMap<String, String>> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonList = new ArrayList<>();
        list_view = findViewById(R.id.list);

        new GetPokemon().execute();
    }

    private class GetPokemon extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json";
            String jsonString = "";

            try {
                // DONE: make a request to the URL
                jsonString = sh.makeHttpRequest(url);

            } catch (IOException e) {
                return null;
            }

            Log.e(TAG, "Response from Url: " + jsonString);
            if (jsonString != null) {
                try {
                    // DONE: Create a new JSONObject
                    JSONObject root = new JSONObject(jsonString);

                    // DONE: Get the JSON Array node and name it "pokemons"
                    JSONArray pokemons = root.getJSONArray("pokemon");

                    // looping through all Contacts
                    for (int i = 0; i < pokemons.length(); i++) {
                        //DONE: get the JSONObject and its three attributes
                        JSONObject jsonObject = pokemons.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("id");
                        String candy = jsonObject.getString("candy");

                        // tmp hash map for a single pokemon
                        HashMap<String, String> pokemon = new HashMap<>();

                        // add each child node to HashMap key => value
                        pokemon.put("name", name);
                        pokemon.put("id", id);
                        pokemon.put("candy", candy);

                        // adding a pokemon to our pokemon list
                        pokemonList.add(pokemon);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show());
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                        "Couldn't get json from server.",
                        Toast.LENGTH_LONG).show());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            list_view.setAdapter(new SimpleAdapter(
                    MainActivity.this,
                    pokemonList,
                    R.layout.list_item,
                    new String[]{"name", "id", "candy"},
                    new int[]{R.id.name, R.id.id, R.id.candy}
            ));
        }
    }
}
