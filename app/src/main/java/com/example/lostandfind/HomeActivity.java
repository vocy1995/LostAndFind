package com.example.lostandfind;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//액티비티라는것을 상속해주어야 한다.
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 부모를 받는다.
        setContentView(R.layout.home);      // 어떤 레이아웃을 가리키는가.

    }
}
