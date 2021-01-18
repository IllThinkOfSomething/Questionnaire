package com.example.and_questionaire.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_questionaire.R;
import com.example.and_questionaire.model.adapter.PackAdapter;
import com.example.and_questionaire.modelView.Questionnaire;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.and_questionaire.views.PackActivity.selectionId;

public class QuestionnaireActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView counter;
    private TextView numOfQuestion;

    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;

    private List<Questionnaire> qList;
    private CountDownTimer cdTimer;
    private int questNum;
    private int results;

    private FirebaseFirestore fStore;
    private int packNumber;
    private Dialog loadDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        question = findViewById(R.id.questionnaire_question_text);
        counter = findViewById(R.id.questionnaire_counter);
        numOfQuestion = findViewById(R.id.questionnaire_count_text);

        ans1 = findViewById(R.id.questionnaire_answer_1);
        ans2 = findViewById(R.id.questionnaire_answer_2);
        ans3 = findViewById(R.id.questionnaire_answer_3);
        ans4 = findViewById(R.id.questionnaire_answer_4);

        ans1.setOnClickListener(this);
        ans2.setOnClickListener(this);
        ans3.setOnClickListener(this);
        ans4.setOnClickListener(this);

        fStore = FirebaseFirestore.getInstance();

        loadDialog = new Dialog(QuestionnaireActivity.this);
        loadDialog.setContentView(R.layout.sets_loading);
        loadDialog.setCancelable(false);
        loadDialog.getWindow().setBackgroundDrawableResource(R.drawable.bar_background);
        loadDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadDialog.show();


        packNumber = getIntent().getIntExtra("PACK_NR", 1);

        getQuestionList();

        results = 0;
    }

    private void getQuestionList()
    {
        qList = new ArrayList<>();

        //Getting all questions collection from FireStore

        fStore.collection("Questionnaire").document("SEL" + String.valueOf(selectionId))
                .collection("PACK" + String.valueOf(packNumber))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()) {
                    QuerySnapshot quests = task.getResult();
                    for (QueryDocumentSnapshot doc : quests)
                    {
                        qList.add(new Questionnaire(doc.getString("QUESTION"),
                                doc.getString("1"),
                                doc.getString("2"),
                                doc.getString("3"),
                                doc.getString("4"),
                                Integer.valueOf(doc.getString("ANSWER"))
                        ));
                    }

                    setQuestionnaire();
                }
                else
                {
                    //Notify if fail
                    Toast.makeText(QuestionnaireActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                }
                loadDialog.cancel();
            }
        });


    }

    private void setQuestionnaire()
    {
        counter.setText(String.valueOf(15));
        question.setText(qList.get(0).getQuestion());
        ans1.setText(qList.get(0).getAnswer1());
        ans2.setText(qList.get(0).getAnswer2());
        ans3.setText(qList.get(0).getAnswer3());
        ans4.setText(qList.get(0).getAnswer4());
        numOfQuestion.setText(String.valueOf(1) + "/ " + String.valueOf(qList.size()));

        startCounter();

        questNum = 0;
    }

    private void startCounter() // This starts and updates timer for SetQuestion func
    {
        cdTimer = new CountDownTimer(15600, 1000) // After 1 sec the counter will be updated
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                if(millisUntilFinished < 15600)
                    counter.setText(String.valueOf(millisUntilFinished / 1000)); // left time in seconds
            }

            @Override
            public void onFinish()
            {
                nextQuestion();
            }
        };
        cdTimer.start();
    }


    @Override
    public void onClick(View v)
    {
        int selectedAnswer = 0;

        switch (v.getId()) //Checked the clicked button
        {
            case R.id.questionnaire_answer_1:
                selectedAnswer = 1;
                break;
            case R.id.questionnaire_answer_2:
                selectedAnswer = 2;
                break;
            case R.id.questionnaire_answer_3:
                selectedAnswer = 3;
                break;
            case R.id.questionnaire_answer_4:
                selectedAnswer = 4;
                break;
            default:
        }
        cdTimer.cancel();
        checkAnswer(selectedAnswer, v);
    }

    private void checkAnswer(int selectedAnswer, View v)
    {
        if(selectedAnswer == qList.get(questNum).correctAns)
        {
            //Correct
            ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            results++;
        }
        else
        {
            //Wrong
            ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            //Show Correct
            switch (qList.get(questNum).getCorrectAns())
            {
                case 1: ans1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
                case 2: ans2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
                case 3: ans3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
                case 4: ans4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 2000);
        nextQuestion();
    }

    private void nextQuestion()
    {
        if(questNum < qList.size() - 1)
        {
            questNum++; //Question Pointer
            playAnimation(0, question, 0);
            playAnimation(0, ans1, 1);
            playAnimation(0, ans2, 2);
            playAnimation(0, ans3, 3);
            playAnimation(0, ans4, 4);

            // After Question change Question and QuestionCounter update

            numOfQuestion.setText((questNum+1) + "/ " + String.valueOf(qList.size()));

            counter.setText(String.valueOf(15));
            startCounter();

        }
        else
        {
            // Go to score activity
            Intent intent = new Intent(QuestionnaireActivity.this, ResultsActivity.class); //Passing score to intent
            intent.putExtra("Results", String.valueOf(results) + "/ " + String.valueOf(qList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Instead of finish
            startActivity(intent);
            //QuestionnaireActivity.this.finish();
        }
    }

    private void playAnimation(final int value, View v, int viewNumber)
    {
        v.animate().alpha(value).scaleX(value).scaleY(value).setDuration(1000).setStartDelay(100).
                setInterpolator(new DecelerateInterpolator()).
                setListener(new Animator.AnimatorListener()  //Shrink question and bring it back
                {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {


                if(value == 0)
                {
                    switch (viewNumber)
                    {
                        case 0: ((TextView)v).setText(qList.get(questNum).getQuestion()); break;
                        case 1: ((Button)v).setText(qList.get(questNum).getAnswer1()); break;
                        case 2: ((Button)v).setText(qList.get(questNum).getAnswer2()); break;
                        case 3: ((Button)v).setText(qList.get(questNum).getAnswer3()); break;
                        case 4: ((Button)v).setText(qList.get(questNum).getAnswer4()); break;

                    }

                    if(viewNumber != 0)
                    {
                        ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0707")));
                    }

                    playAnimation(1, v, viewNumber);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }); // Shrink the button

    }

    @Override
    public void onBackPressed() { // Solution on when getting from questionnaire back to packs and the counter is not stopping
        super.onBackPressed();

        cdTimer.cancel();
    }
}