package com.example.lap2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ResetpwActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 부모를 받는다.
        setContentView(R.layout.reset_pw);      // 어떤 레이아웃을 가리키는가.
    }
}
