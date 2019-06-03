package com.example.lostandfind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleItemView extends Activity {
    // Declare Variables
    String title_no;
    ArrayList<HashMap<String, String>> arraylist;

    static String NO = "no";
    static String BOARD_CONTENT = "board_content";
    static String TITLE_NO = "title_no";
    static String BOARD_WRITER = "board_writer";

    ListView listview;
    ReplyViewAdapter adapter;

    LinearLayoutManager Manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.post_item);
        // Execute loadSingleView AsyncTask
        getJSON("http://192.168.60.54:3000/reply");
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

                    Intent i = getIntent();

                    title_no = i.getStringExtra("no");
                    System.out.println("title no : " + title_no);
                    if(title_no.equals(i.getStringExtra("no"))){
                        System.out.println("equals success");
                    }

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

            System.out.println("JsonArray: " + jsonArray);
            int count=0;
            int array_list_count=0;
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject obj = jsonArray.getJSONObject(i);

                //ImageView[] imageViewList = new ImageView[]{fl};
                map.put("no", obj.getString("no"));
                map.put("board_content", obj.getString("board_content"));
                map.put("title_no", obj.getString("title_no"));
                map.put("board_writer", obj.getString("board_writer"));



                if(map.get("title_no").equals(title_no)){
                    System.out.println("map : " +map);
                    System.out.println("성공 한 값 : " + title_no);
                    arraylist.add(map);
                }
            }





            listview = (ListView) findViewById(R.id.listview_reply);
            // Pass the results into ListViewAdapter.java

            adapter = new ReplyViewAdapter(SingleItemView.this, arraylist);

            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
        }
}
