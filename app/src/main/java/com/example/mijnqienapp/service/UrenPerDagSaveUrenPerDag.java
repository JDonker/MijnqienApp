package com.example.mijnqienapp.service;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.mijnqienapp.UrenPerDagCrudActivity;
import com.example.mijnqienapp.config.Environment;
import com.example.mijnqienapp.domain.UrenPerDag;




public class UrenPerDagSaveUrenPerDag extends AsyncTask<Void, Void, UrenPerDag> {
     UrenPerDag urenPerDag;
    int mCommand;

    public UrenPerDagSaveUrenPerDag(UrenPerDag urenPerDag, int command){
        this.urenPerDag = urenPerDag;
        this.mCommand = command;
        // NB: Aanname is dat bij het commando create, auto.getid() = 0, en dat bij
        // de commandos delete en update, auto.getId() <> 0. Dit wordt hier overigens (ten onrechte)
        // niet gecontroleerd
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected UrenPerDag doInBackground(Void... longs) {
        UrenPerDag returnObject = null;
        HttpURLConnection con = null;
        try {
            // zet het op te slaan object om naar een string, die naar de backend gestuurd kan worden
            JSONObject jsonUPD = urenPerDag.getJSON();

            // Open een kanaal naar de server
            URL url;
            if (this.urenPerDag.getId()==0) {             // Nieuw auto --> create
                url = new URL(Environment.BASE_API+"urenperdag");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");     // Create -> POST
            } else {                                // Bestaande auto --> delete of update
                url = new URL(Environment.BASE_API + "urenperdag/" + urenPerDag.getId());
                con = (HttpURLConnection) url.openConnection();
                if (mCommand == UrenPerDagCrudActivity.UPD_UPDATE) { // Update -> PUT
                    con.setRequestMethod("PUT");
                } else
                    con.setRequestMethod("DELETE");
            }

            if (mCommand== UrenPerDagCrudActivity.UPD_DELETE) {
                // Verwerk response
                StringBuilder response = null;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                returnObject = null;

            } else {
                // Geef de auto aan de backend door via het kanaal
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write( jsonUPD.toString());
                wr.flush();

                // Verwerk response
                StringBuilder response = null;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                JSONObject jsonUPDReturn = new JSONObject(response.toString());

                returnObject = new UrenPerDag(jsonUPDReturn);
            }


        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } finally {
            con.disconnect();
        }

        return returnObject;
    }
}
