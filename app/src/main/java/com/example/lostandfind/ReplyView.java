package com.example.lostandfind;

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
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReplyView extends Activity {
    // Declare Variables
    String title_no;
    ArrayList<HashMap<String, String>> arraylist;

    static String NO = "no";
    static String CONTENT = "content";
    static String TITLE_NO = "title_no";
    static String WRITER = "writer";

    ListView listview;
    ReplyViewAdapter adapter;
    EditText content;
    TextView post;
    String get_content;
    String success_message;

    String id_test;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comment_main);
        reply_load("http://192.168.60.54:3000/reply");
        post = findViewById(R.id.post);
        content = findViewById(R.id.add_comment); //댓글 텍스트뷰를 사용하기위한

        Intent i = getIntent();
        title_no = i.getStringExtra("no");
        id_test = i.getStringExtra("id");
        System.out.println("get_id_test : "+ id_test);

        post.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                new Reply_Post().execute("http://192.168.60.54:3000/replyPost");
                onBackPressed();
            }
        });
        // Execute loadSingleView AsyncTask
         //댓글 데이터를 받기위한 url 사용
    }

    private void reply_load(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //화면에 값이 출력 됌
                if(s != null) {
                    try {
                        loadIntoListView(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                   // Intent i = getIntent();

                    //title_no = i.getStringExtra("no");


                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
        private void loadIntoListView(String json) throws JSONException {
            JSONArray jsonArray = new JSONArray(json);
            arraylist = new ArrayList<HashMap<String, String>>();


            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject obj = jsonArray.getJSONObject(i);

                //ImageView[] imageViewList = new ImageView[]{fl};
                map.put("no", obj.getString("no"));
                map.put("content", obj.getString("content"));
                map.put("title_no", obj.getString("title_no"));
                map.put("writer", obj.getString("writer"));
                System.out.println("map " + map);
                if(map.get("title_no").equals(title_no)){ //Listview에서 글에 해당하는 번호를 title_no로 설정
                    arraylist.add(map);
                }
            }


            listview = (ListView) findViewById(R.id.listview_reply);
            // Pass the results into ListViewAdapter.java

            adapter = new ReplyViewAdapter(ReplyView.this, arraylist); //해당하는ㄴ 데이터를 출력하기위해 adapter사용및 arraylist 전송

            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
        }

    public class Reply_Post extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                get_content=content.getText().toString();

                System.out.println("reply_post: no + "+title_no);
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("title_no",title_no);
                jsonObject.accumulate("content",get_content);
                jsonObject.accumulate("writer",id_test);
                System.out.println("writer_post: no + "+id_test);

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
                    System.out.println("title_no: " + title_no);
                    System.out.println(success_message);
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
            onBackPressed();
        }
    }
}
