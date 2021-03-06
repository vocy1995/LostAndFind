package com.example.lostandfind;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;

public class SignActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> arraylist;
    private TextView tvName;        // 이름
    private TextView tvEmail;       // 이메일
    private TextView tvId;          // 아이디
    private TextView tvPw;          // 비밀번호
    private TextView tvPwConfirm;   // 비밀번호 재확인
    private Button btnSend;         // 이메일 인증보내기
    private Button btnCheck;        // 아이디 중복확인
    private Button btnSignup;       // 회원가입
    private String name, email, id, pw, repeatpw;
    private EditText name_text, email_text, id_text, pw_text, repeatpw_text;
    String success_message;
    int idDuplicateNumber = 0;
    JSONArray jarray; //parse를 위한 jarray 사용

    //이메일 인증 확인
    int emailNum = 0;
    int idNum = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.sign);

        tvName = (TextView) findViewById(R.id.textview_sign_name);              // 이름
        tvEmail = (TextView) findViewById(R.id.textview_sign_email);            // 이메일
        tvId = (TextView) findViewById(R.id.textview_sign_id);                  // 아이디
        tvPw = (TextView) findViewById(R.id.textview_sign_pw);                  // 패스워드
        tvPwConfirm = (TextView) findViewById(R.id.textview_sign_repeatpw);     // 패스워드 재입력
        btnSend = (Button) findViewById(R.id.btn_sign_sendemail);               // 이메일 인증
        btnCheck = (Button) findViewById(R.id.btn_sign_checkid);                // 아이디 중복체크
        btnSignup = (Button) findViewById(R.id.btn_sign_signup);                // 회원가입 완료
        name_text = findViewById(R.id.textview_sign_name);
        email_text = findViewById(R.id.textview_sign_email);
        pw_text = findViewById(R.id.textview_sign_pw);
        repeatpw_text = findViewById(R.id.textview_sign_repeatpw);

        //회원가입화면(이메일 인증버튼) -> 이메일 인증 화면
        Button sendeamil_btn = (Button) findViewById(R.id.btn_sign_sendemail);
        sendeamil_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailNum = 1;
                Intent authActivityintent = new Intent(getApplicationContext(), AuthActivity.class);
                authActivityintent.putExtra("email", tvEmail.getText().toString());
                startActivity(authActivityintent);
            }
        });

        //회원가입화면(아이디 중복체크버튼) -> 결과메시지 출력
        Button sign_btn = (Button) findViewById(R.id.btn_sign_checkid);
        sign_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IDJSONTask().execute("http://192.168.0.23:3000/Duplicate");

            }
        });

        //회원가입화면(회원가입 완료 버튼) -> 회원가입 완료 토스트 출력 로그인 화면
        Button signup_btn = (Button) findViewById(R.id.btn_sign_signup);
        signup_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw = tvPw.getText().toString();
                repeatpw = tvPwConfirm.getText().toString();
                if (emailNum == 0) {
                    Toast.makeText(getApplicationContext(), "이메일 인증을 해주세요.", Toast.LENGTH_SHORT).show();
                }else if(idNum == 0) {
                    Toast.makeText(getApplicationContext(), "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else if (pw.equals(repeatpw)==false) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                } else {
                    new sign().execute("http://192.168.0.23:3000/sign");
                }

            }
        });
    }

    //아이디 중복확인을 위한 아이디 정보 서버로 값전달
    public class IDJSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                tvId = (TextView) findViewById(R.id.textview_sign_id);                  // 아이디
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", tvId.getText().toString());

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
            System.out.println(result);
            if(result.equals("ok")){
                System.out.println("ok");
                Toast.makeText(getApplicationContext(), "아이디가 중복되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                idNum = 1;
            }

        }
    }

    //이메일 정보 서버로 값전달
    public class EmailJSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                tvEmail = (EditText) findViewById(R.id.textview_sign_email);

                System.out.println("사용자가 입력한 이메일  : " + tvEmail.getText().toString());
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", tvEmail.getText().toString());
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
                    /*System.out.println("받은 값 "+buffer);
                    String json_add = buffer.substring(0,buffer.length());
                    jarray = new JSONArray(json_add);
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject obj = jarray.getJSONObject(0);
                    //ImageView[] imageViewList = new ImageView[]{fl};
                    map.put("result",obj.getString("result")); //93~99까지는 id값을 정확히 파싱하기 위해 하는 작업
                    success_message = map.get("result");// id값을 find_id에 넣는다*/
                    success_message = buffer.toString();
                    System.out.println("넣은 값 " + success_message);
                    //System.out.println(success_message);
                    return success_message;//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
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

        }
    }

    //서버로 값전달
    public class sign extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                name = tvName.getText().toString();
                email = tvEmail.getText().toString();
                id = tvId.getText().toString();
                pw = tvPw.getText().toString();

                System.out.println("name  : " + name);
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", name);
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("id", id);
                jsonObject.accumulate("pw", pw);

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
                    success_message = buffer.toString();
                    System.out.println("넣은 값 " + success_message);
                    return success_message;//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
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
            if (success_message.equals("success")==true) {
                System.out.println("성공");
                Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
    }
}
