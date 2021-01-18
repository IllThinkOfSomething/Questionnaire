package com.example.and_questionaire.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.and_questionaire.R;
import com.example.and_questionaire.model.adapter.PackAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class PackActivity extends AppCompatActivity {

    private GridView packGridView;
    private FirebaseFirestore fStore;
    public static int selectionId;

    private Dialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);

        Toolbar toolB = findViewById(R.id.pack_toolbar);
        setSupportActionBar(toolB);
        String packTitle = getIntent().getStringExtra("Selection");
        selectionId = getIntent().getIntExtra("SELECTION_ID", 1);
        getSupportActionBar().setTitle(packTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = FirebaseFirestore.getInstance();

        packGridView = findViewById(R.id.pack_grid_view);
        //Loading bar
        loadDialog = new Dialog(PackActivity.this);
        loadDialog.setContentView(R.layout.sets_loading);
        loadDialog.setCancelable(false);
        loadDialog.getWindow().setBackgroundDrawableResource(R.drawable.bar_background);
        loadDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadDialog.show();

        loadPacks();
        //In LoadData function
       // PackAdapter adapter = new PackAdapter(6);
       // packGridView.setAdapter(adapter);

    }

    private void loadPacks()
    {
        fStore.collection("Questionnaire").document("SEL" + String.valueOf(selectionId)).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists())
                            {
                                long packs = (long)doc.get("PACKS");
                                //Load the adapter
                                PackAdapter adapter = new PackAdapter((int)packs);
                                packGridView.setAdapter(adapter);

                                loadDialog.cancel(); //Cancel Loading Bar
                            }
                            else
                            {   //if no selections were found
                                Toast.makeText(PackActivity.this, "No selection document were found",Toast.LENGTH_SHORT);
                            }
                        }
                        else
                        {
                            //Notify if fail
                            Toast.makeText(PackActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            PackActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}