package com.example.and_questionaire.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_questionaire.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class LauncherActivity extends AppCompatActivity {

    private TextView appName;

    public static List<String> sList = new ArrayList<>();
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        appName = findViewById(R.id.appName);

        //Typeface = ResourcesCompat.getFont()

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.defanim);
        appName.setAnimation(anim);

        fStore = FirebaseFirestore.getInstance(); //
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                    //sleep(2500);
                    loadData();


                // Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
               // startActivity(intent);
            }
        }).start();
    }

    private void loadData()
    {
        sList.clear(); // To start fresh
        fStore.collection("Questionnaire").document("Selections").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists())
                            {
                                long amount = (long)doc.get("AMOUNT");
                                for(int i = 1; i <= amount; i++) // 0 -> 1
                                {
                                    String selName = doc.getString("SEL" + String.valueOf(i));
                                    sList.add(selName); // Storing selections
                                }

                                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                                startActivity(intent);
                                LauncherActivity.this.finish();
                            }
                            else
                            {   //if no selections were found
                                Toast.makeText(LauncherActivity.this, "No selection documents were found",Toast.LENGTH_SHORT);
                            }
                        }
                        else
                        {
                            //Notify if fail
                            Toast.makeText(LauncherActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
}