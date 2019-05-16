package com.example.timeline_test;

import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;

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

import java.lang.String;

public class test_node_js extends AppCompatActivity {

    TextView tvData;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_nodejs_1);

        tvData = findViewById(R.id.textView4);

        btn =findViewById(R.id.httpTest);

        //버튼이 클릭되면 여기 리스너로 옴

        btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                new JSONTask().execute("http://192.168.0.143:3000/post");//AsyncTask 시작시킴
                //http://자신의 IP주소:포트

            }

        });

    }

    public class JSONTask extends AsyncTask<String, Void, String> {


        @Override

        protected String doInBackground(String... urls) {

            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("user_id", "androidTest");

                jsonObject.accumulate("name", "yun");
                //accumulate이거 뒤에가 데이터 전송하는거라 여기서 TEXTVIEW 로그인 뷰 긁어오면 데이터 전송은 가능 근데 전송한 데이터를 db로 넣어야할듯
                HttpURLConnection con = null;

                    BufferedReader reader = null;

                    try{



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

                    while((line = reader.readLine()) != null){

                        buffer.append(line);

                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                } finally {

                    if(con != null){

                        con.disconnect();

                    }

                    try {

                        if(reader != null){

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

            super.onPostExecute(result);
            //FetchPlacesListTask;
            tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }
    }
}
