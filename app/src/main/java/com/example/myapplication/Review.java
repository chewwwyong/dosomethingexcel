package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nuwarobotics.lib.voice.ifly.model.answer.Answer;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Review extends AppCompatActivity {

    TextView qA,qB,qC,qD,title;
    ImageView p1,p2,p3,p4;

    Row row;
    Sheet sheet;
    InputStream is;
    XSSFWorkbook workbook;

    int current_sheet = 0;
    int all_sheet = 0;
    int num= 0 ;
    int times=0;

    DataFormatter fmt;

    ArrayList<String> Question = new ArrayList<>();
    ArrayList<String> Motion_R = new ArrayList<>();

    ArrayList<String> Type = new ArrayList<>();
    ArrayList<String> Text = new ArrayList<>();
    String nowchoose = "Z";
    String ans;
    ArrayList<String> Picture = new ArrayList<>();
    ArrayList<String> Answer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        qA = findViewById(R.id.qA);
        qB = findViewById(R.id.qB);
        qC = findViewById(R.id.qC);
        qD = findViewById(R.id.qD);
        title = findViewById(R.id.title);

        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);

        qA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "A";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    exe();
                }
            }
        });

        qB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nowchoose = "B";
                if(ans.equals(nowchoose)){
                    exe();
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();

                }
                //Toast.makeText(Review.this,ans,Toast.LENGTH_SHORT).show();
                //Toast.makeText(Review.this,nowchoose,Toast.LENGTH_SHORT).show();
            }
        });

        qC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "C";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    exe();
                }
            }
        });

        qD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "D";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    exe();
                }
            }
        });

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "A";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    exe();
                }
            }
        });

        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "B";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    exe();
                }
            }
        });

        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "C";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    exe();
                }
            }
        });

        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "D";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    exe();
                }
            }
        });

        Intent it = this.getIntent();
        int current_sheet = it.getIntExtra("current_sheet",0);
        setexcel();
        sheet = workbook.getSheet(String.valueOf(row.getCell(current_sheet)));
        String is_Review = String.valueOf(row.getCell(current_sheet)).substring(0,6);
        deal_Review(sheet);
        exe();


    }

    public void setexcel() {
        // 取得excel文件流
        is = getResources().openRawResource(R.raw.blessing_09182020_revised);
        // 取得workbook對象
        workbook = null;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 取得sheet對象
        all_sheet = workbook.getNumberOfSheets();
        //Toast.makeText(MainActivity.this,String.valueOf(all_sheet),Toast.LENGTH_SHORT).show();
        sheet = workbook.getSheetAt(0);
        // 取得row對象
        row = sheet.getRow(0);
        fmt = new DataFormatter();
        times = sheet.getPhysicalNumberOfRows();
    }

    public void deal_Review(Sheet sheet){

        //處理Question
        int numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows()-1){
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String ssss = fmt.formatCellValue(row.getCell(0, Row.RETURN_BLANK_AS_NULL));
            if (!ssss.trim().isEmpty()) {
                Question.add(ssss);
                // Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
                numrow++;
            }
        }

        //處理Type
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows()-1){
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String ssss = fmt.formatCellValue(row.getCell(5, Row.RETURN_BLANK_AS_NULL));
            if (!ssss.trim().isEmpty()) {
                Type.add(ssss);
                //   Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
                numrow++;
            }
        }

        //處理Text
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows()-1) {
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String s1 = fmt.formatCellValue(row.getCell(6, Row.RETURN_BLANK_AS_NULL));
            String s2 = fmt.formatCellValue(row.getCell(7, Row.RETURN_BLANK_AS_NULL));
            String s3 = fmt.formatCellValue(row.getCell(8, Row.RETURN_BLANK_AS_NULL));
            String s4 = fmt.formatCellValue(row.getCell(9, Row.RETURN_BLANK_AS_NULL));
            Text.add(s1);
            Text.add(s2);
            Text.add(s3);
            Text.add(s4);
            // Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
            numrow++;
        }

        //處理Answer
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows()-1){
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String ssss = fmt.formatCellValue(row.getCell(14, Row.RETURN_BLANK_AS_NULL));
            if (!ssss.trim().isEmpty()) {
                Answer.add(ssss);
                //   Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
                numrow++;
            }
        }
    }

    public void exe(){
        //num = 0;
        if(times>=0){
            if(Type.get(num).equals("Text")){
                ans = Answer.get(num);
                initview();
                title.setText(Question.get(num));
                qA.setText(Text.get(num));
                qB.setText(Text.get(num+1));
                qC.setText(Text.get(num+2));
                qD.setText(Text.get(num+3));
                num++;
                times--;
            }

            else if (Type.get(num).equals("Picture")){
                ans = Answer.get(num);
                //Toast.makeText(Review.this,"123",Toast.LENGTH_SHORT).show();
                title.setText(Question.get(num));

                initview();

                p1.setBackgroundResource(R.drawable.photo1);;
                p2.setBackgroundResource(R.drawable.photo2);
                p3.setBackgroundResource(R.drawable.photo3);
                p4.setBackgroundResource(R.drawable.review);

                num++;
                times--;
            }
        }
        else{
            Intent it = new Intent(Review.this,Ending.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(it);
        }


    }

    public void initview(){
        title.setText("");
        qA.setText("");
        qB.setText("");
        qC.setText("");
        qD.setText("");

        p1.setBackgroundColor(000000);
        p2.setBackgroundColor(000000);
        p3.setBackgroundColor(000000);
        p4.setBackgroundColor(000000);

    }
}