package com.example.lostandfind;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONObject;

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


public class CreateBoardActivity extends AppCompatActivity {
    //갤러리
    private final int PICK_FROM_CAMERA = 0;
    private final int PICK_FROM_ALBUM = 1;
    final int REQ_CODE_SELECT_IMAGE = 100;
    private File tempFile;
    String getImgURL = "";    //이미지 업로드에 사용될 URL
    String getImgName = "";
    private Uri mImageCaptureUri;   //카메라에 사용될 URL

    //데이터를 받아올 URL
    String url = "http://192.168.0.68:3000/androidSendData";

    //Spinner 객체 및 변수 선언
    Spinner type_question;
    String type_question_text;

    //EditText 객체 및 변수 선언
    EditText title, location, content, hashTag;
    String title_text, location_text, content_text, hashTag_text;

    //해시태그 배열
    String[] tagArray;
    String stringTag = "";
    //콤보박스에 들어갈 객체 선언하기

    //게시글 선언
    String[] type_question_items = {"게시글 선택", "분실물", "습득물"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_board);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        type_question = (Spinner) findViewById(R.id.type_question);

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
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateBoardActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {
                    Location gps_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (gps_location != null) {
                        String provider = gps_location.getProvider();
                        System.out.print(provider);
                    }
                }
                type_question_text = type_question.getSelectedItem().toString();

                title = (EditText) findViewById(R.id.title);
                title_text = title.getText().toString();

                location = (EditText) findViewById(R.id.location);
                location_text = location.getText().toString();

                content = (EditText) findViewById(R.id.content);
                content_text = content.getText().toString();

                hashTag = (EditText) findViewById(R.id.hashTag);
                hashTag_text = hashTag.getText().toString();

                //빈공간확인 -> 확인 후 빈공간이 없으면 게시글 정보 서버로 전달 후 저장
                if (type_question_text == "게시글 선택") {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (title_text.length() == 0) {   // 제목 확인
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (location_text.length() == 0) {   //장소확인
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (content_text.length() == 0) {    //내용확인
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (hashTag_text.length() == 0) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (tempFile == null) {
                    Toast.makeText(getApplicationContext(), "사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    //중복확인 변수
                    int sameNum = 0;
                    stringTag = "";
                    //hashTag split
                    tagArray = hashTag_text.split("#");
                    for (int i = 0; i < tagArray.length; i++) {
                        if (tagArray[i] == tagArray[i + 1]) {
                            sameNum = 1;
                            break;
                        }
                        stringTag += tagArray[i] + " ";
                    }
                    //동일한 해쉬태그가 있으면 저장이 불가능하다.
                    if (sameNum == 1) {
                        Toast.makeText(getApplicationContext(), "동일한 태그가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        new JSONTask().execute("http://192.168.1.19:3000/createBoard");
                    }
                }
            }
        });

        //갤러리 버튼 클릭 이벤트 ->갤러리 오픈
        //http://blog.naver.com/PostView.nhn?blogId=jjjjokk&logNo=220743286618
        //https://black-jin0427.tistory.com/120
        ImageButton btn_gall = (ImageButton) findViewById(R.id.photo_btn);
        btn_gall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //갤러리 오픈
                Intent gall_intent = new Intent(Intent.ACTION_PICK);
                gall_intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //gall_intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                //gall_intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //startActivityForResult(gall_intent, REQ_CODE_SELECT_IMAGE);
                startActivityForResult(gall_intent, PICK_FROM_ALBUM);
            }
        });

        //카메라 버튼 클릭 이벤트 -> 카메라 실행
        ImageButton btn_camera = (ImageButton) findViewById(R.id.camera_btn);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //임시로 사용할 파일의 경로를 생성
                String url = "tmp_" + String.valueOf(System.currentTimeMillis() + ".jpg");
                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(camera_intent, PICK_FROM_CAMERA);
            }
        });

        //지도 버튼 클릭 이벤트 -> 지도출력
        ImageButton btn_map = (ImageButton) findViewById(R.id.map_btn);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                if(type_question_text=="게시글 선택"){
                    type_question_text="";
                }
                intent.putExtra("boardType", "type_question_text");
                startActivity(intent);
            }
        });

        //콤보박스 값 넣기
        ArrayAdapter<String> adapter_type_question = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, type_question_items);
        adapter_type_question.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_question.setAdapter(adapter_type_question);
    }


    //선택된 이미지 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
                //Uri 스키마를 content://에서 file://로 변경한다.
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
                System.out.println("tempFile URI: " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (requestCode == PICK_FROM_CAMERA) {

        }
    }

    //서버에서 데이터 가져오기


    //서버로 값전달

    public class JSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                System.out.println(stringTag);
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type_question", type_question_text);
                jsonObject.accumulate("title", title_text);
                jsonObject.accumulate("location", location_text);
                jsonObject.accumulate("hashTag", stringTag);
                jsonObject.accumulate("content", content_text);
                jsonObject.accumulate("imagePath", Uri.fromFile(tempFile).toString());

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
    }

}
