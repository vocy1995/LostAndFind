package com.example.lap2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /*Toolbar toolbar = findViewById(R.id.);
        setSupportActionBar(toolbar);*/

        //로그인화면(회원가입버튼) -> 회원가입 화면btn_login
        Button sign_btn = (Button) findViewById(R.id.btn_sign);
        sign_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
            }
        });

        //로그인화면(로그인버튼) -> 홈 화면
        Button login_btn = findViewById(R.id.btn_login);
        login_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        // 로그인화면(패스워드 재설정버튼) -> 패스워드재설정 화면
        Button resetpw_btn = findViewById(R.id.btn_pw_reset);
        resetpw_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),ResetpwActivity.class);
                startActivity(intent);
            }
        });

        // 로그인화면(아이디 찾기버튼) -> 아이디찾기 화면
        Button findid_btn = findViewById(R.id.btn_id_find);
        findid_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),FindidActivity.class);
                startActivity(intent);
            }
        });



    }

   /* public void Login (View view){
        Intent login = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(login);
    }

    public void reset (View view){
        Intent reset = new Intent(LoginActivity.this,ResetpwActivity.class);
        startActivity(reset);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
