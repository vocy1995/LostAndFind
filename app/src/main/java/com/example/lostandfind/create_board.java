package com.example.lostandfind;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class create_board extends AppCompatActivity {
    TextView tvData;
    //콤보박스 값 넣기
    String[] type_question_items = {"게시글 선택", "분실물", "습득물"};
    String[] large_category_items = {"대분류 선택", "대분류1", "대분류2", "대분류3"};
    String[] middle_category_items = {"중분류 선택", "중분류1", "중분류2", "중분류3"};
    String[] small_category_items = {"소분류 선택", "소분류1", "소분류2", "소분류3"};
    String[] color_category_items = {"색깔 선택", "빨강", "초록", "노랑", "파랑", "검정", "분홍"};

    //Spinner 값 가져오기 및 변수 선언
    Spinner type_question = (Spinner) findViewById(R.id.type_question);
    String type_question_text = type_question.getSelectedItem().toString();

    Spinner large_catergory = (Spinner) findViewById(R.id.large_catergory);
    String large_catergory_text = large_catergory.getSelectedItem().toString();

    Spinner middle_category = (Spinner) findViewById(R.id.middle_category);
    String middle_category_text = middle_category.getSelectedItem().toString();

    Spinner small_category = (Spinner) findViewById(R.id.small_category);
    String small_category_text = small_category.getSelectedItem().toString();

    //EditText 값 가져오기 및 변수 선언
    EditText brand_name = (EditText) findViewById(R.id.brand_name);  //브랜드명
    String brand_name_text = brand_name.getText().toString();

    EditText location = (EditText) findViewById(R.id.location);  //습득장소
    String location_text = location.getText().toString();

    EditText storage_location = (EditText) findViewById(R.id.storage_location);  //보관장소
    String storage_location_text = storage_location.getText().toString();

    EditText content = (EditText) findViewById(R.id.content);    //내용
    String content_text = content.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_board);

        //취소 버튼 이벤트 -> 뒤로가기
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //저장 버튼 이벤트
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //빈공간확인
                if ((type_question_text == "게시판 선택") && (large_catergory_text == "대분류 선택") && (middle_category_text == "중분류 선택") &&
                        (small_category_text == "소분류 선택") && (brand_name_text.length() == 0) && (location_text.length() == 0) && (storage_location_text.length() == 0) &&
                        (content_text.length() == 0)) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    new JSONTask().execute("http://192.168.60.56:3000/data");
                }

            }
        });

        //콤보박스 값 넣기
        Spinner spinner_type_question = (Spinner) findViewById(R.id.type_question);
        ArrayAdapter<String> adapter_type_question = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, type_question_items);
        adapter_type_question.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type_question.setAdapter(adapter_type_question);

        Spinner spinner_large_category = (Spinner) findViewById(R.id.large_catergory);
        ArrayAdapter<String> adapter_large_category = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, large_category_items);
        adapter_large_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_large_category.setAdapter(adapter_large_category);

        Spinner spinner_middle_category = (Spinner) findViewById(R.id.middle_category);
        ArrayAdapter<String> adapter_middle_category = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, middle_category_items);
        adapter_middle_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_middle_category.setAdapter(adapter_middle_category);

        Spinner spinner_small_category = (Spinner) findViewById(R.id.small_category);
        ArrayAdapter<String> adapter_small_category = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, small_category_items);
        adapter_small_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_small_category.setAdapter(adapter_small_category);

        Spinner spinner_color_category = (Spinner) findViewById(R.id.color_category);
        ArrayAdapter<String> adapter_color_category = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, color_category_items);
        adapter_color_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_color_category.setAdapter(adapter_color_category);
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

        }
    }
}
