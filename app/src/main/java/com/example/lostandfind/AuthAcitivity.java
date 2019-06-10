package com.example.lap2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AuthActivity extends AppCompatActivity{

    EditText authEmail;         // 이메일 입력 텍스트
    Button authSend;            // 이메일 인증전송 버튼

    LayoutInflater emailauth;   // LayoutInflater
    View emailauthLayout;       //layout 을 담을 View
    Dialog emailAuth;          // emailAuth 객체
    EditText emailauth_value;

    /*카운트 다운 타이머에 관련된 필드*/
    TextView time_counter;   // 시간을 보여줌
    EditText emailauth_number;  // 인증번호를 입력
    Button emailauth_btn;   // 인증버튼
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 300*1000;    // 총시간 (300초=5분)
    final int COUNT_DOWN_INTERVAL = 1000;   //onTick 매소드를 호출할 간격(1초)
    private Object dialogLayout;
    private EditText Auth_input;
    private EditText edt_auth_input;
    Intent AuthActivityintent;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_autth);

        Auth_input = (EditText) findViewById(R.id.textview_sign_email); // 이메일정보
        edt_auth_input = (EditText)findViewById(R.id.edt_auth_input);   //사용자가 입력한 인증정보
        authSend = (Button) findViewById(R.id.btn_sign_sendemail);

        AuthActivityintent = getIntent(); // 전달받은 데이터 형식에 맞게 받아준다
        final String stringData = AuthActivityintent.getExtras().getString("randNum");
        final String stringEmail = AuthActivityintent.getExtras().getString("email");
        System.out.println("이메일 인증에서 인증값 확인하기 : "+stringData);
        System.out.println("이메일 인증에서 인증값 확인하기 : "+stringEmail);

        //  인증완료 버튼 누르면 ->  회원가입 화면전환
        Button emailauth = (Button) findViewById(R.id.btn_email_auth);
        emailauth.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                String randomAuth = '"' + edt_auth_input.getText().toString() + '"';    // 사용자가 입력하는 값 저장 변수 큰따으묘가 있어야하뮤
                // 조건문 (인증번호와 사용자 입력값 비교) if-else
                if(stringData.equals(randomAuth)){ //사용자 입력값과 주어직 인증번호가 일치하면
                    Toast.makeText(getApplicationContext(),"인증이 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                    onBackPressed();    // 전화면으로 가기
                }else{  // 일치하지 않으면 재전송 버튼을 누르면 서버로 재전송
                    Toast.makeText(getApplicationContext(), "인증번호를 잘못입력했습니다.", Toast.LENGTH_SHORT).show();
                    Button emailResend = (Button) findViewById(R.id.btn_email_resend);
                    emailResend.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "인증번호를 재전송 하였습니다.", Toast.LENGTH_SHORT).show();
                            //new AuthActivity.EmailJSONTask_2().execute("http://192.168.43.52:3000/sendEmail");
                            System.out.println("전송완료?????하고싶다 ㅠㅠㅠㅠㅠㅠㅠㅠㅠ");
                        }
                    });
                 /*   new AuthActivity.EmailJSONTask().execute("http://192.168.43.52:3000/sendEmail");
                    Intent authActivityIntent = new Intent(getApplicationContext(), AuthActivity.class);
                    authActivityIntent.putExtra("Key","value"); // 데이터를 저장하고 전달한다.
                    startActivity(authActivityIntent);*/
                }

                switch (view) {

                    case R.id.btnSend:

                        emailauth = LayoutInflater.from(this);
                        emailauthLayout = emailauth.inflate(R.layout.email_autth, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
                        emailAuth = new Dialog(this); //Dialog 객체 생성
                        emailAuth.setContentView(emailauthLayout); //Dialog에 inflate한 View를 탑재 하여줌
                        emailAuth.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
                        emailAuth.setOnCancelListener(); //다이얼로그를 닫을 때 일어날 일을 정의하기 위해 onCancelListener 설정
                        emailAuth.show(); //Dialog를 나타내어 준다.
                        countDownTimer();
                        break;

                    case R.id.btn_email_auth : //다이얼로그 내의 인증번호 인증 버튼을 눌렀을 시
                        emailauth_number = (EditText) findViewById(R.id.edt_auth_input);
                        String emailauth_number_text =  emailauth_number.getText().toString();
                        AuthActivityintent = getIntent(); //인텐드해서 받아온 값의 객체 선언
                        String randomAuth_intent = AuthActivityintent.getExtras().getString("randNum");
                        if(emailauth_number_text.equals(randomAuth_intent)){
                            Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
    }

    public void countDownTimer() { //카운트 다운 메소드

        time_counter = (TextView) findViewById(R.id.emailAuth_time_counter);
        //줄어드는 시간을 나타내는 TextView
        emailauth_number = (EditText) findViewById(R.id.edt_auth_input);
        //사용자 인증 번호 입력창
        emailauth_btn = (Button) findViewById(R.id.btn_email_auth);
        //인증하기 버튼


        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료

                emailAuth.cancel();

            }
        }.start();

        emailauth_btn.setOnClickListener((View.OnClickListener) this);
    }


    public void onCancel(DialogInterface emailauth) {
        countDownTimer.cancel();
    } //다이얼로그 닫을 때 카운트 다운 타이머의 cancel()메소드 호출

    //이메일 정보 서버로 값전달
    public class EmailJSONTask_2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userEmail", Auth_input.getText().toString());

                //accumulate이거 뒤에 데이터 전송하는거라 여기서 TEXTVIEW 로그인 뷰 긁어오면 데이터 전송은 가능 근데 전송한 데이터를 db로 넣어야할듯
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

    }
        //authEmail = (EditText)findViewById(R.id.textview_sign_email);
        //authSend = (Button)findViewById(R.id.btn_sign_sendemail);
        //authSend.setOnClickListener(this);
}
