package com.example.and_questionaire.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.and_questionaire.R;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //title = findViewById(R.id.mainActivityName);
        start = findViewById(R.id.mainActivityStartButton);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_selection.class);
                startActivity(intent);                                                                       // Redirect to Selection activity
            }
        });
    }

}