package com.example.lap2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class SignActivity extends AppCompatActivity {

    private Socket mSocket; // 소캣 io 연결

    private TextView tvName;        // 이름
    private EditText tvEmail;       // 이메일
    private TextView tvId;          // 아이디
    private TextView tvPw;          // 비밀번호
    private TextView tvPwConfirm;   // 비밀번호 재확인
    private Button btnSend;         // 이메일 인증보내기
    private Button btnCheck;        // 아이디 중복확인
    private Button btnSignup;       // 회원가입
    String randNum;

    @Override
    protected  void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.sign);

        //layout 객체 선언
        tvName = (TextView) findViewById(R.id.textview_sign_name);              // 이름
        tvEmail = (EditText) findViewById(R.id.textview_sign_email);            // 이메일
        tvId = (TextView) findViewById(R.id.textview_sign_id);                  // 아이디
        tvPw = (TextView) findViewById(R.id.textview_sign_pw);                  // 패스워드
        tvPwConfirm = (TextView) findViewById(R.id.textview_sign_repeatpw);     // 패스워드 재입력
        btnSend = (Button) findViewById(R.id.btn_sign_sendemail);               // 이메일 인증
        btnCheck = (Button) findViewById(R.id.btn_sign_checkid);                // 아이디 중복체크
        btnSignup = (Button) findViewById(R.id.btn_sign_signup);                // 회원가입 완료

        //회원가입화면(이메일 인증버튼) -> 이메일 인증 화면
        Button sendeamil_btn = (Button) findViewById(R.id.btn_sign_sendemail);
        sendeamil_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(tvEmail.getText().toString().length()==0){   //만약 사용자가 email을 입력하지 않으면
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{  //사용자가 email을 입력하고 send버튼을 누르면 서버로 전송
                    Toast.makeText(getApplicationContext(), "인증번호를 전송했습니다", Toast.LENGTH_SHORT).show();
                    new SignActivity.EmailJSONTask().execute("http://192.168.0.7:3000/sendEmail");
                }
            }
        });

        //회원가입화면(회원가입 완료 버튼) -> 회원가입 완료 토스트 출력 로그인 화면
        Button signup_btn = (Button) findViewById(R.id.btn_sign_signup);
        signup_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                new EmailJSONTask().execute("http://192.168.0.7:3000/test1");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    //이메일 정보 서버로 값전달
    public class EmailJSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userEmail", tvEmail.getText().toString());   //이메일값 서버로 보내느거임

                //accumulate이거 뒤에가 데이터 전송하는거라 여기서 TEXTVIEW 로그인 뷰 긁어오면 데이터 전송은 가능 근데 전송한 데이터를 db로 넣어야할듯
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에// response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();
                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            //System.out.println("postResult : " + result);
            randNum = result;
            Intent signActivityIntent = new Intent(getApplicationContext(), AuthActivity.class);
            signActivityIntent.putExtra("randNum",randNum); // 데이터를 저장하고 전달한다.
            signActivityIntent.putExtra("email",tvEmail.getText().toString()); // 이메일데이터를 저장하고 전달한다.

            //System.out.println("key" +randNum);
            startActivity(signActivityIntent);
        }
    }
}
