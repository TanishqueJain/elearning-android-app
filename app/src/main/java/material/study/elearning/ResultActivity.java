package material.study.elearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView resultdeclare;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        resultdeclare = (TextView) findViewById(R.id.result_declare_text);

        result = getIntent().getStringExtra("result");

        resultdeclare.setText(result);

    }
}
