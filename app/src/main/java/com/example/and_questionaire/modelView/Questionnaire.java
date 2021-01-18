package com.example.and_questionaire.modelView;

public class Questionnaire
{
    public int correctAns;
    String question;
    String answer1;
    String answer2;
    String answer3;
    String answer4;

    public Questionnaire(String question, String answer1, String answer2, String answer3, String answer4, int correctAns) {
        this.correctAns = correctAns;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }

    public int getCorrectAns() { return correctAns; }


    public String getQuestion() { return question; }


    public String getAnswer1() { return answer1; }


    public String getAnswer2() { return answer2; }


    public String getAnswer3() { return answer3; }


    public String getAnswer4() { return answer4; }

}
