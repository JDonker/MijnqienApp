package com.example.mijnqienapp.service;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

public class UrenPerDagGetUrenPerDag extends AsyncTask<Void, Void, UrenPerDag> {
    long urenPerDagId;

    public UrenPerDagGetUrenPerDag(long id){
        this.urenPerDagId = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected UrenPerDag doInBackground(Void... longs) {
        UrenPerDag urenPerDag = null;
        try {
            URL url = new URL(Environment.BASE_API + "urenperdag/" + urenPerDagId);
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
            // result is an objects
            JSONObject jsonUrenPerDag = new JSONObject(response.toString());
            urenPerDag = new UrenPerDag(jsonUrenPerDag);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return urenPerDag;
    }
}
