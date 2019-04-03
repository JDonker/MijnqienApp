package com.example.mijnqienapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mijnqienapp.domain.UrenPerDag;
import com.example.mijnqienapp.service.UrenPerDagGetMaand;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int AUTO_REQUEST_CODE = 1;
    RecyclerView urenPerDagRecyclerView;
    ArrayList<UrenPerDag> dagen = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApplicatie();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initApplicatie(){
        try {
        LocalDate nu = LocalDate.now();
            ArrayList<UrenPerDag> lijst = new ArrayList<>();
            for(int i=0;i<5;i++) {
                lijst.add(new UrenPerDag(1, nu = nu.plusDays(1), 8, 0, 0, 0, 0, 0, "hoi"));
                lijst.add(new UrenPerDag(1, nu = nu.plusDays(1), 8, 1, 0, 0, 0, 0, "hoi"));
                lijst.add(new UrenPerDag(1, nu = nu.plusDays(1), 8, 3, 0, 0, 0, 0, "hoi"));
                lijst.add(new UrenPerDag(1, nu = nu.plusDays(1), 8, 2, 0, 0, 0, 0, "hoi"));
            }
            this.dagen = lijst;

        //    try {
                // haal de autos die in de view getoond gaan worden, asynchroon op; wacht tot er
                // resultaat is
        //        this.dagen= new UrenPerDagGetMaand().execute().get();
         //   } catch (Exception e) {
         //       e.printStackTrace();
         //   }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialiseer de recycler view
        this.urenPerDagRecyclerView = (RecyclerView) findViewById(R.id.autoRecyclerView);
        this.urenPerDagRecyclerView.setHasFixedSize(true); //rij-size onafhankelijk van inhoud : performancevoordeel

        // we gebruiken een linear layout manager, want items worden vertikaal gescrolled
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.urenPerDagRecyclerView.setLayoutManager(layoutManager);

        //  Creeer de Auto adapter en geef hierbij aan de constructor de activity context
        // en de lijst met autos mee
        RecyclerView.Adapter urenPerDagRecyclerViewAdapter =
                new UrenPerDagRecycleViewAdapter(this.getApplicationContext(), this.dagen);
        // koppel de adapter tenslotte aan de recyclerview in onze layout
        this.urenPerDagRecyclerView.setAdapter(urenPerDagRecyclerViewAdapter);
    }
}
