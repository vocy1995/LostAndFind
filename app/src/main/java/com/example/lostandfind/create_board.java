package com.example.loatandfind;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class create_board extends AppCompatActivity {

    //콤보박스 값 넣기
    String[] type_question_items = {"게시글 선택", "분실물", "습득물"};
    String[] large_category_items = {"대분류 선택", "대분류1", "대분류2", "대분류3"};
    String[] middle_category_items = {"중분류 선택", "중분류1", "중분류2", "중분류3"};
    String[] small_category_items = {"소분류 선택", "소분류1", "소분류2", "소분류3"};
    String[] color_category_items = {"색깔 선택", "빨강", "초록", "노랑", "파랑", "검정", "분홍"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}
