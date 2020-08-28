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



        is = getResources().openRawResource(R.raw.testing);
        workbook = null;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = workbook.getSheetAt(0);
        row = sheet.getRow(0);
        setTitle(String.valueOf(row.getCell(2)));
        sheet = workbook.getSheet(String.valueOf(row.getCell(2)));
        row = sheet.getRow(1);

        for(int i=0;i<Image.size();i++){
            if(Image.get(0).equals(String.valueOf(row.getCell(8)))){
                B1.setBackgroundResource(imgId[0]);
            }
            if(Image.get(1).equals(String.valueOf(row.getCell(9)))){
                B2.setBackgroundResource(imgId[1]);
            }
            if(Image.get(2).equals(String.valueOf(row.getCell(10)))){
                B3.setBackgroundResource(imgId[2]);
            }
            if(Image.get(3).equals(String.valueOf(row.getCell(11)))){
                B4.setBackgroundResource(imgId[3]);
            }
        }
        //Toast.makeText(this,String.valueOf(row.getCell(8)),Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,Image.get(0),Toast.LENGTH_SHORT).show();
        txv.setText(String.valueOf(row.getCell(0)));

        findViewById(R.id.B1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Question.this,"答案不是這個哦",Toast.LENGTH_SHORT).show();

            }
        });
        findViewById(R.id.B2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Question.this,"答案不是這個哦",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.B3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Question.this,"答案不是這個哦",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.B4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Question.this,"答對了",Toast.LENGTH_SHORT).show();
            }
        });

    }
}