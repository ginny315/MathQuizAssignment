/**
 * Created by guoningyan on 2017/10/17.
 */
package com.example.guoningyan.mathquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Random;


public class QuestionActivity extends Activity implements View.OnClickListener{
    EditText answer1,answer2;
    Button btn_submit,btn_next;
    String answer1_text,answer2_text;
    TextView showAnswer,showQuestion,showQuestionNum;
    double ans1,ans2; //standard answer
    Spinner spin;
    int cnt = 1;
    int ansCorrect = 0, ansWrong = 0, ansSkip = 10 ;
    int ansSkipOne = 0, ansSkipTwo = 0;
    boolean hasAns = false;
    Double timeStart = 0.0, timeEnd = 0.0, durationOne = 0.0, durationTwo = 0.0;
    boolean oneRoot = false;// quadratic has only one root

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_question);

        //TODO:actions after switch the page
        //Intent intent = this.getIntent();
        timeStart = Double.valueOf(System.currentTimeMillis());

        showAnswer = (TextView)findViewById(R.id.txt_showAnswer);
        showQuestion = (TextView)findViewById(R.id.txt_showQuestion);
        showQuestionNum = (TextView)findViewById(R.id.txt_showQuestionNum);
        answer1 = (EditText)findViewById(R.id.txt_Answer1);
        answer2 = (EditText)findViewById(R.id.txt_Answer2);
        spin = (Spinner)findViewById(R.id.spinner_roundAnswer);

        showQuestionNum.setText("Question "+cnt+":");
        showQuestion.setText(newQuestion(1));

        //submit
        btn_submit = (Button)findViewById(R.id.btn_Submit);
        btn_next = (Button)findViewById(R.id.btn_Next);
        btn_submit.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        answer2.setVisibility(View.INVISIBLE);
        showAnswer.setVisibility(View.INVISIBLE);

        numberLimit(answer1);
        numberLimit(answer2);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_Submit) {
            if(cnt<=5 || oneRoot == true) {
                if(answer1.getText() != null && answer1.getText().toString() != "" && answer1.getText().toString().trim().length() > 0) {
                    answer1_text = answer1.getText().toString();
                    try {
                        Float.parseFloat(answer1_text);

                        if(checkDecimal(answer1_text))
                            return;
                        else{
                            btn_submit.setEnabled(false);
                            showAnswer.setVisibility(View.VISIBLE);
                            hasAns = true;
                            timeEnd = Double.valueOf(System.currentTimeMillis());
                            if(cnt<=5) {
                                durationOne = durationOne + (timeEnd - timeStart);
                            }else {
                                durationTwo = durationTwo + (timeEnd - timeStart);
                            }
                            if (areEqual(Double.valueOf(answer1_text), ans1)) {
                                showAnswer.setText("Correct!");
                                ansCorrect ++ ;
                            } else {
                                showAnswer.setText("Sorry, you're Wrong! The correct answer is " + ans1);
                                ansWrong ++ ;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("---------------------wrong-------------------");
                        System.out.println(e);
                        initPrompt("Warning","The answer you input is not format! Try again!");
                        btn_submit.setEnabled(true);
                    }
                }else{
                    System.out.println("-----------------input null---------------");
                    initPrompt("Warning","You didn't submit anything. You may click 'Next' to answer the following question.");
                    //showPrompt("please input your answer!");
                }
            }else { //quadratic
                if (answer1.getText() != null && answer1.getText().toString() != "" && answer1.getText().toString().trim().length() > 0 &&
                        answer2.getText() != null && answer2.getText().toString() != "" && answer2.getText().toString().trim().length() > 0) {
                    answer1_text = answer1.getText().toString();
                    answer2_text = answer2.getText().toString();
                    try {
                        Float.parseFloat(answer1_text);
                        Float.parseFloat(answer2_text);

                        if(checkDecimal(answer1_text) && checkDecimal(answer2_text))
                            return;
                        else{
                            btn_submit.setEnabled(false);
                            showAnswer.setVisibility(View.VISIBLE);
                            hasAns = true;
                            timeEnd = Double.valueOf(System.currentTimeMillis());
                            durationTwo = durationTwo + (timeEnd - timeStart);
                            if (areEqual(Double.valueOf(answer1_text), ans1) && areEqual(Double.valueOf(answer2_text), ans2) ||
                                    areEqual(Double.valueOf(answer1_text), ans2) && areEqual(Double.valueOf(answer2_text), ans1)) {
                                showAnswer.setText("Correct!");
                                ansCorrect ++ ;
                            } else {
                                showAnswer.setText("Sorry, you're Wrong! The correct answer is " + ans1 + " and " + ans2);
                                ansWrong ++ ;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                        initPrompt("Warning","The answer you input is not format! Try again!");
                        btn_submit.setEnabled(true);
                    }
                }else {
                    System.out.println("-----------------input null---------------");
                    initPrompt("Warning","You should provide two answers. You may click 'Next' to answer the following question.");
                }
            }
        } else if (v.getId() == R.id.btn_Next){ //show next question
            showAnswer.setVisibility(View.INVISIBLE);
            btn_submit.setEnabled(true);
            answer1.setText("");
            answer2.setText("");
            timeStart = Double.valueOf(System.currentTimeMillis());//start to record time
            if(cnt<5){
                if(!hasAns)
                    ansSkipOne ++;
                showQuestion.setText(newQuestion(1));
                cnt++;
                showQuestionNum.setText("Question "+cnt+":");
                System.out.println("++++++++++cnt="+cnt+"------ansSkipOne"+ansSkipOne+"++++++++++cnt="+cnt+"-------ansSkipTwo"+ansSkipTwo);
            }else if(cnt<10){
                if(!hasAns && cnt > 5)
                    ansSkipTwo ++;
                else if(!hasAns && (cnt == 5))
                    ansSkipOne ++;
                else
                    ;
                showQuestion.setText(newQuestion(2));
                cnt++;
                showQuestionNum.setText("Question "+cnt+":");
                if(oneRoot)
                    answer2.setVisibility(View.INVISIBLE);
                else
                    answer2.setVisibility(View.VISIBLE);
                System.out.println("++++++++++cnt="+cnt+"------ansSkipOne"+ansSkipOne+"++++++++++cnt="+cnt+"-------ansSkipTwo"+ansSkipTwo);
            }else{//end the quiz
                if(!hasAns)
                    ansSkipTwo ++;
                System.out.println("-----------durationOne="+durationOne+"--------ansSkipOne="+ansSkipOne+
                        "-----------durationOne / ansSkipOne="+durationOne / ansSkipOne+
                        "-----------durationTwo / ansSkipTwo="+durationTwo / ansSkipTwo+
                        "-----------durationTwo="+durationTwo+"---------ansSkipTwo="+ansSkipTwo);
                double duration1 = ansSkipOne !=5 ? durationOne/(5-ansSkipOne) : 0.0;
                double duration2 = ansSkipTwo !=5 ? durationTwo/(5-ansSkipTwo) : 0.0;
                Intent intent = new Intent(getBaseContext(), GradeActivity.class);
                intent.putExtra("ansCorrect",ansCorrect);
                intent.putExtra("ansWrong",ansWrong);
                intent.putExtra("ansSkip",ansSkip - ansCorrect - ansWrong);
                intent.putExtra("duration1",duration1);
                intent.putExtra("duration2",duration2);
                System.out.println("-----------------------jump---------------------");
                startActivity(intent);
            }
            hasAns = false;
        } else{}
    }

    //occur random num
    public int randomNum(){
        Random myRandom = new Random();
        int r = -99 + myRandom.nextInt(197);
        return r;
    }

    //give the question
    public String newQuestion(int n){
        int a,b,c;
        String q = null;

        if(n == 1){ //linear equation
            a = randomNum();
            b = randomNum();
            q = b >= 0 ? formatNum(a) + "X + " + String.valueOf(b) + " = 0": formatNum(a) + "X - " + String.valueOf(-b) + " = 0";
            //2 decimal places
            double temp = -(double)b/(double)a;
            BigDecimal bg = new BigDecimal(temp);
            ans1 = bg.setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
            System.out.println("------------------------q="+q+"------"+a+"------"+b+"------"+temp+"--------ans1="+ans1);
        }
        else{ //quadratic equation
            do{
                a = randomNum();
                b = randomNum();
                c = randomNum();
            }while(Math.pow(b,2) - 4*a*c < 0 || a==0);


            if(b > 1){
                if(c > 0){
                    q = formatNum(a) + "X^2 + " + formatNum(b) + "X + "+formatNum(c)+" = 0";
                }else if(c == 0){
                    q = formatNum(a) + "X^2 + " + formatNum(b) + "X = 0";
                }else{//c<0
                    q = formatNum(a) + "X^2 + " + formatNum(b) + "X - "+formatNum(-c)+" = 0";
                }
            }else if(b == 0){
                if(c > 0){
                    q = formatNum(a) + "X^2 + " + String.valueOf(c)+" = 0";
                }else if(c == 0){
                    q = formatNum(a) + "X^2 = 0";
                }else{//c<0
                    q = formatNum(a) + "X^2 + " + String.valueOf(-c)+" = 0";
                }
            }else{//b<0
                if(c > 0){
                    q = formatNum(a) + "X^2 - " + formatNum(-b) + "X + "+formatNum(c)+" = 0";
                }else if(c == 0){
                    q = formatNum(a) + "X^2 - " + formatNum(-b) + "X = 0";
                }else{//c<0
                    q = formatNum(a) + "X^2 - " + formatNum(-b) + "X - "+formatNum(-c)+" = 0";
                }
            }
            double temp = Math.pow(b,2) - 4*a*c;
            if(temp == 0)
                oneRoot = true;
            else
                oneRoot = false;
            BigDecimal bg1 = new BigDecimal((-(double)b+Math.sqrt(temp))/(2*(double)a));
            ans1 = bg1.setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
            BigDecimal bg2 = new BigDecimal((-(double)b-Math.sqrt(temp))/(2*(double)a));
            ans2 = bg2.setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
            System.out.println("------------------------q="+q+"-------000temp"+temp+"--------"+Math.sqrt(temp)+"--------"+"--------"+"--------ans1="+ans1+"--------ans2="+ans2);
        }
        return q;
    }

    //compare the input answer with the correct one
    boolean areEqual(double A, double B){
        System.out.println("---------A="+A+"---------B="+B+"--------ans1="+ans1+"--------ans2="+ans2);
        if(Math.abs(A - B) < Math.pow(10,-6))
            return true;
        return false;
    }

    String formatNum(int n){
        String tn;
            if(n == 1)
                tn = "";
            else if(n == -1)
                tn = "-";
            else
                tn = String.valueOf(n);
        return tn;
    }

    void initPrompt(String txt, String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View  dialog = inflater.inflate(R.layout.prompt,(ViewGroup)findViewById(R.id.dialog));
        //final EditText editText = (EditText) dialog.findViewById(R.id.et);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView message = (TextView)dialog.findViewById(R.id.tv);
        message.setText(msg);
        builder.setTitle(txt);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(QuestionActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(dialog);
        builder.setIcon(R.drawable.logo);
        builder.show();
    }

    void numberLimit(final EditText num){
        num.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String text = s.toString();
                if (text.contains(".")) {
                    int index = text.indexOf(".");
                    if (index + 3 < text.length()) {
                        initPrompt("Warning","If your answers are not integers, please round them to 2 decimal places.");
//                        text = text.substring(0, index + 3);
//                        num.setText(text);
//                        num.setSelection(text.length());
                    }
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}

            public void afterTextChanged(Editable s) {}
        });
    }

    boolean checkDecimal(String text){
        Boolean ifFormat = false;
        if (text.contains(".")) {
            int index = text.indexOf(".");
            if (index + 3 < text.length()) {
                initPrompt("Warning","If your answers are not integers, please round them to 2 decimal places.");
                ifFormat = true;
            }
        }
        return ifFormat;
    }
}
