package com.example.lostandfind;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeLine extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arraylist;
    static String TITLE = "title";
    static String WRITER = "writer";
    static String TIME = "time";
    static String IMAGE = "image";
    static String CONTENT = "content";
    static String NO = "no";
    static String HASH_TAG = "hash_tag";

    ListView listview;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_main);
        Timeline_getJSON("http://192.168.0.116:3000/post");

    }


    private void Timeline_getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> { //비동기식으로 사용하기위해 AsyncTask 사용

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s != null) {
                    try {
                        loadIntoListView(s); //서버에서 데이터를 받고 넘겨줌
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
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();//url서비스 사용을 위한것
                    con.setRequestMethod("POST");
                    con.connect();

                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    } //서버에서 주는 데이터 저장
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
        arraylist = new ArrayList<HashMap<String, String>>(); //이름 : 정보 로 구분짓기 편하게 위해 HashMap<String, String> 사용

        System.out.println("JsonArray: " + jsonArray);

        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> map = new HashMap<String, String>(); //이름 : 정보 로 값을 정하기 위해 <String , String> 사용
            JSONObject obj = jsonArray.getJSONObject(i);

            map.put("title", obj.getString("title"));
            map.put("writer", obj.getString("writer"));
            map.put("time", obj.getString("time"));
            map.put("image", obj.getString("image"));
            map.put("hash_tag",obj.getString("hash_tag"));
            map.put("content",obj.getString("content"));
            map.put("no",obj.getString("no")); //서버에서 보내주는 값을 저장
            System.out.println("map : " +map);
            arraylist.add(map); //arraylist 에 hashmap을 넣어줌으로써 arraylist에서 ㅂ구분짓게 함
            System.out.println("\n arraylisttest : " + arraylist);
        }
        listview = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(TimeLine.this, arraylist); //listview를 실행시킬곳에 필요한 arraylist 전송
        listview.setAdapter(adapter);
    }


}