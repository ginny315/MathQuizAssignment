package com.example.guoningyan.mathquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity implements View.OnClickListener{
    Button btn_start;
    //EditText txt_username,txt_uno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("","123");
        System.out.println("-----------------------create---------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.btn_Start);
        //txt_username = (EditText)findViewById(R.id.txt_UserName);
        //txt_uno = (EditText)findViewById(R.id.txt_Uno);

        btn_start.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_Start) {
            Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
//        intent.putStringArrayListExtra("CourseName", cname);
//        intent.putStringArrayListExtra("Teachers", cteachers);
            System.out.println("-----------------------jump---------------------");
            startActivity(intent);
        }
    }

}
