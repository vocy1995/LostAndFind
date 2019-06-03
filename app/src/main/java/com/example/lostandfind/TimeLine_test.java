package com.example.lostandfind;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeLine_test extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<String> rankArray, countryArray, populationArray, flageArray;
    RecyclerView recyclerView;
    TextView ra,co,po;
    ImageView fl;
    Bitmap bitmap;
    String rank,coun,pop,flag;
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
        setContentView(R.layout.listview_main);
        //ra = findViewById(R.id.textView);
        //co = findViewById(R.id.textView5);
        //po = findViewById(R.id.textView4);
        //fl = findViewById(R.id.imageView);

        //listView = (ListView) findViewById(R.id.listView);
        getJSON("http://192.168.60.54:3000/post");
    }


    private void getJSON(final String urlWebService) {

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
                    con.setRequestMethod("GET");

                    /*OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("title_no","test");
                    System.out.println("jsonObject : "+jsonObject.toString());*/

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
        //JSONArray test_temp = new JSONArray();
        // String[] heroes = new String[jsonArray.length()];
       // String[] Rank = new String[jsonArray.length()];
        //String[] Coun = new String[jsonArray.length()];
        //String[] Pop = new String[jsonArray.length()];
        //String[] Flag = new String[jsonArray.length()];

        System.out.println("JsonArray: " + jsonArray);

        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            JSONObject obj = jsonArray.getJSONObject(i);

            //ImageView[] imageViewList = new ImageView[]{fl};
            map.put("title", obj.getString("title"));
            map.put("writer", obj.getString("writer"));
            map.put("time", obj.getString("time"));
            map.put("image", obj.getString("image"));
            map.put("hash_tag",obj.getString("hash_tag"));
            map.put("content",obj.getString("content"));
            map.put("no",obj.getString("no"));
            System.out.println("map : " +map);
            arraylist.add(map);
            System.out.println("\n arraylisttest : " + arraylist);
        }

        /*for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonInfo = jsonArray.getJSONObject(i);
            rankArray.add(jsonInfo.getString("rank"));
            populationArray.add(jsonInfo.getString("population"));
        }
        System.out.println(rankArray);
        System.out.println(populationArray);*/
        listview = (ListView) findViewById(R.id.listview);
        // Pass the results into ListViewAdapter.java
        adapter = new ListViewAdapter(TimeLine_test.this, arraylist);
        // Binds the Adapter to the ListView
        listview.setAdapter(adapter);
        // Close the progressdialog
    }


}
