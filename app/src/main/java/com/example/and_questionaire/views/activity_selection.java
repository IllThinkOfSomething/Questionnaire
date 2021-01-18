package com.example.and_questionaire.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.and_questionaire.R;
import com.example.and_questionaire.model.adapter.SelectionGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.and_questionaire.views.LauncherActivity.sList;


public class activity_selection extends AppCompatActivity
{
    private GridView selectionGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Toolbar toolB = findViewById(R.id.selection_toolbar);
        setSupportActionBar(toolB);
        getSupportActionBar().setTitle("Selections");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectionGridView = findViewById(R.id.selection_grid_view);

        //Before FireBase
       // List<String> sList = new ArrayList<>();
       // sList.add("Games");
       // sList.add("Sports");
       //  sList.add("Programming");
       //  sList.add("Math");
       // sList.add("Science");
       // sList.add("Random");

        SelectionGridViewAdapter adapter = new SelectionGridViewAdapter(sList); //Import from LauncherActivity
        selectionGridView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            activity_selection.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}