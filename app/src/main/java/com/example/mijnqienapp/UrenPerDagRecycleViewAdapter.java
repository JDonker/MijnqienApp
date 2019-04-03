package com.example.mijnqienapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mijnqienapp.domain.UrenPerDag;

import java.util.ArrayList;

// Deze class beheerd het recycleview van de urenlijsten
public class UrenPerDagRecycleViewAdapter extends RecyclerView.Adapter<UrenPerDagRecycleViewAdapter.UrenPerDagHolder> {
    // definieer field die we nodig hebben binnen de Adapter
    private Context mContext;                           // context van de activity (zie Toast)
    private ArrayList<UrenPerDag> UrenPerDagList = new ArrayList<>();  // lijst met autos
    private UrenPerDag selectedUrenPerDag = null;

    public UrenPerDag getSelectedUrenPerDag() {
        return selectedUrenPerDag;
    }
    private void setSelectedAuto(UrenPerDag dag) {
        selectedUrenPerDag = dag;
    }

    public void changeTable(ArrayList<UrenPerDag> dagen) {
        this.UrenPerDagList = dagen;
    }


    public class UrenPerDagHolder extends RecyclerView.ViewHolder {
        // Objecten van de class AutoItem zijn de "rijen" in de RecyclerView. Deze class moet
        // de innerclass ViewHolder extenden.  Door de field te definieren, en deze in de
        // constructor te initialiseren, kunnen we hier naar verwijzen. We hebben hier ook het
        // een veld opgenomen, waarin we de referentie naar het Auto object unnen opnemen, dat in de
        // betreffende regel wordt getoond; dit als voorbeeld om aan te geven dat je niet alleen
        // view fielden mag opnemen
        UrenPerDag urenPerDag;              // tbv reference naar object dat wordt getoond
        View layout;            // tbv reference naar de "regel" zelf
        TextView txtDatum;
        TextView txtOpdracht;
        TextView txtOveruren;
        android.support.constraint.ConstraintLayout kaart;

        public UrenPerDagHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            txtDatum = itemView.findViewById(R.id.txtdatum);
            txtOpdracht = itemView.findViewById(R.id.txtopdracht);
            txtOveruren = itemView.findViewById(R.id.txtoveruren);
            kaart = itemView.findViewById(R.id.urenperdagkaart);

        }


    }

    public UrenPerDagRecycleViewAdapter(Context mContext, ArrayList<UrenPerDag> dagen) {
        // De constructor gebruiken we om de lijst met autos door te geven, en om de context van de
        // activity mee te geven, welke we nodig hebben om een alert te tonen (mbv Toast). De lijst is
        // bij aanroep al gevuld
        this.mContext = mContext;
        this.UrenPerDagList = dagen;
    }

    @NonNull
    @Override
    public UrenPerDagHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // onCreateViewHolder creert nieuwe views voor de regels (door layout manager)

        // creeer een nieuwe view
        LayoutInflater layoutInflater = LayoutInflater
                .from(parent.getContext());

        // set de view's size, margins, etc
        View view = layoutInflater
                .inflate(R.layout.urendag_recylcerview_item, parent, false);
        UrenPerDagHolder urenPerDagHolder = new UrenPerDagHolder(view);

        return urenPerDagHolder;
    }




    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final UrenPerDagHolder urenPerDagHolder, int position) {
        // onBindViewHolder moeten we implementeren; hier vullen we de regel met de informatie
        // van het auto object dat op positie position staat
        // NB: We kunnen hier gebruik maken van de instancevariabele txtMerk, txtType, txtBouwjaar etc,
        //     omdat we die in onze AutoItem class zo hebben gedefinieerd (zie hierna)
        //     In plaats van AutoItem zie je overigens meestal een naam als ViewHolder staan

        urenPerDagHolder.urenPerDag = UrenPerDagList.get(position);                      // de reference naar het object
        String weekdag = UrenPerDag.getWeekdag(UrenPerDagList.get(position).getDatum());

        urenPerDagHolder.txtDatum.setText(weekdag + " " +UrenPerDagList.get(position).getDatum().getDayOfMonth());
        if (weekdag.charAt(0)=='z') {

            urenPerDagHolder.kaart.setBackgroundColor(mContext.getResources().getColor(R.color.qienPaars));
            int wit = mContext.getResources().getColor(R.color.qienWit);
            urenPerDagHolder.txtDatum.setTextColor(wit);
            urenPerDagHolder.txtOpdracht.setTextColor(wit);
            urenPerDagHolder.txtOveruren.setTextColor(wit);

        }
;        urenPerDagHolder.txtOpdracht.setText("" + UrenPerDagList.get(position).getOpdracht());
        urenPerDagHolder.txtOveruren.setText(Integer.toString(UrenPerDagList.get(position).getOverwerk()));

//        // we voegen hier bij wijze van voorbeeld een onclick event listener toe. Bj een onclick
//        // geven we een alert met de class Toast, welke de context van de activity nodig heeft
//        autoHolder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, autoHolder.txtMerk.getText(), Toast.LENGTH_LONG).show();
//            }
//        });

        // Ook een longclick kennen we, we doen hier niets meem
        urenPerDagHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, urenPerDagHolder.urenPerDag.getDatum().toString() , Toast.LENGTH_SHORT).show();

                setSelectedAuto(urenPerDagHolder.urenPerDag);
                return false;           // geeft aan dat de activity nog niet is verwerkt
            }
        });

    }

    @Override
    public int getItemCount() {
        // verplichtte implementatie: het aantal regels = aantal autos in de lijst
        return UrenPerDagList.size();
    }
}
