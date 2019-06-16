package com.example.lostandfind;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
import java.net.URL;

public class AuthActivity extends AppCompatActivity {

    TextView authEmail;         // 이메일 입력 텍스트
    Button authSend;            // 이메일 인증전송 버튼
    TextView emailAuth_time_coutner;    //인증시간 출력 텍스트
    EditText edt_auth_input;    //사용자 인증번호 입력창
    Button btn_email_auth;  //인증하기 버튼클릭
    Button authReSend;

    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    LayoutInflater dialog;   // LayoutInflater
    Dialog authDialog;          // dialog 객체

    Intent fromSignIntent;
    String userEmail, authRanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_auth);

        authDialog = new Dialog(this);  //Dialog 객체 생성

        authEmail = (TextView) findViewById(R.id.textview_sign_email);
        authSend = (Button) findViewById(R.id.btn_sign_sendemail);

        //Activity실행시 인증번호를 전송한다.
        new EmaileReSendJSONTask().execute("http://192.168.0.23:3000/sendEmail");
        System.out.println("authRanText : " + authRanText);
        //실행시 인증시간 줄어듬
        countDownTimer();

        fromSignIntent = getIntent();
        userEmail = fromSignIntent.getExtras().getString("email");
        System.out.println("사용자가 입력한 이메일 : " + fromSignIntent.getExtras().getString("email"));

        //재전송 버튼 클릭 이벤트
        authReSend = (Button) findViewById(R.id.btn_email_resend);
        btn_email_auth = (Button) findViewById(R.id.btn_email_auth);
        authReSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EmaileReSendJSONTask().execute("http://192.168.0.68:3000/sendEmail");
                btn_email_auth.setVisibility(View.VISIBLE);
                authReSend.setVisibility(View.INVISIBLE);
                countDownTimer();
            }
        });
        //인증하기 버튼 클릭 이벤트
        btn_email_auth.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(authRanText);

                //사용자 인증 번호 입력창
                edt_auth_input = (EditText) findViewById(R.id.edt_auth_input);
                String edt_auth_input_text = edt_auth_input.getText().toString();
                boolean num = authRanText.equals(edt_auth_input.getText().toString());
                if (num) {
                    System.out.println("성공했오");
                    Toast.makeText(getApplicationContext(), "이메일 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    //0.5초후 뒤로가기
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                            onBackPressed();
                        }
                    }, 500);
                }
            }
        });
    }

    public void countDownTimer() { //카운트 다운 메소드

        //줄어드는 시간을 나타내는 TextView
        emailAuth_time_coutner = (TextView) findViewById(R.id.emailAuth_time_counter);
        //인증하기 버튼
        btn_email_auth = (Button) findViewById(R.id.btn_email_auth);


        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    emailAuth_time_coutner.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    emailAuth_time_coutner.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료

                authDialog.cancel();
                //재전송 버튼 보이게 하기
                authReSend = (Button) findViewById(R.id.btn_email_resend);
                authReSend.setVisibility(View.VISIBLE);
                //시간을 0:00으로 지정
                emailAuth_time_coutner = (TextView) findViewById(R.id.emailAuth_time_counter);
                emailAuth_time_coutner.setText("0:00");
                //인증하기 버튼 안보이게하기
                btn_email_auth = (Button) findViewById(R.id.btn_email_auth);
                btn_email_auth.setVisibility(View.INVISIBLE);


            }
        }.start();
    }

    //이메일 재전송 서버에 값 보내는 메소드
    public class EmaileReSendJSONTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            try {
                fromSignIntent = getIntent();
                userEmail = fromSignIntent.getExtras().getString("email");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userEmail", userEmail);

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
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
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
        protected void onPostExecute(String result) {
            authRanText = result;
            System.out.println(authRanText);
        }
    }
}
