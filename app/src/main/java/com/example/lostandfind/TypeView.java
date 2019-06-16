package com.example.lostandfind;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TypeView extends AppCompatActivity{

    ArrayList<HashMap<String, String>> arraylist;
    static String TITLE = "title";
    static String BOARD_WRITER = "board_writer";
    static String TIME = "time";
    static String IMAGE_URL = "img_url";
    static String CONTENT = "content";
    static String NO = "no";
    static String HASH_TAG = "hash_tag";
    static String LOCATION = "location";
    ListView listview;
    TypeViewAdapter adapter;
    String name_get,type_get,city_get;
    String Search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginActivity a = new LoginActivity();
        Intent get = getIntent();
        name_get = get.getStringExtra("name");
        type_get = get.getStringExtra("type");
        city_get = get.getStringExtra("city");
        System.out.println("id_test_get: " + name_get);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_main);
        openOptionsMenu();

        Timeline_getJSON("http://192.168.0.23:3000/type");

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
                if (s != null) {
                    try {
                        loadIntoListView(s); //서버에서 데이터를 받고 넘겨줌
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.accumulate("type_question",type_get);
                    jsonObject.accumulate("location",city_get);

                    HttpURLConnection con = null;
                    BufferedReader reader = null;
                    try {
                        URL url = new URL(urlWebService);
                        con = (HttpURLConnection) url.openConnection();//url서비스 사용을 위한것
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

                        return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임;
                    }catch(Exception e){

                    }
                }catch (Exception e){

                }
                return null;
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
            map.put("type_question", obj.getString("type_question"));
            map.put("time", obj.getString("time"));
            map.put("img_url", obj.getString("img_url"));
            map.put("hash_tag", obj.getString("hash_tag"));
            map.put("content", obj.getString("content"));
            map.put("no", obj.getString("no")); //서버에서 보내주는 값을 저장
            map.put("location", obj.getString("location"));
            map.put("board_writer",obj.getString("board_writer"));

            System.out.println("map : " + map);
            arraylist.add(map); //arraylist 에 hashmap을 넣어줌으로써 arraylist에서 ㅂ구분짓게 함
            System.out.println("\n arraylisttest : " + arraylist);
        }
        Collections.reverse(arraylist); //최신순서대로 글을 띄우기 위해 reverse 사용
        listview = (ListView) findViewById(R.id.listview);
        adapter = new TypeViewAdapter(TypeView.this, arraylist,name_get); //listview를 실행시킬곳에 필요한 arraylist 전송;
        listview.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Search_text=s;

                if(!(Search_text.equals(null)==true)){
                    System.out.println("searcj  test  :" + Search_text);
                    Intent search_result = new Intent(getApplicationContext(),SearchResult.class);
                    search_result.putExtra("hash_tag",Search_text); //댓글 작성시 필요한 id값 전송
                    startActivity(search_result);
                }
                else {
                    Toast.makeText(getApplicationContext(),"찾고자하는값이 없습니다.", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
                //System.out.println(s);
            }
        });

        return true;
    }
}
