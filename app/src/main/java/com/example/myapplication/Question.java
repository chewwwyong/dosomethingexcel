package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Question extends AppCompatActivity {

    Row row;
    Sheet sheet;
    InputStream is;
    XSSFWorkbook workbook;

    ImageView B1,B2,B3,B4;

    int[] imgId={R.drawable.end2,R.drawable.photo1,R.drawable.photo2,R.drawable.review};
    String[] imgname = {"B1.jpg","B2.jpg","B3.jpg","B4.jpg"};

    ArrayList<String> Image = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        TextView txv = findViewById(R.id.question);
        B1 = findViewById(R.id.B1);
        B2 = findViewById(R.id.B2);
        B3 = findViewById(R.id.B3);
        B4 = findViewById(R.id.B4);

        Image.add("B1.jpg");
        Image.add("B2.jpg");
        Image.add("B3.jpg");
        Image.add("B4.jpg");


        // 获取excel文件流
        is = getResources().openRawResource(R.raw.testing);
        // 获取workbook对象
        workbook = null;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取sheet对象
        sheet = workbook.getSheetAt(0);
        // 获取row对象
        row = sheet.getRow(0);
        setTitle(String.valueOf(row.getCell(2)));
        // 获取sheet对象
        sheet = workbook.getSheet(String.valueOf(row.getCell(2)));
        // 获取row对象
        row = sheet.getRow(1);

        txv.setText(String.valueOf(row.getCell(0)));

        findViewById(R.id.B1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row = sheet.getRow(1);
                for(int i=0;i<Image.size();i++){
                    if(Image.get(0).equals( row.getCell(8))){
                        B1.setBackgroundResource(R.drawable.end2);
                    }
                }

                Toast.makeText(Question.this,"答案不是這個哦",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.B2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row = sheet.getRow(1);
                for(int i=0;i<Image.size();i++){
                    if(Image.get(1).equals( row.getCell(9))){
                        B2.setBackgroundResource(R.drawable.photo1);
                    }
                }
                Toast.makeText(Question.this,"答案不是這個哦",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.B3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row = sheet.getRow(1);
                for(int i=0;i<Image.size();i++){
                    if(Image.get(2).equals( row.getCell(10))){
                        B3.setBackgroundResource(R.drawable.photo2);
                    }
                }
                Toast.makeText(Question.this,"答案不是這個哦",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.B4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row = sheet.getRow(1);
                for(int i=0;i<Image.size();i++){
                    if(Image.get(3).equals( row.getCell(11))){
                        B4.setBackgroundResource(R.drawable.photo3);
                    }
                }
                Toast.makeText(Question.this,"答對了",Toast.LENGTH_SHORT).show();
            }
        });

    }
}