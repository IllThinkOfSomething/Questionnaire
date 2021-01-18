package com.example.and_questionaire.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_questionaire.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResultsActivity extends AppCompatActivity {

    private Button finish;
    private TextView results;
    private static final String RESULTS_HISTORY = "results_history.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        finish = findViewById(R.id.result_finish_button);
        results = findViewById(R.id.results_result_text);

        String resultString = getIntent().getStringExtra("Results");
        results.setText(resultString);



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                ResultsActivity.this.startActivity(intent);
                ResultsActivity.this.finish();


            }
        });

        //Share txt
        Button shareTextButton = findViewById(R.id.share_button);
        shareTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FileOutputStream fOut = null; // For result local save
                String text = shareTextButton.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, resultString);
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareIntent, "Share text via:"));

                //Stores data to Data > Data > com.example.and_questionaire > files > results history (Device File Explorer)
                try {
                    fOut = openFileOutput(RESULTS_HISTORY, MODE_PRIVATE);
                    fOut.write(resultString.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


}