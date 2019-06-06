package com.example.lostandfind;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.pm.ResolveInfo;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class CreateBoardActivity extends AppCompatActivity {
    //갤러리
    private final int PICK_FROM_CAMERA = 0;
    private final int PICK_FROM_ALBUM = 1;
    private final static int IMAGE_RESULT = 200;
    final int REQ_CODE_SELECT_IMAGE = 100;
    private File tempFile;
    String getImgURL = "";    //이미지 업로드에 사용될 URL
    String getImgName = "";
    private Uri mImageCaptureUri;   //카메라에 사용될 URL

    //데이터를 받아올 URL
    String url = "http://192.168.60.54:3000";

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

    //이미지 업로드 선언
    Bitmap mBitmap;
    CreateBoardApiService createBoardApiService;
    Uri picUri;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_board);
        askPermissions();
        initRetrofitClient();

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
                } else {
                    //중복확인 변수
                    int sameNum = 0;
                    stringTag = "";
                    //hashTag split
                    tagArray = hashTag_text.split("#");
                    for (int i = 0; i < tagArray.length; i++) {
                        stringTag += tagArray[i] + " ";
                    }
                    //동일한 해쉬태그가 있으면 저장이 불가능하다.
                    if (sameNum == 1) {
                        Toast.makeText(getApplicationContext(), "동일한 태그가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        //URL 바꿔라 이건 텍스트 보내는거다
                        new CreateBoardActivityJSONTask().execute("http://192.168.60.54:3000/createBoardText");
                        multipartImageUpload();
                    }
                }
                switch (view.getId()) {
                    case R.id.btn_save:
                        if (mBitmap != null) {
                            //new JSONTask().execute("http://localhost:3000/createBoard");
                        } else {
                            Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                        }
                        break;
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
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });

        //지도 버튼 클릭 이벤트 -> 지도출력
        ImageButton btn_map = (ImageButton) findViewById(R.id.map_btn);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면 이동시 데이터 전송
                //https://coding-factory.tistory.com/203
                title = (EditText) findViewById(R.id.title);
                title_text = title.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                if (type_question_text == "게시글 선택") {
                    type_question_text = "";
                }
                intent.putExtra("boardType", type_question_text);
                intent.putExtra("title", title_text);
                intent.putExtra("content", content_text);
                intent.putExtra("hashTag", hashTag_text);
                startActivity(intent);
            }
        });

        //콤보박스 값 넣기
        ArrayAdapter<String> adapter_type_question = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, type_question_items);
        adapter_type_question.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_question.setAdapter(adapter_type_question);
    }

    //서버에서 데이터 가져오기


    //서버로 값전달
    public class CreateBoardActivityJSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type_question", type_question_text);
                jsonObject.accumulate("title", title_text);
                jsonObject.accumulate("location", location_text);
                jsonObject.accumulate("hashTag", stringTag);
                jsonObject.accumulate("content", content_text);

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

    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        //URL 바꿔라 이건 이미지 업로드하는거다
        createBoardApiService = new Retrofit.Builder().baseUrl("http://192.168.60.54:3000").client(client).build().create(CreateBoardApiService.class);
    }

    public Intent getPickImageChooserIntent() {
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = findViewById(R.id.photo_btn);

            if (requestCode == IMAGE_RESULT) {


                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(mBitmap);
                }
            }

        }

    }


    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;
        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void multipartImageUpload() {
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");
            System.out.println("파일 명 : " + filesDir);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

            Call<ResponseBody> req = createBoardApiService.postImage(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
