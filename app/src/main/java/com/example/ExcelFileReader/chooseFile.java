package com.example.ExcelFileReader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class chooseFile extends AppCompatActivity {

    Button btnfilechoose;
    TextView textoutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);
        textoutput = findViewById(R.id.textoutput);

        btnfilechoose = findViewById(R.id.btnfilechoose);
        btnfilechoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,0);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            textoutput.setText(uri.getPath());

        }
        else {
            textoutput.setText("取消選擇檔案!!");
        }
    }
}