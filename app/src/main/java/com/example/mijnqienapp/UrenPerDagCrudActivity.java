package com.example.mijnqienapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mijnqienapp.domain.UrenPerDag;
import com.example.mijnqienapp.service.UrenPerDagGetUrenPerDag;
import com.example.mijnqienapp.service.UrenPerDagSaveUrenPerDag;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class UrenPerDagCrudActivity extends AppCompatActivity {
    public final static String UPD_CMD = "UPD_CMD";
    public final static String UPD_ID = "UPD_ID";

    public final static int UPD_CREATE = 201;
    public final static int UPD_RETRIEVE = 202;
    public final static int UPD_UPDATE = 203;
    public final static int UPD_DELETE = 204;
    public final static int UPD_OK = 1;
    public final static int UPD_NOK = -1;
    public static final String UPD_RETURNMSG = "bericht";


    EditText edtOpdracht;
    EditText edtOverwerk;
    EditText edtTraining;
    EditText edtVerlof;
    EditText edtZiek;
    EditText edtOverig;
    EditText edtOverigUitleg;
    TextView headerUPD;
    Button btnSave;
    Button btnCancel;

    int mCommand;
    UrenPerDag mUrenPerDag;    // Ingeval van wijzigen/verwijderen de auto waarvan de gegevens gewijzigd moeten worden
    long mUrenPerDagId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urenperdag);

        // Get command and key from intent
        mCommand = getIntent().getIntExtra(UPD_CMD, -1);
        mUrenPerDagId = getIntent().getLongExtra(UPD_ID, -1);
        mUrenPerDag = null;

        switch (mCommand) {
            case UPD_CREATE :
                if (mUrenPerDagId != -1) {
                    finish(getString(R.string.msgOngeldigArgument));
                };
                break;
            case UPD_RETRIEVE:
            case UPD_DELETE:
            case UPD_UPDATE :
                if (mUrenPerDagId == -1) {
                    finish(getString(R.string.msgOngeldigArgument));
                };
                try {
                    mUrenPerDag = new UrenPerDagGetUrenPerDag(mUrenPerDagId).execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (mUrenPerDag==null) {
                        finish(getString(R.string.msgAutoBestaatNiet));
                    }
                }
                break;
            default:
                finish(getString(R.string.msgOngeldigeOpdracht));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initApp() {
        edtOpdracht = (EditText) findViewById(R.id.edtOpdracht);
        edtOverwerk = (EditText) findViewById(R.id.edtOverwerk);
        edtTraining = (EditText) findViewById(R.id.edtTraining);
        edtVerlof = (EditText) findViewById(R.id.edtVerlof);
        edtZiek = (EditText) findViewById(R.id.edtZiek);
        edtOverig = (EditText) findViewById(R.id.edtOverig);
        edtOverigUitleg = (EditText) findViewById(R.id.edtOverigUitleg);
        headerUPD = (TextView) findViewById(R.id.headerUPD);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        if (mUrenPerDag != null) {
            headerUPD.setText(UrenPerDag.getWeekdag(mUrenPerDag.getDatum()) + " " + mUrenPerDag.getDatum().getDayOfMonth() );
            edtOpdracht.setText(Integer.toString(mUrenPerDag.getOpdracht()));
            edtOverwerk.setText(Integer.toString(mUrenPerDag.getOverwerk()));
            edtTraining.setText(Integer.toString(mUrenPerDag.getTraining()));
            edtVerlof.setText(Integer.toString(mUrenPerDag.getVerlof()));
            edtZiek.setText(Integer.toString(mUrenPerDag.getZiek()));
            edtOverig.setText(Integer.toString(mUrenPerDag.getOverig()));
            edtOverigUitleg.setText(mUrenPerDag.getVerklaring());;
        }

        if (mCommand == UPD_RETRIEVE || mCommand == UPD_DELETE) {
            edtOpdracht.setEnabled(false);
            edtOverwerk.setEnabled(false);
            edtTraining.setEnabled(false);
            edtVerlof.setEnabled(false);
            edtZiek.setEnabled(false);
            edtOverig.setEnabled(false);
            edtOverigUitleg.setEnabled(false);
        }

        if (mCommand == UPD_CREATE) {
            btnSave.setText(R.string.btnToevoegen);
        } else if (mCommand == UPD_RETRIEVE) {
            btnSave.setVisibility(View.GONE);
            btnCancel.setText(R.string.btnOk);
        } else if (mCommand == UPD_UPDATE) {
            btnSave.setText(R.string.btnOpslaan);
        } else {
            btnSave.setText(R.string.btnVerwijderen);
        }
    }

    @Override
    public void finish() {
        setResult(UPD_OK);
        super.finish();
    }

    private void finish(String string) {
        // Overload finish, om via een intent een bericht aan het aanroepen programma terug
        // te geven (in plaats van het bericht te toasten). We geven hier ook een andere
        // result-code door, om te laten zien dat dit in het aanroepend programma zichtbaar
        // wordt.

//        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
//        setResult(AUTO_NOK);

        Intent bericht = new Intent();
        bericht.putExtra(UPD_RETURNMSG, string);
        setResult(UPD_NOK, bericht);

        super.finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btnSave(View view) {
        try {
            switch (mCommand) {
                case UPD_CREATE:
                    if (!invoerOk()) return;
                    mUrenPerDag = new UrenPerDag(0, LocalDate.now(),
                            Integer.parseInt(edtOpdracht.getText().toString()),
                            Integer.parseInt(edtOverwerk.getText().toString()),
                            Integer.parseInt(edtTraining.getText().toString()),
                            Integer.parseInt(edtVerlof.getText().toString()),
                            Integer.parseInt(edtZiek.getText().toString()),
                            Integer.parseInt(edtOverig.getText().toString()),
                            edtOverigUitleg.getText().toString());
                    new UrenPerDagSaveUrenPerDag(mUrenPerDag, UPD_CREATE).execute();
                    break;
                case UPD_UPDATE:
                    if (!invoerOk()) return;
                    mUrenPerDag.setOpdracht(Integer.parseInt(edtOpdracht.getText().toString()));
                    mUrenPerDag.setOverwerk(Integer.parseInt(edtOverwerk.getText().toString()));
                    mUrenPerDag.setTraining(Integer.parseInt(edtTraining.getText().toString()));
                    mUrenPerDag.setVerlof(Integer.parseInt(edtVerlof.getText().toString()));
                    mUrenPerDag.setZiek(Integer.parseInt(edtZiek.getText().toString()));
                    mUrenPerDag.setOverig(Integer.parseInt(edtOverig.getText().toString()));
                    mUrenPerDag.setVerklaring(edtOverigUitleg.getText().toString());
                    new UrenPerDagSaveUrenPerDag(mUrenPerDag, UPD_UPDATE).execute();
                    break;
                case UPD_DELETE:
                    new UrenPerDagSaveUrenPerDag(mUrenPerDag, UPD_DELETE).execute();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }

    private boolean invoerOk() {
        boolean error = false;
        boolean anythingChanged = false;

        if (edtOpdracht.getText().toString().isEmpty()){
            edtOpdracht.setError(getString(R.string.msgOpdrachtInvullen));
            error = true;
        } else if (mUrenPerDag==null
                || !edtOpdracht.getText().toString().equals(Integer.toString(mUrenPerDag.getOpdracht()))) {
            anythingChanged =true;
        }

        if (edtOverwerk.getText().toString().isEmpty()){
            edtOverwerk.setError(getString(R.string.msgOverwerkInvullen));
            error = true;
        } else if (mUrenPerDag==null
                || !edtOverwerk.getText().toString().equals(Integer.toString(mUrenPerDag.getOverwerk()))) {
            anythingChanged =true;
        }

        if (edtTraining.getText().toString().isEmpty()){
            edtTraining.setError(getString(R.string.msgTrainingInvullen));
            error = true;
        } else if (mUrenPerDag==null
                || !edtTraining.getText().toString().equals(Integer.toString(mUrenPerDag.getTraining()))) {
            anythingChanged =true;
        }

        if (edtVerlof.getText().toString().isEmpty()){
            edtVerlof.setError(getString(R.string.msgVerlofInvullen));
            error = true;
        } else if (mUrenPerDag==null
                || !edtVerlof.getText().toString().equals(Integer.toString(mUrenPerDag.getVerlof()))) {
            anythingChanged =true;
        }

        if (edtZiek.getText().toString().isEmpty()){
            edtZiek.setError(getString(R.string.msgZiekInvullen));
            error = true;
        } else if (mUrenPerDag==null
                || !edtZiek.getText().toString().equals(Integer.toString(mUrenPerDag.getZiek()))) {
            anythingChanged =true;
        }

        if (edtOverig.getText().toString().isEmpty()){
            edtOverig.setError(getString(R.string.msgOverigInvullen));
            error = true;
        } else if (mUrenPerDag==null
                || !edtOverig.getText().toString().equals(Integer.toString(mUrenPerDag.getOverig()))) {
            anythingChanged =true;
        }

        if (edtOverigUitleg.getText().toString().isEmpty()){
            if (mUrenPerDag !=null && !mUrenPerDag.getVerklaring().isEmpty()) {
                anythingChanged = true;
            }
        } else {
            if (mUrenPerDag !=null && mUrenPerDag.getVerklaring().equals(edtOverigUitleg.getText().toString())) {
                anythingChanged = true;
            }
        }

        if (!error && !anythingChanged) {
            edtOpdracht.setError(getString(R.string.msgUPDNietsGewijzigd));
            edtOverwerk.setError(getString(R.string.msgUPDNietsGewijzigd));
            edtTraining.setError(getString(R.string.msgUPDNietsGewijzigd));
            edtVerlof.setError(getString(R.string.msgUPDNietsGewijzigd));
            edtZiek.setError(getString(R.string.msgUPDNietsGewijzigd));
            edtOverwerk.setError(getString(R.string.msgUPDNietsGewijzigd));
            edtOverigUitleg.setError(getString(R.string.msgUPDNietsGewijzigd));
            error = true;
        }
        return !error;
    }

    public void btnCancel(View view) {
        finish();
    }
    @Override
    public void onBackPressed() {
        finish(getString(R.string.actieGeannuleerd));
    }
}