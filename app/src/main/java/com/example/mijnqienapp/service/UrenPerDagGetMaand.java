package com.example.mijnqienapp.service;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.mijnqienapp.config.Environment;
import com.example.mijnqienapp.domain.UrenPerDag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class UrenPerDagGetMaand  extends AsyncTask<Void, Void, ArrayList<UrenPerDag>> {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected ArrayList<UrenPerDag> doInBackground(Void... longs) {
        ArrayList<UrenPerDag> maand = new ArrayList<>();

        try {
            URL url = new URL(Environment.BASE_API+"urenperdag");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            System.out.println("In doInBackgroud: " + url.toString());

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            // result is an array of objects
            JSONArray jsonArrayUrenPerDag = new JSONArray(response.toString());

            for (int i = 0; i < jsonArrayUrenPerDag.length(); i++) {
                JSONObject jsonObjectUrenPerDag = jsonArrayUrenPerDag.getJSONObject(i);

                maand.add(new UrenPerDag(jsonObjectUrenPerDag));
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maand;
    }
}
