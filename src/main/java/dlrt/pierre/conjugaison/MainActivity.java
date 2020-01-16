package dlrt.pierre.conjugaison;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_add = (Button) findViewById(R.id.button_add);
        Button btn_start = (Button) findViewById(R.id.button_start);

        final Intent st_intent = new Intent(MainActivity.this, find_form.class);
        final Intent add_intent = new Intent(MainActivity.this, Add_verb.class);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(add_intent);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_intent.resolveActivityInfo(getPackageManager(), 0) != null) {
                    startActivity(st_intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"You have to input a verb", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

