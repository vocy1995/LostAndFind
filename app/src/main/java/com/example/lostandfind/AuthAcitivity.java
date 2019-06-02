package com.example.lap2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    TextView authEmail;         // 이메일 입력 텍스트
    Button authSend;            // 이메일 인증전송 버튼

    LayoutInflater emailauth;   // LayoutInflater
    View emailauthLayout;       //layout 을 담을 View
    Dialog emailAuth;          // emailAuth 객체

    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_autth);

        authEmail = (TextView)findViewById(R.id.textview_sign_email);
        authSend = (Button)findViewById(R.id.btn_sign_sendemail);
        authSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_email_auth :

                emailauth = LayoutInflater.from(this);
                emailauthLayout = emailauth.inflate(R.layout.email_autth,null); // emailauthLayout
                emailAuth = new Dialog(this);
                emailAuth.setContentView(emailauthLayout);
                emailAuth.setCanceledOnTouchOutside(false);
                emailAuth.show();
        }
    }
}
