package com.example.lostandfind;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.URL;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText id_edit,pw_edit;
    String id;
    String pw;
    String success_message;
    String name_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        id_edit=findViewById(R.id.textview_id_input);
        pw_edit=findViewById(R.id.textview_pw_input);
        /*Toolbar toolbar = findViewById(R.id.);
        setSupportActionBar(toolbar);*/

        //로그인화면(회원가입버튼) -> 회원가입 화면btn_login
        Button sign_btn = findViewById(R.id.btn_sign);
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
                new Login().execute("http://192.168.0.23:3000/login");

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

    public class Login extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {

                id=id_edit.getText().toString();
                pw=pw_edit.getText().toString();

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("id",id);
                jsonObject.accumulate("pw",pw);

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
                    System.out.println(buffer.toString());


                    name_get= buffer.toString();


                    System.out.println("test : "+ name_get);
                    return name_get;//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

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
            if(name_get.equals(null)==false){
                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("name",name_get); //댓글 작성시 필요한 id값 전송
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "아이디와 패스워드를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
