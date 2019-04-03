package com.example.mijnqienapp.domain;


import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

public class UrenPerDag {

    long id;
    private LocalDate datum;
    private int opdracht;
    private int overwerk;
    private int training;
    private int verlof;
    private int ziek;
    private int overig;
    private String verklaring;


    public UrenPerDag(long id, LocalDate datum, int opdracht, int overwerk,int training,int verlof,int ziek, int overig,String verklaring) {
        super();
        this.id = id;
        this.datum = datum;
        this.opdracht = opdracht;
        this.overwerk= overwerk;
        this.training=training;
        this.verlof=verlof;
        this.ziek=ziek;
        this.overig=overig;
        this.verklaring=verklaring;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public int getOpdracht() {
        return opdracht;
    }

    public void setOpdracht(int opdracht) {
        this.opdracht = opdracht;
    }

    public int getOverwerk() {
        return overwerk;
    }

    public void setOverwerk(int overwerk) {
        this.overwerk = overwerk;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UrenPerDag(JSONObject jsonUrenPerDag) throws JSONException {
        this(
                jsonUrenPerDag.getInt("id"),
                LocalDate.parse(jsonUrenPerDag.getString("datum")),
                jsonUrenPerDag.getInt("opdracht"),
                jsonUrenPerDag.getInt("overwerk"),
                jsonUrenPerDag.getInt("training"),
                jsonUrenPerDag.getInt("verlof"),
                jsonUrenPerDag.getInt("ziek"),
                jsonUrenPerDag.getInt("overig"),
                jsonUrenPerDag.getString("verklaringOverig")
        );
        System.out.println(jsonUrenPerDag.getString("datum"));
    }

    public int getTraining() {
        return training;
    }

    public void setTraining(int training) {
        this.training = training;
    }

    public int getVerlof() {
        return verlof;
    }

    public void setVerlof(int verlof) {
        this.verlof = verlof;
    }

    public int getZiek() {
        return ziek;
    }

    public void setZiek(int ziek) {
        this.ziek = ziek;
    }

    public int getOverig() {
        return overig;
    }

    public void setOverig(int overig) {
        this.overig = overig;
    }

    public String getVerklaring() {
        return verklaring;
    }

    public void setVerklaring(String verklaring) {
        this.verklaring = verklaring;
    }

    public JSONObject getJSON() {
        JSONObject jsonUrenPerDag = new JSONObject();
        try {
            jsonUrenPerDag.put("id", getId());
            jsonUrenPerDag.put("datum", getDatum().toString());
            jsonUrenPerDag.put("opdracht", getOpdracht());
            jsonUrenPerDag.put("overwerk", getOverwerk());
            jsonUrenPerDag.put("training", getTraining());
            jsonUrenPerDag.put("verlof", getVerlof());
            jsonUrenPerDag.put("ziek", getZiek());
            jsonUrenPerDag.put("overig", getOverig());
            jsonUrenPerDag.put("verklaringOverig", getVerklaring());
        } catch (JSONException e) {
            jsonUrenPerDag = null;
        }
        return jsonUrenPerDag;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getWeekdag(LocalDate datum){
        String weekdag = "";
        switch (datum.getDayOfWeek()) {
            case MONDAY:
                weekdag = "maandag";
                break;
            case TUESDAY:
                weekdag = "dinsdag";
                break;
            case WEDNESDAY:
                weekdag = "woensdag";
                break;
            case THURSDAY:
                weekdag = "donderdag";
                break;
            case FRIDAY:
                weekdag = "vrijdag";
                break;
            case SATURDAY:
                weekdag = "zaterdag";
                break;
            case SUNDAY:
                weekdag = "zondag";
                break;
        }
        return weekdag;
    }
}
