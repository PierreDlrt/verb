package dlrt.pierre.conjugaison;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicReference;

public class Add_verb extends AppCompatActivity  {

    private EditText editIo, editTu, editLui, editNoi, editVoi, editLoro, editTextNew;
    private Spinner spinVerb;
    private int selTps;
    private Verb selVerb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_verb);

        Spinner spinTps = (Spinner) findViewById(R.id.spinnerT);
        spinVerb = (Spinner) findViewById(R.id.spinnerV);

        editIo = findViewById(R.id.editTextIo);
        editTu = findViewById(R.id.editTextTu);
        editLui = findViewById(R.id.editTextLui);
        editNoi = findViewById(R.id.editTextNoi);
        editVoi = findViewById(R.id.editTextVoi);
        editLoro = findViewById(R.id.editTextLoro);
        editTextNew = findViewById(R.id.editTextNew);

        ArrayAdapter<CharSequence> adapterTps = ArrayAdapter.createFromResource(this,
                R.array.tps_array,
                android.R.layout.simple_spinner_item);
        adapterTps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTps.setAdapter(adapterTps);

        getVerbsFromDB();
        spinTps.setSelection(0);
        spinVerb.setSelection(0);

        spinVerb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                editTextNew = findViewById(R.id.editTextNew);
                Cursor cs = (Cursor) spinVerb.getSelectedItem();
                if (i==0) {
                    editTextNew.setVisibility(View.VISIBLE);
                    emptyFields();
                }
                else{
                    editTextNew.setVisibility(View.GONE);
                    getVrbInstance(cs.getString(cs.getColumnIndex("verb")));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spinTps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selTps = i;
                if (i==8 || i==9){
                    hideFields();
                }
                else {
                    displayFields();
                }
                if (findViewById(R.id.editTextNew).getVisibility()==View.GONE){
                    updateFields();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }

        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (findViewById(R.id.editTextNew).getVisibility()==View.VISIBLE) {
                    if (editTextNew.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(),"Verb field is empty !", Toast.LENGTH_LONG).show();
                    }
                    else if (checkExistingVerb()) {
                        Toast.makeText(getApplicationContext(),"This verb is already in the list !", Toast.LENGTH_LONG).show();
                    }
                    else {
                        saveVerb();
                    }
                }
                if (findViewById(R.id.editTextNew).getVisibility()==View.GONE){
                    updateVerb(selVerb);
                }
            }
        });

        findViewById(R.id.buttonDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinVerb.getSelectedItemPosition()==0){ return;  }

                AlertDialog.Builder builder = new AlertDialog.Builder(Add_verb.this);
                builder.setTitle("Delete tense or verb ?");
                builder.setPositiveButton("Tense", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTense(selVerb);
                    }
                });
                builder.setNegativeButton("Verb", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteVerb(selVerb);
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextTense();
            }
        });
    }

    private void hideFields() {
        editTu.setVisibility(View.GONE);
        editLui.setVisibility(View.GONE);
        editNoi.setVisibility(View.GONE);
        editVoi.setVisibility(View.GONE);
        editLoro.setVisibility(View.GONE);
        findViewById(R.id.textViewIo).setVisibility(View.GONE);
        findViewById(R.id.textViewTu).setVisibility(View.GONE);
        findViewById(R.id.textViewLui).setVisibility(View.GONE);
        findViewById(R.id.textViewNoi).setVisibility(View.GONE);
        findViewById(R.id.textViewVoi).setVisibility(View.GONE);
        findViewById(R.id.textViewLoro).setVisibility(View.GONE);
    }

    private void displayFields() {
        editTu.setVisibility(View.VISIBLE);
        editLui.setVisibility(View.VISIBLE);
        editNoi.setVisibility(View.VISIBLE);
        editVoi.setVisibility(View.VISIBLE);
        editLoro.setVisibility(View.VISIBLE);
        findViewById(R.id.textViewIo).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewTu).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewLui).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewNoi).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewVoi).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewLoro).setVisibility(View.VISIBLE);
    }

    private boolean checkExistingVerb() {
        String entry = editTextNew.getText().toString().trim();
        Adapter adapter = spinVerb.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            Cursor cursor = (Cursor) adapter.getItem(i);
            if (entry.equals(cursor.getString(cursor.getColumnIndex("verb")))){
                return true;
            }
        }
        return false;
    }

    private void nextTense() {
        Spinner spinTps = (Spinner) findViewById(R.id.spinnerT);
        if (spinTps.getSelectedItemPosition()==9){
            spinTps.setSelection(0);
        }
        else {
            spinTps.setSelection(spinTps.getSelectedItemPosition()+1);
        }
    }

    private void updateFields() {

        AtomicReference<VxT> conj = new AtomicReference<>(new VxT("", "", "", "", "", ""));
        conj.set(selVerb.getTemps(selTps));

        editIo.setText(conj.get().getIo());
        editTu.setText(conj.get().getTu());
        editLui.setText(conj.get().getLui());
        editNoi.setText(conj.get().getNoi());
        editVoi.setText(conj.get().getVoi());
        editLoro.setText(conj.get().getLoro());
    }

    private void emptyFields() {

        editTextNew.setText("");
        editIo.setText("");
        editTu.setText("");
        editLui.setText("");
        editNoi.setText("");
        editVoi.setText("");
        editLoro.setText("");
    }

    private void getVerbsFromDB() {

        final String[] adapterCols = new String[]{"verb"};
        final int[] adapterRowViews = new int[]{android.R.id.text1};

        class GetVerbs extends AsyncTask<Void, Void, Cursor> {
            @Override
            protected Cursor doInBackground(Void... voids) {
                Cursor cursor = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().getAllCursorVerb();
                return cursor;
            }
            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);

                MatrixCursor matrixCursor = new MatrixCursor(new String[] {"_id", "verb"});
                matrixCursor.addRow(new Object[] {1, "Add a verb"});
                MergeCursor mergeCursor = new MergeCursor(new Cursor[]{matrixCursor,cursor});

                SimpleCursorAdapter adapterVrb = new SimpleCursorAdapter(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        mergeCursor,
                        adapterCols,
                        adapterRowViews,
                        0);

                adapterVrb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinVerb.setAdapter(adapterVrb);
            }
        }
        GetVerbs gv = new GetVerbs();
        gv.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean checkEmptyBox(String io, String tu, String lui, String noi, String voi, String loro) {

        if (io.isEmpty()) { return false; }
        if (tu.isEmpty()) { return false; }
        if (lui.isEmpty()) { return false; }
        if (noi.isEmpty()) { return false; }
        if (voi.isEmpty()) { return false; }
        if (loro.isEmpty()) { return false; }
        else { return true; }
    }

    private void saveVerb() {

        final String sEdit, sIo, sTu, sLui, sNoi, sVoi, sLoro;

        sEdit = editTextNew.getText().toString().trim();
        sIo = editIo.getText().toString().trim();

        if (selTps==8 || selTps==9) {
            sTu = editIo.getText().toString().trim();
            sLui = editIo.getText().toString().trim();
            sNoi = editIo.getText().toString().trim();
            sVoi = editIo.getText().toString().trim();
            sLoro = editIo.getText().toString().trim();
        }
        else {
            sTu = editTu.getText().toString().trim();
            sLui = editLui.getText().toString().trim();
            sNoi = editNoi.getText().toString().trim();
            sVoi = editVoi.getText().toString().trim();
            sLoro = editLoro.getText().toString().trim();
        }

        if(!checkEmptyBox(sIo, sTu, sLui, sNoi, sVoi, sLoro)){
            Toast.makeText(getApplicationContext(),"Some fields are empty !", Toast.LENGTH_SHORT).show();
            return;
        }

        final VxT newVxT = new VxT(sIo, sTu, sLui, sNoi, sVoi, sLoro);

        class SaveVerb extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Verb verb = new Verb();
                verb.setVerb(sEdit);
                for (int i = 0; i < getResources().getStringArray(R.array.tps_array).length; i++) {
                    verb.setTemps(i, new VxT("","","","","",""));
                }
                verb.setTemps(selTps,newVxT);
                verb.setFil(1);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().insert(verb);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Verb added",Toast.LENGTH_SHORT).show();
                getVerbsFromDB();
            }
        }
        SaveVerb sv = new SaveVerb();
        sv.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateVerb(final Verb verb) {

        final String uIo, uTu, uLui, uNoi, uVoi, uLoro;

        uIo = editIo.getText().toString().trim();

        if (selTps==8 || selTps==9) {
            uTu = editIo.getText().toString().trim();
            uLui = editIo.getText().toString().trim();
            uNoi = editIo.getText().toString().trim();
            uVoi = editIo.getText().toString().trim();
            uLoro = editIo.getText().toString().trim();
        }
        else {
            uTu = editTu.getText().toString().trim();
            uLui = editLui.getText().toString().trim();
            uNoi = editNoi.getText().toString().trim();
            uVoi = editVoi.getText().toString().trim();
            uLoro = editLoro.getText().toString().trim();
        }

        if(!checkEmptyBox(uIo, uTu, uLui, uNoi, uVoi, uLoro)){
            Toast.makeText(getApplicationContext(),"Some fields are empty !", Toast.LENGTH_SHORT).show();
            return;
        }

        final VxT newVxT = new VxT(uIo, uTu, uLui, uNoi, uVoi, uLoro);

        class UpdateVerb extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                if(verb.getTemps(selTps).getIo().equals("")){
                    verb.setFil(verb.getFil()+1);
                }
                verb.setTemps(selTps,newVxT);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().update(verb);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Verb updated",Toast.LENGTH_SHORT).show();
                nextTense();
            }
        }
        UpdateVerb uv = new UpdateVerb();
        uv.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void deleteTense(final Verb verb) {
        class DeleteTense extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                if (verb.getFil()-1 == 0) {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().delete(verb);
                }
                else {
                    verb.setFil(verb.getFil() - 1);
                    verb.setTemps(selTps, new VxT("", "", "", "", "", ""));
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().update(verb);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Verb updated",Toast.LENGTH_SHORT).show();
                getVerbsFromDB();
            }
        }
        DeleteTense dt = new DeleteTense();
        dt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getVrbInstance(String verbName) {

        class GetVerbInstance extends AsyncTask<String, Void, Verb> {
            @Override
            protected Verb doInBackground(String... strings) {
                Verb selVrb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().getVerb(strings[0]);
                return selVrb;
            }
            @Override
            protected void onPostExecute(Verb vrb) {
                super.onPostExecute(vrb);
                setVrb(vrb);
                updateFields();
            }
        }
        GetVerbInstance gvi = new GetVerbInstance();
        gvi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,verbName);
    }

    private void setVrb(Verb vrb) {
        selVerb = vrb;
    }

    private void deleteVerb(final Verb verb) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().delete(verb);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Verb deleted",Toast.LENGTH_SHORT).show();
                getVerbsFromDB();
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}