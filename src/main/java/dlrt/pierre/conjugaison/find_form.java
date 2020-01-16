package dlrt.pierre.conjugaison;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class find_form extends AppCompatActivity {

    public static final String TAG = find_form.class.getSimpleName();

    private EditText editForm;
    private String selVerb;
    private List<Integer> CountList;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_form);

        editForm = findViewById(R.id.editTextForm);

        initActivity();
        //randomVerbSel();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: max = "+max);
                if (max > 0) {
                    ((TextView) findViewById(R.id.textViewC)).setText(selVerb);
                    Log.d(TAG, "onClick: selverb:"+selVerb);
                    if (editForm.getText().toString().trim().toLowerCase().equals(selVerb.toLowerCase())) {
                        ((TextView) findViewById(R.id.textViewC)).setTextColor(getResources().getColor(R.color.colorCorrect));
                        incCpt();
                    } else {
                        ((TextView) findViewById(R.id.textViewC)).setTextColor(getResources().getColor(R.color.colorWrong));
                        ((TextView) findViewById(R.id.textViewPts)).setText("0");
                    }
                    editForm.setText("");
                    randomVerbSel();
                }
                else {
                    Toast.makeText(getApplicationContext(),"You have to input a verb", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void incCpt() {
        int crt = Integer.valueOf(((TextView) findViewById(R.id.textViewPts)).getText().toString());
        ((TextView) findViewById(R.id.textViewPts)).setText(String.valueOf(crt+1));
    }

    public int getMax() {
        return max;
    }

    /**
     * randomVerbSel()
     *
     * @var CounterList is a list of the number of input tenses for each verb
     * @var max is the total number of input tenses of every verbs
     * @var i is the variable looking for the index of the verb designated by rdn
     *
     * The while condition checks if the randomly generated number is included in the section
     * designated by i. If yes, the index is found. If no, it jumps to the next section by adding
     * the size of the next section.
     *
    **/

    private void randomVerbSel() {

        Random r = new Random();
        int tot = CountList.get(0), i = 0;
        int rnb = r.nextInt(max)+1;
        while (tot < rnb) {
            i++;
            tot += CountList.get(i);
        }
        getVrbInstance(i);
    }

    private String randomForm(Verb v) {
        Random r = new Random();
        int rdnt = r.nextInt(10), rdnp =  r.nextInt(6);

        while(v.getTemps(rdnt).getIo().equals("")){
            rdnt  = r.nextInt(10);
        }

        ((TextView) findViewById(R.id.textViewV)).setText(v.getVerb());
        ((TextView) findViewById(R.id.textViewT)).setText(v.dispTps(rdnt));
        ((TextView) findViewById(R.id.textViewP)).setText(v.getTemps(rdnt).dispPers(rdnp));

        if (rdnt==8 || rdnt==9){
            ((TextView) findViewById(R.id.textViewP)).setVisibility(View.GONE);
        }
        else {
            ((TextView) findViewById(R.id.textViewP)).setVisibility(View.VISIBLE);
        }

        return v.getTemps(rdnt).getPers(rdnp);
    }

    private int getTotalEntry() {
        int tot = 0;
        for (int i = 0; i < CountList.size(); i++) {
            int temp = CountList.get(i);
            tot += temp;
        }
        return tot;
    }

    private void getVrbInstance(Integer verbNum) {

        class GetVerbInstance extends AsyncTask<Integer, Void, Verb> {
            @Override
            protected Verb doInBackground(Integer... ints) {
                List<Integer> verbIds = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().getAllId();
                Verb selVrb = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().getVerbFromId(verbIds.get(ints[0]));
                return selVrb;
            }
            @Override
            protected void onPostExecute(Verb vrb) {
                super.onPostExecute(vrb);
                selVerb = randomForm(vrb);
            }
        }
        GetVerbInstance gvi = new GetVerbInstance();
        gvi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,verbNum);
    }

    private void initActivity() {

        class GetVerbs extends AsyncTask<Void, Void, List<Integer>> {
            @Override
            protected List<Integer> doInBackground(Void... voids) {
                List<Integer> list = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().verbDao().getAllfil();
                return list;
            }
            @Override
            protected void onPostExecute(List<Integer> list) {
                super.onPostExecute(list);
                setList(list);
                max = getTotalEntry();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editForm, InputMethodManager.SHOW_IMPLICIT); // Display soft keyboard
                if (max > 0){
                    randomVerbSel();
                }
            }
        }
        GetVerbs gv = new GetVerbs();
        gv.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    void setList(List<Integer> l){ CountList = l; }
}
